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
#define LRED 1 // GPIO18

void init()
{
	pinMode(LRED, OUTPUT);
}

void procesarTemperatura(int temperatura)
{
	if( temperatura >= PIVOTETEMP) digitalWrite(LRED,HIGH);
	else digitalWrite(LRED,LOW);
}

int main(int argc, char **argv)
{
	int fd;                               /* File descriptor*/
	const char *fileName = "/dev/i2c-1";  /* Name of the port we will be using. On Raspberry 2 this is i2c-1, on an older Raspberry Pi 1 this might be i2c-0.*/
	int  address = 0x27;                  /* Address of Honeywell sensor shifted right 1 bit */
	unsigned char buf[4];                 /* Buffer for data read/written on the i2c bus */
	time_t _t;
	struct tm *_tm;
	char _dateTime[100];
	
	wiringPiSetup();
	init();

	/* Open port (r/w) */
	if ((fd = open(fileName, O_RDWR)) < 0)
	{
		printf("Failed to open i2c port\n");
		exit(1);
	}

	/* Set port options and slave devie address */
	if (ioctl(fd, I2C_SLAVE, address) < 0)
	{
		printf("Unable to get bus access to talk to slave\n");
		exit(1);
	}

	for(;;)
	{
		/* Initiate measurement by sending a zero bit (see datasheet for communication pattern) */
		if ((write(fd,buf,0)) != 0)
		{
			printf("Error writing bit to i2c slave\n");
			exit(1);
		}

		/* Wait for 100ms for measurement to complete.
		 Typical measurement cycle is 36.65ms for each of humidity and temperature, so you may reduce this to 74ms. */
		usleep(100000);
		
		_t = time(NULL);	
		_tm = localtime(&_t);
		strftime(_dateTime,100,"%d/%m/%Y %H:%M:%S", _tm);
		printf ("%s\n", _dateTime);
		
		/* read back data */
		if (read(fd, buf, 4) < 0)
		{
			printf("Unable to read from slave\n");
			exit(1);
		}
		else
		{
			/* Humidity is located in first two bytes */
			//printf("buf-0: %u\n",buf[0]);
			//printf("buf-0: %X\n",buf[0]);
			//printf("Val buf-0 0xC0: %u\n",buf[0] & 0xC0);
			//printf("Hex buf-0 0xC0: %X\n",buf[0] & 0xC0);
			//int _state = buf[0] & 0xC0;
			//printf("Estado: %d\n",_state);
			if((buf[0] & 0xC0) == 0)
			{	
				/* Temperature is located in next two bytes, padded by two trailing bits */
				int reading_temp = (buf[2] << 6) + (buf[3] >> 2);
				double temperature = reading_temp / 16382.0 * 165.0 - 40;
				printf("Temperatura%s: %.1f\n", "(ºC)", temperature);
				
				int reading_hum = (buf[0] << 8) + buf[1];
				double humidity =reading_hum / 16382.0 * 100.0;
				printf("Humedad%s: %.1f\n\n","(%)", humidity);
				
				procesarTemperatura((int)temperature);
			}else 
				printf("Error, el estado es diferente de 0\n");
		}
		delay(3000);
	}

	return 0;
}