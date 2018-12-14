package es.upm.dte.iot.platform;

import java.util.Properties;

public class IoTPlatformFactory {

	/**
	 * 
	 * @param type
	 * @param properties
	 */
	public static IIoTPlatform getInstance(String type, Properties properties) {
		if ( type.equalsIgnoreCase("ThingSpeak"))
			return new ThingSpeak(properties);
		return null;
	}

}