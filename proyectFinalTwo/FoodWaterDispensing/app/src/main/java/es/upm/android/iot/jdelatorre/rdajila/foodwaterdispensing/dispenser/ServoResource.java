package es.upm.android.iot.jdelatorre.rdajila.foodwaterdispensing.dispenser;

import android.util.Log;

import java.util.Iterator;
import java.util.Set;

import es.upm.android.iot.jdelatorre.rdajila.foodwaterdispensing.hwplatform.ServoDescriptionJSONParser;
import es.upm.dte.iot.*;
import es.upm.dte.iot.infomodel.*;

public class ServoResource extends Thing
{
    private static final String TAG = ServoResource.class.getSimpleName();

    private ServoRepresentation servoRepresentation;
    private IRepresentationGenerator<ServoRepresentation> actionParser;

    public ServoResource() {
        super("N/A");
        actionParser = ThingsSequenceRepresentationFactory.getInstance("JSON");
        actionParser.registerActuationParser(ActuationType.rotate, new ServoDescriptionJSONParser());
    }

    /**
     *
     * @param representation
     */
    public void setFromRepresentation(String representation) {
        if ( representation == null )
            return;
        Set<ITag> tags = getTags();
        servoRepresentation = actionParser.parseRepresentation(ServoRepresentation.class, representation);
        boolean encontrado = false;
        Iterator<ITag> iterator = tags.iterator();
        while ( iterator.hasNext() && !encontrado ) {
            ITag tag = iterator.next();
            if ( tag.getIdentification().equalsIgnoreCase(servoRepresentation.getDispenser().getIdThing()))
                encontrado = true;
        }
        if ( !encontrado )
            throw new IllegalStateException("Action targeting another thing has been received.");

        operate();
    }

    @Override
    public String getRepresentation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operate() {
        Actuation []actuations = servoRepresentation.getDispenser().getAct();
        Object[] actThing = getActuators().toArray(); // Listado de Interfaces Actuadores
        for ( Actuation action : actuations ) // ThingSpeak
        {
            for(int i=0; i<actThing.length;i++)
            {
                Actuator _tmpA = ((Actuator)actThing[i]);
                if( action.getId().compareToIgnoreCase(_tmpA.getId()) == 0 )
                {
                    ((IActuator)actThing[i]).act(action.getDsc());
                    break;
                }
            }
        }
    }

    @Override
    public void attach(ISensor sensor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void attach(IActuator actuator) {
        if ( getActuators().size() >= 2 )
            throw new IllegalArgumentException("The screen already has been attached two actuators.");
        if ( actuator instanceof Actuator ) {
            Actuator act= (Actuator)actuator;
            if ( act.getType() != ActuatorType.miniServo )
                throw new IllegalArgumentException("Actuator type is incorrect.");
        }
        super.attach(actuator);
    }
}