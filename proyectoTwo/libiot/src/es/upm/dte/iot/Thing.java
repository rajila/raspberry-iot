package es.upm.dte.iot;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import es.upm.dte.iot.infomodel.Observation;
import es.upm.dte.iot.infomodel.SensorMeasurement;

public abstract class Thing implements IThing {

	private Set<ISensor> sensors;
	private Set<ITag> tags;
	private String situation;
	private Set<IActuator> actuators;

	public Set<IActuator> getActuators() {
		return this.actuators;
	}

	/**
	 * 
	 * @param actuators
	 */
	public void setActuators(Set<IActuator> actuators) {
		this.actuators = actuators;
	}

	public Set<ISensor> getSensors() {
		return sensors;
	}

	/**
	 * 
	 * @param sensors
	 */
	public void setSensors(Set<ISensor> sensors) {
		this.sensors = sensors;
	}

	public Set<ITag> getTags() {
		return tags;
	}

	/**
	 * 
	 * @param tags
	 */
	public void setTags(Set<ITag> tags) {
		this.tags = tags;
	}

	public Thing(String situation) {
		sensors = new HashSet<ISensor>();
		tags = new HashSet<ITag>();
		actuators = new HashSet<IActuator>();
		this.situation = situation;
	}

	public String getSituation() {
		return this.situation;
	}

	/**
	 * 
	 * @param situation
	 */
	public void setSituation(String situation) {
		this.situation = situation;
	}

	@Override
	public void attach(ISensor sensor) {
		if ( sensor == null )
			throw new IllegalArgumentException("Sensor to be attached is null.");
		sensors.add(sensor);
	}
	
	@Override
	public void attach(ITag tag) {
		if ( tag == null )
			throw new IllegalArgumentException("Tag to be attached is null.");
		tags.add(tag);
	}
	
	@Override
	public void attach(IActuator actuator) {
		if ( actuator == null )
			throw new IllegalArgumentException("Actuator to be attached is null.");
		actuators.add(actuator);
	}
	
	@Override
	public Observation observe() {
		SensorMeasurement []measures = new SensorMeasurement[sensors.size()];
		int i = 0;
		
		for (ISensor sensor : sensors) {
			measures[i++] = sensor.monitor();
		}
		
		return new Observation(OffsetDateTime.now().toString(), situation, measures); 
	}
	
}