#ifndef ROBOLOGS_H
#define ROBOLOGS_H
 
#include <Arduino.h> //Permite utilizar los comandos de Arduino
#include <stdio.h>

#define _WATERLEVEL_PIN A0
#define _FORSESENSITIVER_PIN A0
#define _PINLED 2
#define _TIMESLEEP 250
 
class robologs //Definicion de la clase
{
 
    public:
 
    //Constructor de la clase
    robologs();
 
    void blinking(int pin, int time);
 
    private:
 
    //Nada que declarar
};

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

class ForceSensitiveResistorSensor : public Sensor 
{
  private: 
    double _valueSensor;
  
  public:
    ForceSensitiveResistorSensor(char id[], int analPort);
    SensorMeasurement monitor();
};
 
#endif
