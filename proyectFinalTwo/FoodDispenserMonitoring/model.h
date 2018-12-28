#ifndef MODEL_H
#define MODEL_H

#include <Arduino.h> //Permite utilizar los comandos de Arduino
#include <stdio.h>
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
    char id[100];
    char observationProperty[100];

  public:
    Sensor(char id[], char observationProperty[]);
    char* getID();
    char* getObservationProperty();

    void setID(char value[]);
    void setObservationProperty(char value[]);
};

class WaterLevelSensor : public Sensor 
{
  private: 
    double _valueSensor;
  
  public:
    WaterLevelSensor(char id[], int analPort);
    SensorMeasurement monitor();
};

class DigitalBalanceSensor : public Sensor 
{
  private: 
    double _valueSensor;
  
  public:
    DigitalBalanceSensor(char id[], int analPort);
    SensorMeasurement monitor();
};

#endif
