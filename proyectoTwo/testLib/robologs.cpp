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

ForceSensitiveResistorSensor::ForceSensitiveResistorSensor(char id[], int analPort) : Sensor(id, (char*)"dbl")
{
  // Capturamos el valor del PIN
  this->_valueSensor = analogRead(analPort);
  Serial.print("Value Sensor: ");Serial.println(this->_valueSensor);
}

SensorMeasurement ForceSensitiveResistorSensor::monitor()
{
  // Link de ayuda: https://learn.adafruit.com/force-sensitive-resistor-fsr?view=all
  // analog voltage reading ranges from about 0 to 1023 which maps to 0V to 5V (= 5000mV)
  double _peso = 0.0;
  Serial.print("this->_valueSensor = ");
  Serial.println(this->_valueSensor); 
  int _fsrVoltage = map(this->_valueSensor, 0, 1024, 0, 5000);
  int _fsrResistance = 5000 - _fsrVoltage; // fsrVoltage is in millivolts so 5V = 5000mV
  Serial.print("Voltage reading in mV = ");
  Serial.println(_fsrVoltage); 
  if( _fsrVoltage != 0 && _fsrResistance != 0 )
  {
    // The voltage = Vcc * R / (R + FSR) where R = 10K and Vcc = 5V
    // so FSR = ((Vcc - V) * R) / V yay math!
    _fsrResistance *= 10000; // 10K resistor // 9.89
    _fsrResistance /= _fsrVoltage;

    int _fsrConductance = 1000000; // we measure in micromhos so 
    _fsrConductance /= _fsrResistance;
    Serial.print("Conductance in microMhos: ");
    Serial.println(_fsrConductance);

    if (_fsrConductance <= 1000) 
    {
      int _fsrForce = _fsrConductance / 80; // Newtons
      Serial.print("Force in Newtons: ");
      Serial.println(_fsrForce);  
      _peso = (_fsrForce/9.8)*1000; // gramos
    }else {
      int _fsrForce = _fsrConductance - 1000;
      _fsrForce /= 30;
      //Serial.print("Force in Newtons: ");
      //Serial.println(fsrForce); 
      Serial.print("Force in Newtons: ");
      Serial.println(_fsrForce);
      _peso = (_fsrForce/9.8)*1000; // gramos
    }
  }
  Serial.print("Peso: ");Serial.println(_peso);
  
  return SensorMeasurement(Sensor::getID(), Sensor::getObservationProperty(), _peso);
}
