#include "model.h"

char* SensorMeasurement::getID()
{
  return this->id;
}

char* SensorMeasurement::getObserverProperty() 
{
  return this->obProp;
}

double SensorMeasurement::getSensorOutput() 
{
  return this->out;
}

void SensorMeasurement::setID(char value[]) 
{
  strcpy(this->id,value);
}

void SensorMeasurement::setObserverProperty(char value[]) 
{
  if (!(strcmp(value, "int") == 0) && !(strcmp(value,"dbl") == 0) ) return;
  strcpy(this->obProp, value);
}

void SensorMeasurement::setSensorOutput(double value)
{
  if (strcmp(this->obProp, "int") == 0)
    this->out = (int)value;
  else
    this->out = value;
}

SensorMeasurement::SensorMeasurement(char id[], char observationProperty[], double sensorOutput)
{
  if (!(strcmp(observationProperty, "int") == 0) && !(strcmp(observationProperty, "dbl") == 0)) return;
  strcpy(this->id, id);
  strcpy(this->obProp, observationProperty);
  if (strcmp(obProp, "int") == 0)
    this->out = (int)sensorOutput;
  else
    this->out = sensorOutput;
}

/**
 *
 *
 */
void Sensor::init(char id[], char observationProperty[])
{
  strcpy(this->id, id);
  strcpy(this->observationProperty, observationProperty);
}

char* Sensor::getID()
{
  return this->id;
}

char* Sensor::getObservationProperty()
{
  return this->observationProperty;
}

void Sensor::setID(char value[])
{
  strcpy(this->id, value);
}

void Sensor::setObservationProperty(char value[])
{
  strcpy(this->observationProperty, value);
}

/**
 * 
 * 
 */
void WaterLevelSensor::init(char id[], char observationProperty[], int analogicPin)
{
  Sensor::init(id, observationProperty);
  this->analogicPin = analogicPin;
  pinMode(analogicPin, INPUT);
}

double WaterLevelSensor::getDataSensor()
{
  //return (analogRead(this->analogicPin)*100)/1024; // % de nivel de agua
  return analogRead(this->analogicPin); // % de nivel de agua
}

SensorMeasurement WaterLevelSensor::monitor()
{
  //Operación matematica con el valor del sensor
  return SensorMeasurement(Sensor::getID(), Sensor::getObservationProperty(), getDataSensor());
}

/**
 * 
 */
void DigitalBalanceSensor::init(char id[], char observationProperty[], int analogicPinDOUT, int analogicPinSCK)
{
  Sensor::init(id, observationProperty);
  this->analogicPinDOUT = analogicPinDOUT;
  this->analogicPinSCK = analogicPinSCK;
  this->balanza.begin(analogicPinDOUT,analogicPinSCK);
  this->balanza.set_scale();
  this->balanza.tare();
  this->balanza.set_scale(_CALIBRATIONFACTOR);
}

double DigitalBalanceSensor::getDataSensor()
{
  return this->balanza.get_units(); // Kg
}

HX711 DigitalBalanceSensor::getBalanza()
{
  return this->balanza;
}

SensorMeasurement DigitalBalanceSensor::monitor()
{
  return SensorMeasurement(Sensor::getID(), Sensor::getObservationProperty(), getDataSensor());
}
