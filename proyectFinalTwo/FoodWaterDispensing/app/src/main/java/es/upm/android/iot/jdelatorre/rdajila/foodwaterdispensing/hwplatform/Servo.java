package es.upm.android.iot.jdelatorre.rdajila.foodwaterdispensing.hwplatform;

import android.util.Log;

import es.upm.dte.iot.*;

public class Servo extends Actuator
{
    private static final String TAG = Servo.class.getSimpleName();

    private synchronized void lcdUpdate (int message1, int message2)
    {
        Log.i(TAG + getId() + ": message1",String.valueOf(message1));
        Log.i(TAG + getId() + ": message2",String.valueOf(message2));
        //lcd.clear();
        // display a message on the first row of the LCD
        //lcd.setCursor(0, 0);
        //lcd.write(message1);
        //lcd.setCursor(1, 0);
        //lcd.write(message2);

        // apply the calculated result
        //lcd.setColor(RED_BOLD, GREEN_BOLD, BLUE_BOLD);

        //try {
        //    Thread.sleep(3000);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}

        // apply the default values (red = 0, green = 0, blue = 255)
        //lcd.setColor(RED_BY_DEFAULT, GREEN_BY_DEFAULT, BLUE_BY_DEFAULT);
    }

    public Servo(String id)
    {
        super(id,ActuatorType.miniServo);

        // LCD connected to the default I2C bus
        //lcd = new Jhd1313m1(0);

        // display an initial message on the LCD
        //lcd.setCursor(0, 0);
        //lcd.write("ID: "+id);

        // apply the default values (red = 0, green = 0, blue = 255)
        //lcd.setColor(RED_BY_DEFAULT, GREEN_BY_DEFAULT, BLUE_BY_DEFAULT);
    }

    /**
     *
     * @param action
     */
    public void act(Object action)
    {
        if ( action instanceof ServoActionDescription ) {
            ServoActionDescription aux = (ServoActionDescription)action;
            lcdUpdate(aux.getAng(), aux.getSec());
        }
        else
            throw new IllegalArgumentException("Action is not an instance of "+ServoActionDescription.class.getName()+".");
    }
}
