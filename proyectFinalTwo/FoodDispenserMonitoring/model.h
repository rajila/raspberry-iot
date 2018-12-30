#ifndef MODEL_H
#define MODEL_H

#include <Arduino.h> //Permite utilizar los comandos de Arduino
#include <stdio.h>
#include <HX711.h>
#include "constants.h"

class SensorMeasurement 
{
  private:
    char id[100];
    char obProp[100];
    double out;
  
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
    char id[100]; // identificador del sensor: hum, temp
    char observationProperty[100]; // Indica el tipo de dato

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
    int analogicPin;
     
  public:
    void init(char id[], char observationProperty[], int analogicPin);
    SensorMeasurement monitor();
    double getDataSensor();
};

class DigitalBalanceSensor : public Sensor 
{ 
  private:
    int analogicPinDOUT;
    int analogicPinSCK;
    HX711 balanza;
    
  public:
    void init(char id[], char observationProperty[], int analogicPinDOUT, int analogicPinSCK );
    SensorMeasurement monitor();
    double getDataSensor();
    HX711 getBalanza();
};

#endif
