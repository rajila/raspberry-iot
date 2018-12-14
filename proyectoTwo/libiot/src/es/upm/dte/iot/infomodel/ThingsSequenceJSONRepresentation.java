package es.upm.dte.iot.infomodel;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;

public class ThingsSequenceJSONRepresentation<T> implements IRepresentationGenerator<T> {
	
	private Map<ActuationType,IActuationDescriptionParser> mapa = new HashMap<ActuationType, IActuationDescriptionParser>();

	private Actuation parseActuation( JsonObject object ) throws IOException{
		Actuation res = null;
		ActuationType actuationType = null;
		String id = null;
		Object dsc = null;
		JsonObject dscRaw = null;
		
		Set<Map.Entry<String,JsonElement>> objectMembers = object.entrySet();
		for ( Map.Entry<String, JsonElement> member : objectMembers) {
			String key = member.getKey();
			if ( key.equalsIgnoreCase("type")) {
				String type = member.getValue().getAsString();
				if ( type.equalsIgnoreCase("display")) {
					actuationType = ActuationType.display;
				}
				if ( type.equalsIgnoreCase("soundGen")) {
					actuationType = ActuationType.soundGen;
				}
				if ( type.equalsIgnoreCase("lightON")) {
					actuationType = ActuationType.lightON;
				}
				if ( type.equalsIgnoreCase("lightOFF")) {
					actuationType = ActuationType.lightOFF;
				}
				if ( type.equalsIgnoreCase("openCircuit")) {
					actuationType = ActuationType.openCircuit;
				}
				if ( type.equalsIgnoreCase("closeCircuit")) {
					actuationType = ActuationType.closeCircuit;
				}
				if ( type.equalsIgnoreCase("rotate")) {
					actuationType = ActuationType.rotate;
				}				
			}
			
			if ( key.equalsIgnoreCase("id")) {
				id = member.getValue().getAsString();
			}
			
			if ( key.equalsIgnoreCase("dsc")) {
				dscRaw = member.getValue().getAsJsonObject();
			}
		}
		

		if (actuationType == null || id == null || dscRaw == null) {
			throw new IllegalStateException("Missing a member when parsing an actuation.");
		}
			
		IActuationDescriptionParser parser = mapa.get(actuationType);
		if ( parser == null )
			throw new IllegalStateException("No parser for "+actuationType+".");
		dsc = parser.parseActuationDescription(dscRaw);
		res = new Actuation(actuationType, dsc, id);
		return res;
	}
	
	private Actuation [] parseActuationArray( JsonArray array ) throws IOException{
		List<Actuation> lista = new ArrayList<Actuation>();
		Iterator<JsonElement> it = array.iterator();
		
		while ( it.hasNext()) {
			JsonElement aux = it.next();
			lista.add(parseActuation(aux.getAsJsonObject()));
		}
		return lista.toArray(new Actuation[0]);
	}
	
	private ThingActuation parseThingActuation( JsonObject object ) throws IOException{
		ThingActuation res = null;
		String idThing = null;
		Actuation [] acts = null;
		
		Set<Map.Entry<String, JsonElement>> actuationMembers = object.entrySet();
		for ( Map.Entry<String,JsonElement> member : actuationMembers) {
			String name = member.getKey();
			if ( name.equalsIgnoreCase("idThing")) {
				idThing = member.getValue().getAsString();
			}
			if ( name.equalsIgnoreCase("act")) {
				acts = parseActuationArray(member.getValue().getAsJsonArray());
			}			
		}
		
		if ( idThing == null || acts == null )
			throw new IllegalStateException("\"idThing\" attribute or \"act\" attribute is missing.");
		res = new ThingActuation(idThing, acts);
		return res;
	}
	
	@Override
	public String genRepresentation(Object thingRepresentation) {
		return new Gson().toJson(thingRepresentation);
	}

	@Override
	public synchronized T parseRepresentation(Class<T> template, String representation) {
		
		T res = null;
		
		Field []attributes = template.getDeclaredFields();
		List<String> lista = new ArrayList<String>();
		
		for ( Field e : attributes) {
			if ( e.getType().getName().equalsIgnoreCase(ThingActuation.class.getTypeName())) {
				lista.add(e.getName());
			}
		}
		
		if ( lista.size() == 0 )
			return null;
		
		for ( String attr : lista ) {
			try {
				String pattern = "set"+attr.substring(0, 1).toUpperCase()+attr.substring(1);
				template.getMethod(pattern, ThingActuation.class);
			}catch(NoSuchMethodException e) {
				throw new IllegalArgumentException("Class "+template.getName()+" has no appropiate setters. Missing setter for \""+attr+"\".");
			}
		}
		
		try {
			Constructor<T> constructor = template.getConstructor();
			res = constructor.newInstance();
		} catch (NoSuchMethodException e2) {
			throw new IllegalArgumentException("Class "+template.getName()+" has not a constructor without parameters.");
		} catch (SecurityException e2) {
			throw new IllegalArgumentException("Can't access constructor for "+template.getName()+".");
		} catch (Exception e1) {
			throw new IllegalArgumentException("Problems when invoking constructor for "+template.getName()+" : "+e1.getMessage());
		}

		JsonStreamParser reader = new JsonStreamParser(representation);
		try {
			if ( !reader.hasNext() )
				throw new IllegalStateException("No JSON element detected when parsing a thing representation.");
			JsonElement JSONElement = reader.next();
			if ( !JSONElement.isJsonObject())
				throw new IllegalStateException("An object expected when parsing a thing representation.");
			Set<Map.Entry<String,JsonElement>> thingMembers = ((JsonObject)JSONElement).entrySet();
			if ( thingMembers.size() != lista.size())
				throw new IllegalStateException("Number of members in received JSON object is different from atributes in class "+res.getClass().getName());
			for (Map.Entry<String, JsonElement> elem : thingMembers ) {
				String name = elem.getKey();
				if ( !lista.contains(name))
					throw new IllegalStateException("Attribute "+name+" not present in class "+res.getClass().getName());
				String pattern = "set"+name.substring(0, 1).toUpperCase()+name.substring(1);
				ThingActuation aux = parseThingActuation(elem.getValue().getAsJsonObject());
				template.getMethod(pattern, ThingActuation.class).invoke(res, aux);
			}
		} catch (Exception e1) {
			throw new IllegalStateException("Problems when setting thing actuation: "+e1.getMessage());
		} 
		return res;
	}

	@Override
	public void registerActuationParser(ActuationType actuationType, IActuationDescriptionParser parser) {
		mapa.put(actuationType, parser);
	}

}