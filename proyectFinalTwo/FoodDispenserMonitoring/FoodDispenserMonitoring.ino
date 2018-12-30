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
  Serial.print("Water: ");
  Serial.print(_wtlSensor.getDataSensor());
  Serial.println(" %");
  Serial.print("Balance: ");
  Serial.print(_dgbSensor.getDataSensor(),3);
  Serial.println(" kg");
  delay(5000);
}
