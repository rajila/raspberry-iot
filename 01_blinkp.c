#include <wiringPi.h>
#include <stdio.h>

/*
 * gcc -Wall -o <nombre> <nombre>.c -lwiringPi
 * sudo ./<nombre>
 *
 * Link ayuda: https://www.admfactory.com/breathing-light-led-on-raspberry-pi-using-c/
 * */

#define LRED 1
#define MAX 1024

int main (void)
{
	wiringPiSetup();
	pinMode(LRED, PWM_OUTPUT);

	for(;;){
		for(int i=0;i<MAX;i++)
		{
			pwmWrite(LRED,i);
			delay(1);
		}
		delay(1000);
		for(int i=MAX-1;i>=0;i--)
		{
			pwmWrite(LRED,i);
			delay(1);
		}
	}
	return 0;
}
