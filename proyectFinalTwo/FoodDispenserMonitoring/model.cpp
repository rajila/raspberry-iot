#include "model.h"

char* SensorMeasurement::getID()
{
  return this->_id;
}

char* SensorMeasurement::getObserverProperty() 
{
  return this->_obProp;
}

double SensorMeasurement::getSensorOutput() 
{
  return this->_out;
}

void SensorMeasurement::setID(char value[]) 
{
  strcpy(this->_id,value);
}

void SensorMeasurement::setObserverProperty(char value[]) 
{
  if (!(strcmp(value, "int") == 0) && !(strcmp(value,"dbl") == 0) ) return;
  strcpy(this->_obProp, value);
}

void SensorMeasurement::setSensorOutput(double value)
{
  if (strcmp(this->_obProp, "int") == 0)
    this->_out = (int)value;
  else
    this->_out = value;
}

SensorMeasurement::SensorMeasurement(char id[], char observationProperty[], double sensorOutput)
{
  if (!(strcmp(observationProperty, "int") == 0) && !(strcmp(observationProperty, "dbl") == 0)) return;
  strcpy(this->_id, id);
  strcpy(this->_obProp, observationProperty);
  if (strcmp(this->_obProp, "int") == 0)
    this->_out = (int)sensorOutput;
  else
    this->_out = sensorOutput;
}

/**
 *
 *
 */
void Sensor::init(char id[], char observationProperty[])
{
  strcpy(this->_id, id);
  strcpy(this->_observationProperty, observationProperty);
}

char* Sensor::getID()
{
  return this->_id;
}

char* Sensor::getObservationProperty()
{
  return this->_observationProperty;
}

void Sensor::setID(char value[])
{
  strcpy(this->_id, value);
}

void Sensor::setObservationProperty(char value[])
{
  strcpy(this->_observationProperty, value);
}

/**
 * 
 * 
 */
void WaterLevelSensor::init(char id[], char observationProperty[], int analogicPinTRIG, int analogicPinECHO)
{
  Sensor::init(id, observationProperty);
  this->_analogicPinTRIG = analogicPinTRIG;
  this->_analogicPinECHO = analogicPinECHO;
  this->_ultraSonic.init(analogicPinTRIG,analogicPinECHO,_MAX_DISTANCE);
}

double WaterLevelSensor::getDataSensor()
{
  return this->_ultraSonic.ping_cm(); // distancia en cm
}

SensorMeasurement WaterLevelSensor::monitor()
{
  return SensorMeasurement(Sensor::getID(), Sensor::getObservationProperty(), getDataSensor()*50); // centrimetros cubicos: 1 cm --> 50ml
}

/**
 * 
 */
void DigitalBalanceSensor::init(char id[], char observationProperty[], int analogicPinDOUT, int analogicPinSCK)
{
  Sensor::init(id, observationProperty);
  this->_analogicPinDOUT = analogicPinDOUT;
  this->_analogicPinSCK = analogicPinSCK;
  this->_digitalBalance.begin(analogicPinDOUT,analogicPinSCK);
  this->_digitalBalance.set_scale();
  this->_digitalBalance.tare();
  this->_digitalBalance.set_scale(_CALIBRATIONFACTOR);
}

double DigitalBalanceSensor::getDataSensor()
{
  return this->_digitalBalance.get_units(); // Kg
}

HX711 DigitalBalanceSensor::getDigitalBalance()
{
  return this->_digitalBalance;
}

SensorMeasurement DigitalBalanceSensor::monitor()
{
  return SensorMeasurement(Sensor::getID(), Sensor::getObservationProperty(), getDataSensor() * 1000); //gramos
}

/**
 * 
 */
void Thing::init(char idThing[])
{
  strcpy(this->_idThing, idThing);
  this->_timeClient.init(this->_ntpUDP);
  this->_timeClient.begin();
  this->_timeClient.setTimeOffset(3600);
}

void Thing::attach(ISensor &sensor)
{
  this->_sensors.Add(&sensor);
}

char* Thing::getIdThing()
{
  return this->_idThing;
}

char* Thing::getStringJSON()
{
  StaticJsonDocument<250> _doc;
  JsonObject _root = _doc.to<JsonObject>();

  JsonObject _dispensator = _root.createNestedObject("dispenser");
  _dispensator["idThing"] = this->_idThing;
  
  JsonObject _obs = _dispensator.createNestedObject("obs");
  while(!this->_timeClient.update()){this->_timeClient.forceUpdate();}
  _obs["time"] = this->_timeClient.getFormattedDate();

  JsonArray _sensor = _obs.createNestedArray("sensor");
  for ( int i = 0; i < this->_sensors.Count(); i++ )
  {
    SensorMeasurement _sMeasurement = this->_sensors[i]->monitor();
    JsonObject _measurement = _sensor.createNestedObject();
    _measurement["id"] = _sMeasurement.getID();
    _measurement["obProp"] = _sMeasurement.getObserverProperty();
    _measurement["out"] = _sMeasurement.getSensorOutput();
  }
  
  serializeJson(_root, this->_stringJSON);
  
  return this->_stringJSON;
}

void Thing::sendDataThingSpeak()
{
  //int _status = ThingSpeak.writeField(_SECRET_CH_ID, 1, getStringJSON(), _SECRET_WRITE_APIKEY);
  //if( _status == 200 ) Serial.println("Channel update successful.");
  //else Serial.println("Problem updating channel. HTTP error code " + String(_status));
  Serial.println(getStringJSON());
}
