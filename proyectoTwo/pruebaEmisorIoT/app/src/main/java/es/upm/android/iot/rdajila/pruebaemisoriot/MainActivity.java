package es.upm.android.iot.rdajila.pruebaemisoriot;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.io.IOException;
import java.util.Properties;

import es.upm.dte.iot.platform.IIoTPlatform;
import es.upm.dte.iot.platform.IoTPlatformFactory;
import es.upm.dte.iot.platform.ThingSpeak;


public class MainActivity extends Activity
{
    private static final String TAG = MainActivity.class.getSimpleName();

    private IIoTPlatform plataforma;
    private Properties properties = new Properties();
    private String _dataJSON = "";

    private HandlerThread mHandlerThread;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        properties.setProperty("WRITER_KEY", "MFBNHV6LVFEJMKMQ");

        _dataJSON = "{" +
                        "\"planta\":" +
                            "{" +
                                "\"idThing\":\"743\"," +
                                "\"obs\":{" +
                                            "\"time\":\"2018-12-12T18:46:25.695Z\"," +
                                            "\"sit\":\"66.666666 -6.666666\"," +
                                            "\"sensor\":[" +
                                                            "{" +
                                                                "\"id\":\"hum\"," +
                                                                "\"obProp\":\"dbl\"," +
                                                                "\"out\":5.0" +
                                                            "}," +
                                                            "{" +
                                                                "\"id\":\"temp\"," +
                                                                "\"obProp\":\"int\"," +
                                                                "\"out\":25.0" +
                                                            "}," +
                                                            "{" +
                                                                "\"id\":\"lum\"," +
                                                                "\"obProp\":\"int\"," +
                                                                "\"out\":48.0" +
                                                            "}" +
                                                        "]" +
                                        "}" +
                            "}" +
                    "}";

        plataforma = IoTPlatformFactory.getInstance("ThingSpeak", properties);

        mHandlerThread = new HandlerThread("pwm-playback");
        mHandlerThread.start();
        Log.i(TAG,"RADC HILO: "+mHandlerThread.getLooper().toString());
        mHandler = new Handler(mHandlerThread.getLooper());
        mHandler.post(mPlaybackRunnable);
    }

    private Runnable mPlaybackRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            try {
                plataforma.publishThingRepresentation(_dataJSON);
                Log.i("TAG","Got it!");
                mHandler.postDelayed(this, 20000); // Se vuelve e ajecutar el Run, en un tiempo: PLAYBACK_NOTE_DELAY
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