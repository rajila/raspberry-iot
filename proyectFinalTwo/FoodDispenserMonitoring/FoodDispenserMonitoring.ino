#include "model.h"

WaterLevelSensor _wtlSensor;
DigitalBalanceSensor _dgbSensor;

void setup() 
{
  Serial.begin(115200);
  _wtlSensor.init((char*)"wtl",(char*)"dbl", _WATERLEVEL_PIN);
  _dgbSensor.init((char*)"dgb",(char*)"dbl",_DIGITALBALANCE_DOUT_PIN, _DIGITALBALANCE_CLK_PIN);
}

void loop() 
{
  // put your main code here, to run repeatedly:
}
