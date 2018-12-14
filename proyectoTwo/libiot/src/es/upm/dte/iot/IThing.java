package es.upm.dte.iot;

public interface IThing extends IObservable, IPublishable, IOperable {

	/**
	 * 
	 * @param sensor
	 */
	void attach(ISensor sensor);

	/**
	 * 
	 * @param tag
	 */
	void attach(ITag tag);

	/**
	 * 
	 * @param actuator
	 */
	void attach(IActuator actuator);

}