package es.upm.dte.iot.infomodel;

public interface IRepresentationGenerator<T> {

	/**
	 * 
	 * @param thingRepresentation
	 */
	String genRepresentation(java.lang.Object thingRepresentation);

	/**
	 * 
	 * @param template
	 * @param representation
	 * @param templateActuationDescription
	 */
	T parseRepresentation(Class<T> template, String representation);

	/**
	 * 
	 * @param actuationType
	 * @param parser
	 */
	void registerActuationParser(ActuationType actuationType, IActuationDescriptionParser parser);

}