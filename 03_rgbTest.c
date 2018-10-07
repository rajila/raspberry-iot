#include <stdio.h>
#include <wiringPi.h>
#include <softPwm.h>
#include <stdlib.h>

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

void randCycle()
{
	softPwmWrite(LRED,rand()%(RMIN-RMAX+1)+RMAX);
	softPwmWrite(LGREEN,rand()%(RMIN-RAMX+1)+RMAX);
	softPwmWrite(LBLUE,rand()%(RMIN-RMAX+1)+RMAX);
	delay(500);
}


int main(void)
{
	wiringPiSetup();
	init();
	for(;;)
	{
		randCycle();
	}
	return 0;
}
