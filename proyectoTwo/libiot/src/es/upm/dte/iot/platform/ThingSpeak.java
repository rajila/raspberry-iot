package es.upm.dte.iot.platform;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

public class ThingSpeak implements IIoTPlatform {

	private final String URL_UPDATE_FIELDS = "http://api.thingspeak.com/update";
	private final String URL_TALKBACK = "http://api.thingspeak.com/talkbacks/%s/commands/execute?api_key=%s";
	private final int TAM_BUFFER = 4096;

	private String writerKey;
	private boolean isProxyEnabled;
	private String proxyAddress;
	private int proxyPort;
	private String talkBackId;
	private String talkBackKey;

	public ThingSpeak(Properties properties) {
		if (properties == null )
			throw new IllegalArgumentException("Properties is null.");
		writerKey = properties.getProperty("WRITER_KEY");
		talkBackId = properties.getProperty("TALKBACK_ID");
		talkBackKey = properties.getProperty("TALKBACK_KEY");
		isProxyEnabled = (properties.getProperty("PROXY_ENABLED")==null)?false:true;
		if (isProxyEnabled) {
			try {
				proxyAddress = properties.getProperty("PROXY_ADDRESS");
				InetAddress.getByName(proxyAddress);
				proxyPort = Integer.parseInt(properties.getProperty("PROXY_PORT"));
			}catch(Exception e) {
				throw new IllegalArgumentException("Either PROXY_ADDRESS or PROXY_PORT property is wrong.");
			}
		}
	}

	@Override
	public synchronized void publishThingRepresentation(String representation) throws IoTPlatformException {
		URL apiURL;

		HttpURLConnection con;
		Proxy proxy;

		try {
			if ( writerKey == null )
				throw new IllegalStateException("No ThingSpeak channel writer key provided.");
			apiURL = new URL(URL_UPDATE_FIELDS);
			if ( isProxyEnabled ){
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, proxyPort));
				con = (HttpURLConnection) apiURL.openConnection(proxy);
			}
			else
				con = (HttpURLConnection) apiURL.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			con.setDoOutput(true);

			String urlParameters = "api_key="+writerKey+"&field1="+URLEncoder.encode(representation,"UTF-8");
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			int result = con.getResponseCode();
			if ( result != HttpURLConnection.HTTP_OK )
				throw new IoTPlatformException("ThingspeakChannel:publishMeasurement: HTTP response is not 200: "+result);
		}catch(IOException e) {
			throw new IoTPlatformException(e.getMessage());
		}
	}

	@Override
	public synchronized String getNextAction() throws IoTPlatformException {
		
		String res = null;
		if ( talkBackId == null || talkBackKey == null )
			throw new IllegalStateException("Talback id or talkback key has not been provided.");
		
		String uri = String.format(URL_TALKBACK, talkBackId, talkBackKey);
		URL apiURL;
		Proxy proxy;
		HttpURLConnection con;
		
		try {
			apiURL = new URL(uri);
		} catch (MalformedURLException e) {
			throw new IoTPlatformException("URL "+uri+" is wrong: "+e.getMessage());
		}
		
		try {

			if ( isProxyEnabled ){
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, proxyPort));
				con = (HttpURLConnection) apiURL.openConnection(proxy);
			}
			else
				con = (HttpURLConnection) apiURL.openConnection();
			con.setRequestMethod("POST");
			if ( con.getResponseCode()!= HttpURLConnection.HTTP_OK )
				throw new IoTPlatformException("ThingSpeak.getNextAction: HTTP Response: "	+ con.getResponseCode());
			
			BufferedInputStream bis = new BufferedInputStream(con.getInputStream());

			byte[] bytes = new byte[TAM_BUFFER];
			int numBytes = bis.read(bytes);
			if ( numBytes != -1 )
				res = new String(bytes, 0, numBytes);
			

			bis.close();
			con.disconnect();

		}catch(IOException e) {
			throw new IoTPlatformException(e.getMessage());
		}
		
		return res;
	}

}