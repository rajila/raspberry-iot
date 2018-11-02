/**
 * Links: 
 *      https://github.com/karlrupp/i2cHoneywellHumidity/blob/master/i2cHoneywellHumidity.c
 *      https://poesiabinaria.net/2012/06/obtener-la-fecha-y-hora-formateada-en-c/
 *      http://rants.dyer.com.hk/rpi/humidity_i2c.html
 *		https://github.com/4dsystems/ViSi-Genie-RaspPi-Library/blob/master/geniePi.c
 *		
 * Conexion del Sensor HIH al Raspberry Pi
 *   SENSOR       RASPBERRY
 * VDD(1)    -->    3V3 (NumPin: 17)
 * VSS(2)    -->    GND (NumPin: 9)
 * SCL(3)    -->    GPIO3 (NumPin: 5)
 * SDA(4)    -->	GPIO2 (NumPin: 3)
 *
 * Para compilar el proyecto hacer uso del archivo Makefile, para esto ejecutar el siguiente comando:
 * Comando para compilar: make
 * Comando para Ejecutar: sudo ./sensorHIH-I2CFinal
 *
 */
 
#include <stdio.h>
#include <stdlib.h>
#include <linux/i2c-dev.h>
#include <fcntl.h>
#include <string.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <time.h>
#include <wiringPi.h>
#include <softPwm.h>
#include <math.h>

// Librerias para el proyecto
#include <pthread.h>
#include <geniePi.h>  //the ViSi-Genie-RaspPi library

#define BAUDRATE 9600 // Constante de configuración para el Display

#define RMINHUM 40.0 // Valor mínimo para evaluar el cambio de Humedad
#define RMAXHUM 70.0 // Valor máximo para evaluar el cambio de Humedad

#define LRED 1 // GPIO18 (NumPin: 12)

#define RGBGREEN 4 // GPIO23 (NumPin: 16)
#define RGBBLUE 5 // GPIO24 (NumPin: 18)
#define RGBRED 6 // GPIO25 (NumPin: 22)

#define MAX 100 // Constante General

// Variables de configuracion
int _fileDescriptor; // File descriptor
const char *_fileNamePort = "/dev/i2c-1"; // Name of the port we will be using. On Raspberry 2 this is i2c-1, on an older Raspberry Pi 1 this might be i2c-0.
int  _address = 0x27; // Address of Honeywell sensor shifted right 1 bit
unsigned char _data[4]; // Buffer for data read/written on the i2c bus

// Variables para lectura de Fecha y Hora del sistema
time_t _t;
struct tm *_tm;
char _dateTime[100];

// Variables para lectura de Sensor
int _lecturaTemperatura;
int _lecturaHumedad;
double _temperaturaCal;
double _humedadCal;

// Variables para Display 4D System
pthread_t _getDataSensor; // Variable que hace referencia al Hilo de obtiene los datos del Sensor
struct genieReplyStruct _dataDisplay; // Variable del Display para la obtención de los datos del Display
char *_fileNamePortDisplay = "/dev/serial0"; // Nombre del puerto del Display en la Raspberry
int _threshold = 27; // Variable dinámica que se actualiza desde el Display.
char *_formatoLog = "Temperature%s: %.1f\nHumidity%s: %.1f"; // Formato del Log para mostrar en el Display
char _txtLogDisplay[MAX]  = ""; // Variable q' guardar el Log de la temperatura y humedad para el Display

/**
 * Función que inicializa el modo de salida de los sensores y asigna el valor por defecto de los componentes del Display (LED-DIGITS, KNOB).
 */
void init()
{	
	// LEDs fisicos del prototipo
	pinMode(LRED, OUTPUT);
	pinMode(RGBGREEN, OUTPUT);
	pinMode(RGBBLUE, OUTPUT);
	pinMode(RGBRED, OUTPUT);

	genieWriteObj(GENIE_OBJ_LED_DIGITS, 0x00, _threshold); // Inicializa el valor en 27
	genieWriteObj(GENIE_OBJ_KNOB, 0x00, _threshold); // Inicializa el valor en 27
}

