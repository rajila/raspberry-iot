#include "robologs.h"
 
robologs::robologs(){} //Constructor: no tiene que hacer nada en especial
 
void robologs::blinking(int pin, int time)
{
    //Encenderlo y apagarlo
    digitalWrite(pin,LOW);
    delay(time);
    digitalWrite(pin, HIGH);
    delay(time);
}

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

Sensor::Sensor(char id[], char observationProperty[])
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

WaterLevelSensor::WaterLevelSensor(char id[], int analPort) : Sensor(id, (char*)"dbl")
{
  // Capturamos el valor del PIN
  this->_valueSensor = analogRead(analPort);
  Serial.print("Value Sensor: ");Serial.println(this->_valueSensor);
}

SensorMeasurement WaterLevelSensor::monitor()
{
  //Operación matematica con el valor del sensor
  return SensorMeasurement(Sensor::getID(), Sensor::getObservationProperty(), this->_valueSensor);
}
