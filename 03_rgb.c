#include <stdio.h>
#include <wiringPi.h>
#include <softPwm.h>

/**
 * gcc -Wall -o <nombre> <nombre>.c -lwiringPi
 * sudo ./<nombre>
 * https://www.admfactory.com/rgb-led-on-raspberry-pi-using-c/
 *
 */

#define LRED 1 // GPIO18
#define LBLUE 5 // GPIO24
#define LGREEN 4 // GPIO23

#define RMIN 0
#define RMAX 100

void init()
{
	softPwmCreate(LRED, RMIN, RMAX);
	softPwmCreate(LBLUE, RMIN, RMAX);
	softPwmCreate(LGREEN, RMIN, RMAX);
}

void ledOff()
{
	softPwmWrite(LRED,RMIN);
	softPwmWrite(LGREEN,RMIN);
	softPwmWrite(LBLUE,RMIN);
}

void softPwdLed(int ledPin)
{
	for(int i=0;i<RMAX;i++)
	{
		softPwmWrite(ledPin,i);
		delay(10);
	}
}

int main(void)
{
	wiringPiSetup();
	init();
	for(;;)
	{
		softPwdLed(LRED);
		ledOff(); delay(500);
		softPwdLed(LGREEN);
		ledOff(); delay(500);
		softPwdLed(LBLUE);
		ledOff(); delay(500);
	}
	return 0;
}
