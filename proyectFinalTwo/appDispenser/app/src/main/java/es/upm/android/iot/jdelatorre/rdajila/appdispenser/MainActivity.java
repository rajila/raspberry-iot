package es.upm.android.iot.jdelatorre.rdajila.appdispenser;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.larswerkman.lobsterpicker.OnColorListener;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

import es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity.Configuration;
import es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity.Dispenser;
import es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity.LogDispenser;
import es.upm.android.iot.jdelatorre.rdajila.appdispenser.util.Constant;
import es.upm.android.iot.jdelatorre.rdajila.appdispenser.util.DispenserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();

    private CircularProgressBar _cirFoodDaily;
    private CircularProgressBar _cirFoodPortion;
    private CircularProgressBar _cirWaterCurrent;
    private TextView _valFoodDaily;
    private TextView _valFoodPortion;
    private TextView _valWaterCurrent;

    private TextView _lblDate;

    private HandlerThread _mHandlerThread;
    private Handler _mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _lblDate = findViewById(R.id._lblDate);
        _valFoodDaily = findViewById(R.id._valueFoodDaily);
        _valFoodPortion = findViewById(R.id._valueFoodPortion);
        _valWaterCurrent = findViewById(R.id._valueWaterCurrent);

        _cirFoodDaily = findViewById(R.id._cirFoodDaily);
        _cirFoodDaily.setProgressWithAnimation(Constant._MIN_PROGRESS);
        _cirFoodDaily.setBackgroundProgressBarWidth(Constant._BPROGRESS_BAR_WIDTH * getResources().getDisplayMetrics().density);
        _cirFoodDaily.setProgressBarWidth(Constant._PROGRESS_BAR_WIDTH * getResources().getDisplayMetrics().density);
        _cirFoodDaily.setColor(Constant._COLOR_RED);
        _cirFoodDaily.setBackgroundColor(adjustAlpha(Constant._COLOR_RED, 0.3f));

        _cirFoodPortion = findViewById(R.id._cirFoodPortion);
        _cirFoodPortion.setProgressWithAnimation(Constant._MIN_PROGRESS);
        _cirFoodPortion.setBackgroundProgressBarWidth(Constant._BPROGRESS_BAR_WIDTH * getResources().getDisplayMetrics().density);
        _cirFoodPortion.setProgressBarWidth(Constant._PROGRESS_BAR_WIDTH * getResources().getDisplayMetrics().density);
        _cirFoodPortion.setColor(Constant._COLOR_RED);
        _cirFoodPortion.setBackgroundColor(adjustAlpha(Constant._COLOR_RED, 0.3f));

        _cirWaterCurrent = findViewById(R.id._cirWaterCurrent);
        _cirWaterCurrent.setProgressWithAnimation(Constant._MIN_PROGRESS);
        _cirWaterCurrent.setBackgroundProgressBarWidth(Constant._BPROGRESS_BAR_WIDTH * getResources().getDisplayMetrics().density);
        _cirWaterCurrent.setProgressBarWidth(Constant._PROGRESS_BAR_WIDTH * getResources().getDisplayMetrics().density);
        _cirWaterCurrent.setColor(Constant._COLOR_BLUE);
        _cirWaterCurrent.setBackgroundColor(adjustAlpha(Constant._COLOR_BLUE, 0.3f));

        _mHandlerThread = new HandlerThread("get-data-azure");
        _mHandlerThread.start();
        _mHandler = new Handler(_mHandlerThread.getLooper());
        _mHandler.post(getDataAzure);
    }

    private void updateGraphics(Dispenser dispenserData)
    {
        _lblDate.setText( android.text.format.DateFormat.format("dd/MM/yyyy HH:MM:ss", new java.util.Date()) );
        if( dispenserData.getListConfiguration().size() != 0 && dispenserData.getListFoodHour().size() != 0 )
        {
            Configuration _config = dispenserData.getListConfiguration().get(0);
            double _amountDailyFood = _config.getAmountDailyFood();
            double _amountPortionFood = _amountDailyFood/dispenserData.getListFoodHour().size();
            double _milliterWater = _config.getMilliLiterWater();

            int _progressDailyFood = Constant._MIN_PROGRESS;
            int _progressPortionFood = Constant._MIN_PROGRESS;
            int _progressWater = Constant._MIN_PROGRESS;

            double _amountDailyFoodCurrent = 0;
            double _amountPortionFoodCurrent = 0;
            double _amountWaterCurrent = 0;

            if( dispenserData.getListLastLog().size() != 0 )
            {
                _amountPortionFoodCurrent = (dispenserData.getListLastLog().get(0).getCurrentAmountFood()<=_amountPortionFood)?dispenserData.getListLastLog().get(0).getCurrentAmountFood():_amountPortionFood;
                _amountWaterCurrent = (dispenserData.getListLastLog().get(0).getCurrentMilliLiterWater()<=_milliterWater)?dispenserData.getListLastLog().get(0).getCurrentAmountFood():_milliterWater;

                _progressPortionFood = (int)((_amountPortionFoodCurrent*100)/_amountPortionFood);
                _progressWater = (int)((_amountWaterCurrent*100)/_milliterWater);
            }

            if( dispenserData.getListFoodDispenser().size() != 0 )
            {
                double _suma = 0;
                for(int i=0; i<dispenserData.getListFoodDispenser().size();i++)
                    _suma+=dispenserData.getListFoodDispenser().get(i).getAmountFoodDownloaded();
                _progressDailyFood = (int)((_suma*100)/_amountDailyFood);
            }

            _cirFoodPortion.setProgressWithAnimation(_progressDailyFood);
            _cirFoodPortion.setProgressWithAnimation(_progressPortionFood);
            _cirWaterCurrent.setProgressWithAnimation(_progressWater);

            _valFoodDaily.setText(String.valueOf(_amountDailyFoodCurrent)+"/"+String.valueOf(_amountDailyFood)+" "+Constant._UNIT_FOOD);
            _valFoodPortion.setText(String.valueOf(_amountPortionFoodCurrent)+"/"+String.valueOf(_amountPortionFood)+" "+Constant._UNIT_FOOD);
            _valWaterCurrent.setText(String.valueOf(_amountWaterCurrent)+"/"+String.valueOf(_milliterWater)+" "+Constant._UNIT_WATER);
        }
    }

    /**
     * Transparent the given color by the factor
     * The more the factor closer to zero the more the color gets transparent
     *
     * @param color  The color to transparent
     * @param factor 1.0f to 0.0f
     * @return int - A transplanted color
     */
    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    private Runnable getDataAzure = new Runnable()
    {
        @Override
        public void run()
        {
            try
            {
                DispenserService.getInstance()
                        .getJSONApi()
                        .getDispenser()
                        .enqueue(new Callback<Dispenser>() {
                            @Override
                            public void onResponse(@NonNull Call<Dispenser> call, @NonNull Response<Dispenser> response) {
                                updateGraphics(response.body());
                            }

                            @Override
                            public void onFailure(@NonNull Call<Dispenser> call, @NonNull Throwable t)
                            {
                                Log.e("on Failure", t.toString());
                            }
                        });
                _mHandler.postDelayed(this, Constant.DELAY_REQUEST); // Se vuelve e ajecutar el Run, en un tiempo: DELAY_REQUEST
            } catch (Exception e) {
                Log.e(TAG, "Error request Azure", e);
            }
        }
    };

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (_mHandler != null) {
            _mHandler.removeCallbacks(getDataAzure);
            _mHandlerThread.quitSafely();
        }
    }
}
