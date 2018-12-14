package es.upm.dte.iot.infomodel;

public class ThingObservation {

	private String idThing;
	private Observation obs;

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

	public Observation getObs() {
		return this.obs;
	}

	/**
	 * 
	 * @param obs
	 */
	public void setObs(Observation obs) {
		this.obs = obs;
	}

	/**
	 * 
	 * @param idThing
	 * @param observation
	 */
	public ThingObservation(String idThing, Observation observation) {
		this.idThing = idThing;
		this.obs = observation;
	}

	/**
	 * 
	 * @param idThing
	 */
	public ThingObservation(String idThing) {
		this.idThing = idThing;
		obs = null;
	}

}