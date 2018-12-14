package es.upm.dte.iot;

public interface IPublishable {

	String getRepresentation();

	/**
	 * 
	 * @param representation
	 */
	void setFromRepresentation(String representation);

}