/**
 * Función que carga la configuración inicial del Display, Sensor y otros para la respectiva Comunicación con el Raspberry
 */
void loadConfiguration()
{
	wiringPiSetup(); // Librería para el uso de perifericos (PINs)
	
	// Open port (r/w) -- SENSOR
	if ((_fileDescriptor = open(_fileNamePort, O_RDWR)) < 0)
	{
		printf("Error al abrir el puerto I2C\n");
		exit(1);
	}

	// Set port options and slave devie address -- SENSOR
	if (ioctl(_fileDescriptor, I2C_SLAVE, _address) < 0)
	{
		printf("Unable to get bus access to talk to slave\n");
		exit(1);
	}

	// DISPLAY
	//open the Raspberry Pi's onboard serial port, baud rate is 9600
  	//make sure that the display module has the same baud rate
  	genieSetup(_fileNamePortDisplay, BAUDRATE);
}

/**
 * Función que imprime la fecha y hora del sistema en consola y también actualiza la hora del Display
 */
void processDateTime()
{
	_t = time(NULL);	
	_tm = localtime(&_t);
	strftime(_dateTime,100,"%d/%m/%Y %H:%M:%S", _tm);
	printf ("%s\n", _dateTime);

	// Actualiza la Hora en el Display
	genieWriteObj(GENIE_OBJ_LED_DIGITS, 0x01, _tm->tm_hour);
	genieWriteObj(GENIE_OBJ_LED_DIGITS, 0x02, _tm->tm_min);
	genieWriteObj(GENIE_OBJ_LED_DIGITS, 0x03, _tm->tm_sec);
}

/**
 * Función que evalua el THRESHOLD, en donde actualiza el estado del LED y además también actualiza el LED del Display 
 */
void processTemperature(int temperatura)
{
	genieWriteObj(GENIE_OBJ_ANGULAR_METER, 0x00, temperatura); // Actualiza el valor en el Display

	if( temperatura >= _threshold ) 
	{
		digitalWrite(LRED,HIGH); // RED ON
		genieWriteObj(GENIE_OBJ_USER_LED, 0x00, HIGH); // Enciende LED Display
	}
	else {
		digitalWrite(LRED,LOW); // RED OFF
		genieWriteObj(GENIE_OBJ_USER_LED, 0x00, LOW); // Apaga LED Display
	}

}

/**
 * Función que evalua el valor de la Humedad, en donde actualiza el estado del actuador RGBn y también actualiza el valor en el Display
 */
void processHumidity(int humedad)
{
	genieWriteObj(GENIE_OBJ_METER, 0x01, humedad); // Actualiza el valor en el Display 

	if( humedad < RMINHUM ) // RED ON
	{
		digitalWrite(RGBRED,HIGH);
		digitalWrite(RGBGREEN,LOW);
		digitalWrite(RGBBLUE,LOW);
	}else if( humedad >= RMINHUM && humedad <= RMAXHUM ){ // GREEN ON
		digitalWrite(RGBGREEN,HIGH);
		digitalWrite(RGBRED,LOW);
		digitalWrite(RGBBLUE,LOW);
	}else{ // BLUE ON
		digitalWrite(RGBBLUE,HIGH);
		digitalWrite(RGBRED,LOW);
		digitalWrite(RGBGREEN,LOW);
	}
}

/**
 * Función que registra el Log en consola y también registra el valor en el Display
 */
void processLog(double temperature, double humidity)
{
	sprintf(_txtLogDisplay, "%s",""); // Set la vacío ""
	sprintf(_txtLogDisplay, _formatoLog, "(C)",temperature,"(%)",humidity); // Actualizamos el valor con el formato respectivo

	printf("Temperature%s: %.1f\n", "(C)", temperature); // Imprime en consola
	printf("Humidity%s: %.1f\n\n", "(%)", humidity); // Imprime en consola

	genieWriteStr(0x00, _txtLogDisplay); // write to Strings0. Actualiza el Log en el Display
}

