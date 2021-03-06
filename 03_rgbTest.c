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
	srand(time(NULL));
	softPwmWrite(LRED,rand()%(RMAX-RMIN+1)+RMIN);
	softPwmWrite(LGREEN,rand()%(RMAX-RMIN+1)+RMIN);
	softPwmWrite(LBLUE,rand()%(RMAX-RMIN+1)+RMIN);
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
