package es.upm.dte.iot;

public abstract class Actuator implements IActuator {

	private String id;
	private ActuatorType type;

	public String getId() {
		return this.id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	public ActuatorType getType() {
		return this.type;
	}

	/**
	 * 
	 * @param type
	 */
	public void setType(ActuatorType type) {
		this.type = type;
	}

	public Actuator(String id, ActuatorType type) {
		super();
		this.id = id;
		this.type = type;
	}

}