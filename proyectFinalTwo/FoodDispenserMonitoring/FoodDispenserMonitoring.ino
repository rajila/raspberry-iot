#include <WiFi.h>
#include <ThingSpeak.h>
#include "model.h"

WiFiClient  _client;

WaterLevelSensor _wtlSensor;
DigitalBalanceSensor _dgbSensor;

void setupWIFI() 
{
  WiFi.begin(_SECRET_SSID, _SECRET_PASS); // Conexion a Internet
  while (WiFi.status() != WL_CONNECTED) {delay(500);Serial.print(".");}
  Serial.println();
}

void setup() 
{
  Serial.begin(115200);
  _wtlSensor.init((char*)"wtl",(char*)"dbl", _WATERLEVEL_PIN);
  _dgbSensor.init((char*)"dgb",(char*)"dbl",_DIGITALBALANCE_DOUT_PIN, _DIGITALBALANCE_CLK_PIN);
  setupWIFI();
  ThingSpeak.begin(_client);
}

void loop() 
{
  Serial.print("IP: ");
  Serial.println(WiFi.localIP());
  //Serial.print(_wtlSensor.getDataSensor());
  //Serial.println(" %");
  //Serial.print("Balance: ");
  //Serial.print(_dgbSensor.getDataSensor(),3);
  //Serial.println(" kg");
  delay(5000);
}
