#include "model.h"

WiFiClient  _client;

WaterLevelSensor _wtlSensor;
DigitalBalanceSensor _dgbSensor;
Thing _thing;

void setupWIFI() 
{
  WiFi.begin(_SECRET_SSID, _SECRET_PASS); // Conexion a Internet
  while (WiFi.status() != WL_CONNECTED) {delay(500);Serial.print(".");}
  Serial.println();
}

void setup() 
{
  Serial.begin(115200);
  setupWIFI();
  _wtlSensor.init((char*)"wtl",(char*)"int", _WATERLEVEL_TRIG_PIN, _WATERLEVEL_ECHO_PIN);
  _dgbSensor.init((char*)"dgb",(char*)"int",_DIGITALBALANCE_DOUT_PIN, _DIGITALBALANCE_CLK_PIN);
  _thing.init((char*)"1234");
  _thing.attach(_wtlSensor);
  _thing.attach(_dgbSensor);
  ThingSpeak.begin(_client);
}

void loop() 
{
  _thing.sendDataThingSpeak();
  delay(_DELAY_SEND_DATA);
}

