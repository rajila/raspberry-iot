package es.upm.android.iot.jdelatorre.rdajila.foodwaterdispensing.hwplatform;

import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;

import es.upm.dte.iot.Actuator;
import es.upm.dte.iot.ActuatorType;

public class LedThing extends Actuator
{
    private static final String TAG = ServoThing.class.getSimpleName();

    private Gpio _ledPin;;
    private PeripheralManager _manager;

    private synchronized void servoUpdateAngleRotation (int angle, int seconds)
    {
        final int _seconds = seconds;
        final int _angle = angle;

        if (_ledPin == null) return;

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            _ledPin.setValue(true);
                            Thread.sleep(_seconds*1000);
                            _ledPin.setValue(false);
                        } catch (IOException e) {
                            Log.e(TAG, "Error setting Servo LED");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();


    }

    public LedThing(String id, String pin)
    {
        super(id,ActuatorType.miniServo);

        try
        {
            _manager = PeripheralManager.getInstance();
            _ledPin = _manager.openGpio(pin);
            _ledPin.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            _ledPin.setValue(false);
        } catch (IOException e) {
            Log.e(TAG, "Error creating LED", e);
            return; // don't init handler
        }
    }

    /**
     *
     * @param action
     */
    public void act(Object action)
    {
        if ( action instanceof ServoActionDescription ) {
            ServoActionDescription aux = (ServoActionDescription)action;
            servoUpdateAngleRotation(aux.getAng(), aux.getSec());
        }
        else
            throw new IllegalArgumentException("Action is not an instance of "+ServoActionDescription.class.getName()+".");
    }
}