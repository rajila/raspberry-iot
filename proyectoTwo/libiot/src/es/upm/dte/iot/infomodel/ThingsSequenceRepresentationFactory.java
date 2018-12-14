package es.upm.dte.iot.infomodel;

public class ThingsSequenceRepresentationFactory {

	/**
	 * 
	 * @param <T>
	 * @param <T>
	 * @param type
	 */
	public static <T> IRepresentationGenerator<T> getInstance(String type) {
		
		if ( type.equalsIgnoreCase("JSON"))
			return new ThingsSequenceJSONRepresentation<T>();
		return null;
	}

}