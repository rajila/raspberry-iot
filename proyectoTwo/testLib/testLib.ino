#include "robologs.h"
 
robologs robby;
//WaterLevelSensor _waterLevelSensor((char*)"wtl", _WATERLEVEL_PIN);

void setup()
{
  Serial.begin(115200);
  // put your setup code here, to run once:
  pinMode(_PINLED, OUTPUT);
}

void loop() 
{
  robby.blinking(_PINLED, _TIMESLEEP);
  WaterLevelSensor _waterLevelSensor((char*)"wtl", _WATERLEVEL_PIN);
  delay(3000);
}
