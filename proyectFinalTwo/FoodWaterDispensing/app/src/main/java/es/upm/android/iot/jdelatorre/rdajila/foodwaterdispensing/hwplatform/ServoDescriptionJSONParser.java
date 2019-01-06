package es.upm.android.iot.jdelatorre.rdajila.foodwaterdispensing.hwplatform;

import java.util.Map;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import es.upm.dte.iot.infomodel.*;

public class ServoDescriptionJSONParser implements IActuationDescriptionParser
{
    @Override
    public Object parseActuationDescription(Object toBeParsed)
    {
        ServoActionDescription desc = null;
        if ( !(toBeParsed instanceof JsonObject))
            throw new IllegalArgumentException("Servo Description is not JSON.");
        JsonObject object = (JsonObject) toBeParsed;
        Set<Map.Entry<String,JsonElement>> objectMembers = object.entrySet();
        if ( objectMembers.size() != 2)
            throw new IllegalStateException("Servo description has not only 2 members.");
        int ang = -1, sec = -1;
        for( Map.Entry<String, JsonElement> member : objectMembers ) {
            String key = member.getKey();
            if ( key.equalsIgnoreCase("ang")) {
                ang = member.getValue().getAsInt();
            }
            if ( key.equalsIgnoreCase("sec")) {
                sec = member.getValue().getAsInt();
            }
        }

        if ( ang == -1 || sec == -1 )
            throw new IllegalStateException("Miissing ang or sec in actuation description.");
        desc = new ServoActionDescription(ang, sec);
        return desc;
    }
}
