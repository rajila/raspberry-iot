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

#define PIVOTETEMP 27.0
#define RMINHUM 40.0
#define RMAXHUM 70.0

#define LRED 1 // GPIO18

#define RGBGREEN 4 // GPIO23
#define RGBBLUE 5 // GPIO24
#define RGBRED 6 // GPIO25

int _fileDescriptor; // File descriptor
const char *_fileNamePort = "/dev/i2c-1"; // Name of the port we will be using. On Raspberry 2 this is i2c-1, on an older Raspberry Pi 1 this might be i2c-0.
int  _address = 0x27; // Address of Honeywell sensor shifted right 1 bit
unsigned char _data[4]; // Buffer for data read/written on the i2c bus
time_t _t;
struct tm *_tm;
char _dateTime[100];

int _lecturaTemperatura;
int _lecturaHumedad;
double _temperaturaCal;
double _humedadCal;

/**
 * Funci?n que define el estado de los PINs como salida.
 */
void init()
{
	wiringPiSetup();
	
	pinMode(LRED, OUTPUT);
	pinMode(RGBGREEN, OUTPUT);
	pinMode(RGBBLUE, OUTPUT);
	pinMode(RGBRED, OUTPUT);
}

/**
 * Funci?n que imprime la fecha y hora del sistema
 */
void imprimirDateTime()
{
	_t = time(NULL);	
	_tm = localtime(&_t);
	strftime(_dateTime,100,"%d/%m/%Y %H:%M:%S", _tm);
	printf ("%s\n", _dateTime);
}

/**
 * Funci?n que evalua el valor de la Temperatura y actualiza el estado del LED 
 */
void procesarTemperatura(int temperatura)
{
	if( temperatura >= PIVOTETEMP) digitalWrite(LRED,HIGH);
	else digitalWrite(LRED,LOW);
}

/**
 * Funci?n que evalua el valor de la Humedad y actualiza el estado del actuador RGB
 */
void procesarHumedad(int humedad)
{
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

int main(int argc, char **argv)
{	
	init();

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
		
		imprimirDateTime();
		
		// read back data
		if (read(_fileDescriptor, _data, 4) < 0)
		{
			printf("No se puede leer los datos del esclavo\n");
			exit(1);
		}
		else
		{
			if((_data[0] & 0xC0) == 0) // Verificamos que el Estado del Sensor es 0
			{	
				// Humidity is located in first two bytes
				_lecturaHumedad = (_data[0] << 8) + _data[1];
				_humedadCal =_lecturaHumedad / 16382.0 * 100.0;
				
				// Temperature is located in next two bytes, padded by two trailing bits
				_lecturaTemperatura = (_data[2] << 6) + (_data[3] >> 2);
				_temperaturaCal = _lecturaTemperatura / 16382.0 * 165.0 - 40;
				
				printf("Temperatura%s: %.1f\n", "(C)", _temperaturaCal);
				printf("Humedad%s: %.1f\n\n", "(%)", _humedadCal);
				
				procesarTemperatura((int)_temperaturaCal);
				procesarHumedad((int)_humedadCal);
			}else 
				printf("Error, el Estado es != 0\n");
		}
		delay(3000);
	}

	return 0;
}