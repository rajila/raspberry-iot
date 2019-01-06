package es.upm.android.iot.jdelatorre.rdajila.foodwaterdispensing;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.util.Properties;

import es.upm.android.iot.jdelatorre.rdajila.foodwaterdispensing.dispenser.ServoResource;
import es.upm.android.iot.jdelatorre.rdajila.foodwaterdispensing.hwplatform.Servo;
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

    Servo servoFood;
    Servo servoWater;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        properties.setProperty("TALKBACK_ID", "30174");
        properties.setProperty("TALKBACK_KEY", "82VRSK83J7RYH5OF");

        platform = IoTPlatformFactory.getInstance("ThingSpeak", properties);
        servoR = new ServoResource();
        servoFood = new Servo("001");
        servoWater = new Servo("002");

        servoR.attach(servoFood);
        servoR.attach(servoWater);
        servoR.attach(new Tag("179"));

        mHandlerThread = new HandlerThread("pwm-playback");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
        mHandler.post(mPlaybackRunnable);
    }

    private Runnable mPlaybackRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            try
            {
                String action = platform.getNextAction();
                if ( action == null ) // action
                {
                    //led.on();
                    Log.i(TAG,"Sin Data THINSPEAK");
                }else{
                    Log.i(TAG,action);
                    //led.off();
                    servoR.setFromRepresentation(action);
                }
                mHandler.postDelayed(this, 25000); // Se vuelve e ajecutar el Run, en un tiempo: PLAYBACK_NOTE_DELAY
            } catch (Exception e) {
                Log.e(TAG, "RADC03 Error playing speaker", e);
            }
        }
    };

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(mPlaybackRunnable);
            mHandlerThread.quitSafely();
        }
    }
}