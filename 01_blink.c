#include <wiringPi.h>

/*
 * gcc -Wall -o <nombre> <nombre>.c -lwiringPi
 * sudo ./<nombre>
 * */

#define LRED 1

int main (void)
{
	wiringPiSetup();
	pinMode(LRED, OUTPUT);
	for(;;){
		digitalWrite(LRED,HIGH); delay(500);
		digitalWrite(LRED,LOW); delay(500);
	}
	return 0;
}
