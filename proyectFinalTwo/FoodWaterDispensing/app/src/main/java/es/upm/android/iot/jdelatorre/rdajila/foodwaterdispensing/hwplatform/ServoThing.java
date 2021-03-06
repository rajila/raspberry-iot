package es.upm.android.iot.jdelatorre.rdajila.foodwaterdispensing.hwplatform;

import android.util.Log;

import es.upm.android.iot.jdelatorre.rdajila.foodwaterdispensing.util.Constant;
import es.upm.dte.iot.*;

import com.google.android.things.contrib.driver.pwmservo.Servo;

import java.io.IOException;


public class ServoThing extends Actuator
{
    private static final String TAG = ServoThing.class.getSimpleName();

    private Servo _servo;

    private synchronized void servoUpdateAngleRotation (int angle, int seconds)
    {
        final int _seconds = seconds;
        final int _angle = angle;

        if (_servo == null) return;

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            _servo.setAngle(_angle);
                            Thread.sleep(_seconds*1000);
                            _servo.setAngle(Constant.ANGLE_MIN);
                        } catch (IOException e) {
                            Log.e(TAG, "Error setting Servo angle");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();
    }

    public ServoThing(String id, String pin)
    {
        super(id,ActuatorType.miniServo);

        try
        {
            _servo = new Servo(pin);
            _servo.setAngleRange(Constant.ANGLE_MIN, Constant.ANGLE_MAX);
            _servo.setEnabled(true);
            _servo.setAngle(Constant.ANGLE_MIN);
        } catch (IOException e) {
            Log.e(TAG, "Error creating Servo", e);
            return; // don't init handler
        }
    }

    public Servo get_servo() {
        return _servo;
    }

    public void set_servo(Servo _servo) {
        this._servo = _servo;
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