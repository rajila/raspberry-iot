#ifndef MODEL_H
#define MODEL_H

#include <WiFi.h>
#include <ThingSpeak.h>
#include <Arduino.h> //Permite utilizar los comandos de Arduino
#include <stdio.h>
#include <HX711.h>
#include <ListLib.h>
#include <ArduinoJson.h>
#include <NTPClient.h>
#include <WiFiUdp.h>
#include <math.h>
#include "constants.h"

class SensorMeasurement 
{
  private:
    char _id[100];
    char _obProp[100];
    double _out;
  
  public:
    char* getID();
    char* getObserverProperty();
    double getSensorOutput();

    void setID(char value[]);
    void setObserverProperty(char value[]);
    void setSensorOutput(double value);

    SensorMeasurement(char id[], char observationProperty[], double sensorOutput);
};

class ISensor 
{
  public:
    virtual SensorMeasurement monitor() = 0;
};

class Sensor : public ISensor 
{
  private:
    char _id[100]; // identificador del sensor: hum, temp
    char _observationProperty[100]; // Indica el tipo de dato

  public:
    void init(char id[], char observationProperty[]);
    char* getID();
    char* getObservationProperty();

    void setID(char value[]);
    void setObservationProperty(char value[]);
};

class WaterLevelSensor : public Sensor 
{
  private:
    int _analogicPin;
     
  public:
    void init(char id[], char observationProperty[], int analogicPin);
    SensorMeasurement monitor();
    double getDataSensor();
};

class DigitalBalanceSensor : public Sensor 
{ 
  private:
    int _analogicPinDOUT;
    int _analogicPinSCK;
    HX711 _digitalBalance;
    
  public:
    void init(char id[], char observationProperty[], int analogicPinDOUT, int analogicPinSCK );
    SensorMeasurement monitor();
    double getDataSensor();
    HX711 getDigitalBalance();
};

class Thing
{
  private:
    char _idThing[100];
    char _stringJSON[200];
    List<ISensor*> _sensors;
    WiFiUDP _ntpUDP;
    NTPClient _timeClient;

    char* getStringJSON();
    
  public:
    void init(char idThing[]);
    void attach(ISensor &sensor);
    char* getIdThing();
    void sendDataThingSpeak();
};

#endif
