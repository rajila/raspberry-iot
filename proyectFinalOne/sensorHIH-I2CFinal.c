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
 * Compilar: gcc -Wall -o <nombre> <nombre>.c -lwiringPi
 * Ejecutar: sudo ./<nombre>
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

#include <ctype.h>
#include <errno.h>

// Librerias para el proyecto
#include <pthread.h>
#include <geniePi.h>  //the ViSi-Genie-RaspPi library

#define BAUDRATE 9600

//#define PIVOTETEMP 27.0
#define RMINHUM 40.0
#define RMAXHUM 70.0

#define LRED 1 // GPIO18 (NumPin: 12)

#define RGBGREEN 4 // GPIO23 (NumPin: 16)
#define RGBBLUE 5 // GPIO24 (NumPin: 18)
#define RGBRED 6 // GPIO25 (NumPin: 22)

#define MAX 100

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
pthread_t _updateTimeDisplay;
pthread_t _getDataSensor;
struct genieReplyStruct _dataDisplay;
char *_fileNamePortDisplay = "/dev/serial0";
int _threshold = 27;

char _txtLogDisplay[MAX]  = "";
char *_txtTemperature = "";
char *_txtHumidity = "";

/**
 * Funcion que define el estado de los PINs como salida.
 */
void init()
{	
	pinMode(LRED, OUTPUT);
	pinMode(RGBGREEN, OUTPUT);
	pinMode(RGBBLUE, OUTPUT);
	pinMode(RGBRED, OUTPUT);

	genieWriteObj(GENIE_OBJ_LED_DIGITS, 0x00, _threshold); //Inicializa el valor en 27
	genieWriteObj(GENIE_OBJ_KNOB, 0x00, _threshold); //Inicializa el valor en 27
}

void loadConfiguration()
{
	wiringPiSetup();
	
	// Open port (r/w)
	if ((_fileDescriptor = open(_fileNamePort, O_RDWR)) < 0)
	{
		printf("Error al abrir el puerto I2C\n");
		exit(1);
	}

	// Set port options and slave devie address
	if (ioctl(_fileDescriptor, I2C_SLAVE, _address) < 0)
	{
		printf("Unable to get bus access to talk to slave\n");
		exit(1);
	}

	//open the Raspberry Pi's onboard serial port, baud rate is 9600
  	//make sure that the display module has the same baud rate
  	genieSetup(_fileNamePortDisplay, BAUDRATE);
}

/**
 * Funci?n que imprime la fecha y hora del sistema
 */
void printDateTime()
{
	_t = time(NULL);	
	_tm = localtime(&_t);
	strftime(_dateTime,100,"%d/%m/%Y %H:%M:%S", _tm);
	printf ("%s\n", _dateTime);
}

/**
 * Funci?n que evalua el valor de la Temperatura y actualiza el estado del LED 
 */
void processTemperature(int temperatura)
{
	genieWriteObj(GENIE_OBJ_ANGULAR_METER, 0x00, temperatura);

	//if( temperatura >= _threshold ) digitalWrite(LRED,HIGH);
	//else digitalWrite(LRED,LOW);
}

/**
 * Funci?n que evalua el valor de la Humedad y actualiza el estado del actuador RGB
 */
void processHumidity(int humedad)
{
	genieWriteObj(GENIE_OBJ_METER, 0x01, humedad);

	/*if( humedad < RMINHUM ) // RED ON
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
	}*/
}

void printLog(double temperature, double humidity)
{
	sprintf(_txtLogDisplay, "");
	_txtTemperature = "";
	_txtHumidity = "";

	dtostrf(temperature, 2, 1, _txtTemperature);
	dtostrf(humidity, 2, 1, _txtHumidity);
	sprintf(_txtLogDisplay, "Temperature%s: %s\nHumidity%s: %s\n\n", "(C)", _txtTemperature, "(%)", _txtHumidity);

	printf("Temperature%s: %.1f\n", "(C)", temperature);
	printf("Humidity%s: %.1f\n\n", "(%)", humidity);
	genieWriteStr(0x00, _txtLogDisplay); //write to Strings0
}

static void *updateTimeDisplay(void *data)
{
	time_t _tDisplay;
	struct tm *_tmDisplay;

	for(;;)
	{
		_tDisplay = time(NULL);
		_tmDisplay = localtime(&_tDisplay);

		genieWriteObj(GENIE_OBJ_LED_DIGITS, 0x01, _tmDisplay->tm_hour);
		genieWriteObj(GENIE_OBJ_LED_DIGITS, 0x02, _tmDisplay->tm_min);
		genieWriteObj(GENIE_OBJ_LED_DIGITS, 0x03, _tmDisplay->tm_sec);
	}
	return NULL;
}

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
		
		printDateTime();
		
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
				_lecturaHumedad = (_data[0] << 8) + _data[1];
				_humedadCal =_lecturaHumedad / 16382.0 * 100.0;
				
				// Temperature is located in next two bytes, padded by two trailing bits
				_lecturaTemperatura = (_data[2] << 6) + (_data[3] >> 2);
				_temperaturaCal = _lecturaTemperatura / 16382.0 * 165.0 - 40;

				printLog(_temperaturaCal, _humedadCal);
				processTemperature((int)_temperaturaCal);
				processHumidity((int)_humedadCal);
			}else 
				printf("Error, el Estado es != 0\n");
		}
		delay(3000);
	}
	return NULL;
}


void startPthread()
{
	(void)pthread_create(&_updateTimeDisplay,  NULL, updateTimeDisplay, NULL);
	(void)pthread_create(&_getDataSensor, NULL, getDataSensor, NULL);
}

//This is the event handler. Messages received from the display
//are processed here.
void handleGenieEvent(struct genieReplyStruct * reply)
{
  	if(reply->cmd == GENIE_REPORT_EVENT)    //check if the cmd byte is a report event
  	{
    	if(reply->object == GENIE_OBJ_KNOB) //check if the object byte is that of a knob
      	{
        	if(reply->index == 0)		  //check if the index byte is that of knob0
				_threshold = reply->data;
      	}
  	} 
  	else //if the received message is not a report event, print a message on the terminal window
    	printf("Unhandled event: command: %2d, object: %2d, index: %d, data: %d \r\n", reply->cmd, reply->object, reply->index, reply->data);
}

int main(int argc, char **argv)
{	
	loadConfiguration();
	init();
	startPthread();

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