/**
 * Implementación del Hilo que captura la información del Sensor, en donde dicha es procesada
 * para realizar las respectivas evaluaciones y actualizar la información (Display & estado de los LEDs)
 */
static void *getDataSensor(void *data)
{
	for(;;)
	{
		// Initiate measurement by sending a zero bit (see datasheet for communication pattern)
		if ( (write(_fileDescriptor,_data,0)) != 0 )
		{
			printf("Error writing bit to i2c slave\n");
			exit(1);
		}

		//Wait for 100ms for measurement to complete. Typical measurement cycle is 36.65ms for each of humidity and temperature, so you may reduce this to 74ms.
		usleep(100000);

		processDateTime(); // Imprime la hora en consola y actualiza la hora en el Display
		
		// read back data
		if (read(_fileDescriptor, _data, 4) < 0)
		{
			printf("No se puede leer los datos del esclavo\n");
			exit(1);
		}
		else
		{
			if( (_data[0] & 0xC0) == 0 ) // Verificamos que el Estado del Sensor es 0
			{	
				// Humidity is located in first two bytes
				_lecturaHumedad = (_data[0] << 8) + _data[1]; // lectura del sensor
				_humedadCal =_lecturaHumedad / 16382.0 * 100.0; // Calculo para obtener la Humedad
				
				// Temperature is located in next two bytes, padded by two trailing bits
				_lecturaTemperatura = (_data[2] << 6) + (_data[3] >> 2); // lectura del sensor
				_temperaturaCal = _lecturaTemperatura / 16382.0 * 165.0 - 40; // Calculo para obtener la Temperatura

				processLog(_temperaturaCal, _humedadCal); // Imprime log (temperatura y humedad) en consola y actualiza el log en el Display
				processTemperature((int)_temperaturaCal); // Actualiza el valor de la temperatura en el Display y actualiza el valor del LED (ROJO)
				processHumidity((int)_humedadCal); // Actualiza el valor de la humedad en el Display y actualiza el valor de los LEDs (RGB)
			}else 
				printf("Error, el Estado es != 0\n");
		}
	}
	return NULL;
}

/**
 * Función que inicializa todos los hilos de la aplicacion
 */
void startPthread()
{
	(void)pthread_create(&_getDataSensor, NULL, getDataSensor, NULL);
}

/**
 * This is the event handler. Messages received from the display are processed here.
 * Referencia: https://github.com/4dsystems/ViSi-Genie-RaspPi-Demo-Slider/blob/master/demo1.c
 */
void handleGenieEvent(struct genieReplyStruct * reply)
{
  	if(reply->cmd == GENIE_REPORT_EVENT)    //check if the cmd byte is a report event
  	{
    	if(reply->object == GENIE_OBJ_KNOB) //check if the object byte is that of a knob
      	{
        	if(reply->index == 0)		  //check if the index byte is that of knob0
				_threshold = reply->data; // Actualiza el valor local de la variable THRESHOLSD, obtenida desde el Display
      	}
  	} 
  	else //if the received message is not a report event, print a message on the terminal window
    	printf("Unhandled event: command: %2d, object: %2d, index: %d, data: %d \r\n", reply->cmd, reply->object, reply->index, reply->data);
}

/**
 * Función principal de la aplicación
 */
int main(int argc, char **argv)
{	
	loadConfiguration(); // carga de configuración inicial para el Display y Sensor para la respectiva comunicación con el Raspberry
	init(); // Valores iniciales por defecto
	startPthread(); // Ejecición de los hilos

	for(;;)
	{
		while( genieReplyAvail() )      //check if a message is available
    	{
      		genieGetReply(&_dataDisplay);      //take out a message from the events buffer
      		handleGenieEvent(&_dataDisplay);   //call the event handler to process the message
    	}	
    	usleep(10000);                //10-millisecond delay.Don't hog the CPU in case anything else is happening
	}

	return 0;
}