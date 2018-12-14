package es.upm.dte.iot.infomodel;

import java.util.Arrays;

public class Observation {

	private String time;
	private String sit;
	private SensorMeasurement[] sensor;

	public Observation(String time, String situation, SensorMeasurement[] sensors) {
		super();
		this.time = time;
		this.sit = situation;
		this.sensor = sensors;
	}

	public Observation(String situation, SensorMeasurement[] sensors) {
		super();
		this.sit = situation;
		this.sensor = sensors;
	}

	public String getTime() {
		return this.time;
	}

	public void setTime(String value) {
		this.time = value;
	}

	public String getSituation() {
		return sit;
	}

	public void setSituation(String value) {
		sit = value;
	}

	public SensorMeasurement getSensor(int sensorIndex) {
		if ( sensorIndex >= 0 && sensorIndex < sensor.length )
			return sensor[sensorIndex];
		else throw new IllegalArgumentException("sensor index out of bound");
	}

	public void setSensor(int sensorIndex, SensorMeasurement newValue) {
		
		if ( sensorIndex < 0 || sensorIndex >= sensor.length )
			throw new IllegalArgumentException("sensor index out of bound");
		sensor[sensorIndex]=newValue;
	}

	@Override
	public String toString() {
		return "Observation [time=" + time + ", sit=" + sit + ", sensor=" + Arrays.toString(sensor) + "]";
	}

}