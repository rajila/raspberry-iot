package es.upm.dte.iot.infomodel;

public class Actuation {

	public Actuation(ActuationType type, Object dsc, String id) {
		super();
		this.type = type;
		this.dsc = dsc;
		this.id = id;
	}

	private ActuationType type;
	private Object dsc;
	private String id;

	public ActuationType getType() {
		return this.type;
	}

	/**
	 * 
	 * @param type
	 */
	public void setType(ActuationType type) {
		this.type = type;
	}

	public Object getDsc() {
		return this.dsc;
	}

	/**
	 * 
	 * @param dsc
	 */
	public void setDsc(Object dsc) {
		this.dsc = dsc;
	}

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

}