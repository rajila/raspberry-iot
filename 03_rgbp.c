#include <stdio.h>
#include <wiringPi.h>
#include <softPwm.h>

/**
 * https://www.admfactory.com/rgb-led-on-raspberry-pi-using-c/
 *
 */

#define LRED 1 // GPIO18
#define LBLUE 4 // GPIO23
#define LGREEN 5 // GPIO24

#define RMIN 0
#define RMAX 100

const int colors[] = {0xFF0000,0x00FF00,0x0000FF,0xFFFF00,0x00FFFF,0xFF00FF,0xFFFFFF,0x9400D3};

/**
 * Función ...
 */
int map(int colorValue, int minIn, int maxIn, int minOut, int maxOut)
{
	return (colorValue - minIn)*(maxOut - minOut)/(maxIn - minIn) + minOut;
}

void init()
{
	softPwmCreate(LRED, RMIN, RMAX);
	softPwmCreate(LBLUE, RMIN, RMAX);
	softPwmCreate(LGREEN, RMIN, RMAX);
}

void ledColorSet(int color)
{
	int _rValue, _bValue, _gValue;
	
	_rValue = (color & 0xFF0000) >> 16; // get red value
	_gValue = (color & 0x00FF00) >> 8; // get green value
	_bValue = (color & 0x0000FF) >> 0; // get blue value

    _rValue = map(_rValue, 0, 255, 0, 100);    //change a num(0~255) to 0~100
    _gValue = map(_gValue, 0, 255, 0, 100);
    _bValue = map(_bValue, 0, 255, 0, 100);
	
	softPwmWrite(LRED, RMAX - _rValue); // change duty cycle
	softPwmWrite(LGREEN, RMAX - _gValue);
	softPwmWrite(LBLUE, RMAX - _bValue);
}

int main(void)
{
	wiringPiSetup();
	init();
	for(;;)
	{
		for(int i=0; sizeof(colors)/sizeof(int); i++)
		{
			ledColorSet(colors[i]);
			delay(1000);
		}
	}
	return 0;
}