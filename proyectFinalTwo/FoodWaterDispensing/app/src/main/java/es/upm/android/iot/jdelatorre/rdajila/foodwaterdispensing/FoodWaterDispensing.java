package es.upm.android.iot.jdelatorre.rdajila.foodwaterdispensing;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.io.IOException;
import java.util.Properties;

import es.upm.android.iot.jdelatorre.rdajila.foodwaterdispensing.dispenser.ServoResource;
import es.upm.android.iot.jdelatorre.rdajila.foodwaterdispensing.hwplatform.LedThing;
import es.upm.android.iot.jdelatorre.rdajila.foodwaterdispensing.hwplatform.ServoThing;
import es.upm.android.iot.jdelatorre.rdajila.foodwaterdispensing.util.Constant;
import es.upm.dte.iot.Tag;
import es.upm.dte.iot.platform.IIoTPlatform;
import es.upm.dte.iot.platform.IoTPlatformFactory;

public class FoodWaterDispensing extends Activity
{
    private static final String TAG = FoodWaterDispensing.class.getSimpleName();

    private IIoTPlatform platform;
    private ServoResource servoR;

    private Properties properties = new Properties();

    private HandlerThread mHandlerThread;
    private Handler mHandler;

    ServoThing servoFood;
    ServoThing servoWater;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        properties.setProperty("TALKBACK_ID", Constant.TALKBACK_ID);
        properties.setProperty("TALKBACK_KEY", Constant.TALKBACK_KEY);

        platform = IoTPlatformFactory.getInstance("ThingSpeak", properties);
        servoR = new ServoResource();
        servoFood = new ServoThing(Constant.SERVO_DIGITAL_BALANCE_ID, Constant.PIN_SERVO_DIGITALBALANCE);
        servoWater = new ServoThing(Constant.SERVO_WATER_ID, Constant.PIN_SERVO_WATER);

        servoR.attach(servoFood);
        servoR.attach(servoWater);
        servoR.attach(new Tag(Constant.THING_ID));

        mHandlerThread = new HandlerThread("pwm-execute-action");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
        mHandler.post(executeAction);
    }

    private Runnable executeAction = new Runnable()
    {
        @Override
        public void run()
        {
            try
            {
                String action = platform.getNextAction();
                //String action = "{\"dispenser\":{\"idThing\":\"179\",\"act\":[{\"id\":\"001\",\"type\":\"rotate\",\"dsc\":{\"ang\":75,\"sec\":5}},{\"id\":\"002\",\"type\":\"rotate\",\"dsc\":{\"ang\":115,\"sec\":5}}]}}";
                if ( action == null ) // action
                {
                    Log.i(TAG,"Sin Datos");
                }else{
                    Log.i(TAG,action);
                    servoR.setFromRepresentation(action);
                }
                mHandler.postDelayed(this, Constant.DELAY_REQUEST); // Se vuelve e ajecutar el Run, en un tiempo: DELAY_REQUEST
            } catch (Exception e) {
                Log.e(TAG, "Error setting Servo angle", e);
            }
        }
    };

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(executeAction);
            mHandlerThread.quitSafely();
        }

        if (servoFood.get_servo() != null) {
            try {
                servoFood.get_servo().close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing Servo");
            } finally {
                servoFood.set_servo(null);
            }
        }

        if (servoWater.get_servo() != null) {
            try {
                servoWater.get_servo().close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing Servo");
            } finally {
                servoWater.set_servo(null);
            }
        }
    }
}