package es.upm.dte.iot;

public abstract class Sensor implements ISensor {

	private String id;
	private String observationProperty;
	
	/**
	 * 
	 * @param id
	 * @param observerProperty
	 */
	public Sensor(String id, String observerProperty) {
		this.id = id;
		this.observationProperty = observerProperty;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getObservationProperty() {
		return observationProperty;
	}

	public void setObservationProperty(String observationProperty) {
		this.observationProperty = observationProperty;
	}
}