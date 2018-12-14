package es.upm.dte.iot.infomodel;

public class ThingActuation {

	private String idThing;
	private Actuation[] act;

	public String getIdThing() {
		return this.idThing;
	}

	/**
	 * 
	 * @param idThing
	 */
	public void setIdThing(String idThing) {
		this.idThing = idThing;
	}

	public Actuation[] getAct() {
		return this.act;
	}

	/**
	 * 
	 * @param act
	 */
	public void setAct(Actuation act) {

	}

	public ThingActuation(String idThing, Actuation[] act) {
		super();
		this.idThing = idThing;
		this.act = act;
	}

}