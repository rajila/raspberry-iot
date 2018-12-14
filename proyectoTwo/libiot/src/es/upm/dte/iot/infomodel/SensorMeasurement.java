package es.upm.dte.iot.infomodel;

public class SensorMeasurement {

	private String id;
	private String obProp;
	private double out;

	public String getID() {
		return id;
	}

	public String getObserverProperty() {
		return obProp;
	}

	public double getSensorOutput() {
		return out;
	}

	/**
	 * 
	 * @param value
	 */
	public void setID(String value) {
		id = value;
	}

	/**
	 * 
	 * @param value
	 */
	public void setObserverProperty(String value) {
		if ( !value.equalsIgnoreCase("int") && ! value.equalsIgnoreCase("dbl"))
			throw new IllegalArgumentException("Observer Property must be 'int' or 'dbl'.");
		this.obProp = value;
	}

	/**
	 * 
	 * @param value
	 */
	public void setSensorOutput(double value) {
		if (obProp.equalsIgnoreCase("int"))
			out = (int) value;
		else
			out = value;
	}

	/**
	 * 
	 * @param id
	 * @param observationProperty
	 * @param sensorOutput
	 */
	public SensorMeasurement(String id, String observationProperty, double sensorOutput) {
		if ( !observationProperty.equalsIgnoreCase("int") && ! observationProperty.equalsIgnoreCase("dbl"))
			throw new IllegalArgumentException("Observer Property must be 'int' or 'dbl'.");
		this.id = id;
		this.obProp = observationProperty;
		if (obProp.equalsIgnoreCase("int"))
			this.out = (int) sensorOutput;
		else
			this.out = sensorOutput;
	}

	@Override
	public String toString() {
		return "SensorMeasurement [id=" + id + ", obProp=" + obProp + ", out=" + out + "]";
	}

}