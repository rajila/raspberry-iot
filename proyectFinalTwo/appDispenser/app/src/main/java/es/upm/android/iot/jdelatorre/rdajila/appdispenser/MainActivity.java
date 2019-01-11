package es.upm.android.iot.jdelatorre.rdajila.appdispenser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity.ConfigurationParameter;
import es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity.Dispenser;
import es.upm.android.iot.jdelatorre.rdajila.appdispenser.util.Constant;
import es.upm.android.iot.jdelatorre.rdajila.appdispenser.util.DispenserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();

    private CircularProgressBar _cirFoodDailyDownloaded;
    private CircularProgressBar _cirFoodConsumed;
    private CircularProgressBar _cirWaterDailyDownloaded;
    private CircularProgressBar _cirWaterConsumed;
    private TextView _valFoodDailyDownloaded;
    private TextView _valFoodConsumed;
    private TextView _valWaterDailyDownloaded;
    private TextView _valWaterConsumed;

    private TextView _lblDate;

    private HandlerThread _mHandlerThread;
    private Handler _mHandler;

    private FloatingActionButton _btnFood;
    private FloatingActionButton _btnWater;

    private boolean _flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _lblDate = findViewById(R.id._lblDate);
        _valFoodDailyDownloaded = findViewById(R.id._valueFoodDaily);
        _valFoodConsumed = findViewById(R.id._valueFoodPortion);
        _valWaterDailyDownloaded = findViewById(R.id._valueWaterCurrent);
        _valWaterConsumed = findViewById(R.id._valueWaterConsumed);

        _cirFoodDailyDownloaded = findViewById(R.id._cirFoodDaily);
        _cirFoodDailyDownloaded.setProgressWithAnimation(Constant._MIN_PROGRESS);
        _cirFoodDailyDownloaded.setBackgroundProgressBarWidth(Constant._BPROGRESS_BAR_WIDTH * getResources().getDisplayMetrics().density);
        _cirFoodDailyDownloaded.setProgressBarWidth(Constant._PROGRESS_BAR_WIDTH * getResources().getDisplayMetrics().density);
        _cirFoodDailyDownloaded.setColor(Constant._COLOR_RED);
        _cirFoodDailyDownloaded.setBackgroundColor(adjustAlpha(Constant._COLOR_RED, 0.3f));

        _cirFoodConsumed = findViewById(R.id._cirFoodPortion);
        _cirFoodConsumed.setProgressWithAnimation(Constant._MIN_PROGRESS);
        _cirFoodConsumed.setBackgroundProgressBarWidth(Constant._BPROGRESS_BAR_WIDTH * getResources().getDisplayMetrics().density);
        _cirFoodConsumed.setProgressBarWidth(Constant._PROGRESS_BAR_WIDTH * getResources().getDisplayMetrics().density);
        _cirFoodConsumed.setColor(Constant._COLOR_RED);
        _cirFoodConsumed.setBackgroundColor(adjustAlpha(Constant._COLOR_RED, 0.3f));

        _cirWaterDailyDownloaded = findViewById(R.id._cirWaterCurrent);
        _cirWaterDailyDownloaded.setProgressWithAnimation(Constant._MIN_PROGRESS);
        _cirWaterDailyDownloaded.setBackgroundProgressBarWidth(Constant._BPROGRESS_BAR_WIDTH * getResources().getDisplayMetrics().density);
        _cirWaterDailyDownloaded.setProgressBarWidth(Constant._PROGRESS_BAR_WIDTH * getResources().getDisplayMetrics().density);
        _cirWaterDailyDownloaded.setColor(Constant._COLOR_BLUE);
        _cirWaterDailyDownloaded.setBackgroundColor(adjustAlpha(Constant._COLOR_BLUE, 0.3f));

        _cirWaterConsumed = findViewById(R.id._cirWaterConsumed);
        _cirWaterConsumed.setProgressWithAnimation(Constant._MIN_PROGRESS);
        _cirWaterConsumed.setBackgroundProgressBarWidth(Constant._BPROGRESS_BAR_WIDTH * getResources().getDisplayMetrics().density);
        _cirWaterConsumed.setProgressBarWidth(Constant._PROGRESS_BAR_WIDTH * getResources().getDisplayMetrics().density);
        _cirWaterConsumed.setColor(Constant._COLOR_BLUE);
        _cirWaterConsumed.setBackgroundColor(adjustAlpha(Constant._COLOR_BLUE, 0.3f));

        _btnFood = (FloatingActionButton)findViewById(R.id._btnFood);
        _btnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_flag) return;
                _flag = true;
                DispenserService.getInstance()
                        .getJSONApi()
                        .getDispenser()
                        .enqueue(new Callback<Dispenser>() {
                            @Override
                            public void onResponse(@NonNull Call<Dispenser> call, @NonNull Response<Dispenser> response) {
                                dispenseFood(response.body());
                            }

                            @Override
                            public void onFailure(@NonNull Call<Dispenser> call, @NonNull Throwable t)
                            {
                                _flag = false;
                                Log.e("on Failure", t.toString());
                            }
                        });
            }
        });

        _btnWater = (FloatingActionButton)findViewById(R.id._btnWater);
        _btnWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"Action _btnWater");
            }
        });

        _mHandlerThread = new HandlerThread("get-data-azure");
        _mHandlerThread.start();
        _mHandler = new Handler(_mHandlerThread.getLooper());
        _mHandler.post(getDataAzure);
    }

    private void dispenseFood(Dispenser dispenserData)
    {
        /*if( dispenserData.getListConfigurationParameter().size() != 0 )
        {
            ConfigurationParameter _config = dispenserData.getListConfigurationParameter().get(0);
            double _amountDailyFood = _config.getAmountDailyFood();
            double _amountPortionFood = _amountDailyFood/dispenserData.getListFoodHour().size();

            double _amountDailyFoodCurrent = 0;
            double _amountPortionFoodCurrent = 0;

            if( dispenserData.getListLastLog().size() == 0 )
            {
                //Mensaje
                return;
            }

            _amountPortionFoodCurrent = (dispenserData.getListLastLog().get(0).getCurrentAmountFood()<_amountPortionFood)?dispenserData.getListLastLog().get(0).getCurrentAmountFood():_amountPortionFood;

            if( dispenserData.getListFoodDispenser().size() != 0 )
                for(int i=0; i<dispenserData.getListFoodDispenser().size();i++)
                    _amountDailyFoodCurrent+=dispenserData.getListFoodDispenser().get(i).getAmountFoodDownloaded();

            if( _amountDailyFood-_amountDailyFoodCurrent <= 0 )
            {
                // mensaje nooo
                return;
            }

            double _diffFood = (_amountPortionFood - _amountPortionFoodCurrent < 0)?0:_amountPortionFood - _amountPortionFoodCurrent;
            if(_diffFood == 0)
            {
                // mensaje noooo
                return;
            }
            _diffFood = (_diffFood < (_amountDailyFood-_amountDailyFoodCurrent))? _diffFood : (_amountDailyFood-_amountDailyFoodCurrent);

            int _angleServoFood = 0;
            int _openingSecondsFood = 5;
            if( _diffFood <= 0 ) {
                _angleServoFood = 0;
                _openingSecondsFood = 0;
            }else if(_diffFood <= (_amountPortionFood*0.10))
                _angleServoFood = 10;
            else if(_diffFood <= (_amountPortionFood*0.20))
                _angleServoFood = 20;
            else if(_diffFood <= (_amountPortionFood*0.30))
                _angleServoFood = 30;
            else if(_diffFood <= (_amountPortionFood*0.40))
                _angleServoFood = 40;
            else if(_diffFood <= (_amountPortionFood*0.50))
                _angleServoFood = 50;
            else if(_diffFood <= (_amountPortionFood*0.60))
                _angleServoFood = 60;
            else if(_diffFood <= (_amountPortionFood*0.70))
                _angleServoFood = 70;
            else if(_diffFood <= (_amountPortionFood*0.80))
                _angleServoFood = 80;
            else if(_diffFood <= (_amountPortionFood*0.90))
                _angleServoFood = 90;
            else
                _angleServoFood = 120;


            LogDispenser _logDB = dispenserData.getListLastLog().get(0);
            DispenserForm _dispenser = new DispenserForm();
            _dispenser.setThingId(_logDB.getThingId());
            _dispenser.setActionType(Constant._ACTION_TYPE);
            _dispenser.setAmountDailyFood(_amountDailyFood);
            _dispenser.setFoodPortion(_amountPortionFood);
            _dispenser.setMilliLiterWater(_logDB.getMilliLiterWater());
            _dispenser.setMinPercentWater(_logDB.getMinPercentWater());
            _dispenser.setCurrentAmountFood(_amountPortionFoodCurrent);
            _dispenser.setAmountFoodDownloaded(_diffFood);
            _dispenser.setCurrentMilliLiterWater(_logDB.getCurrentMilliLiterWater());
            _dispenser.setMilliLiterWaterDownloaded(_logDB.getMilliLiterWaterDownloaded());
            _dispenser.setAngleServoFood(_angleServoFood);
            _dispenser.setOpeningSecondsFood(_openingSecondsFood);
            _dispenser.setAngleServoWater(_logDB.getAngleServoWater());
            _dispenser.setOpeningSecondsWater(_logDB.getOpeningSecondsWater());
            _dispenser.setDispenseFood(1);
            _dispenser.setDispenseWater(0);

            Call<DispenserForm> _call = DispenserService.getInstance().getJSONApi().createLog(_dispenser);
            _call.enqueue(new Callback<DispenserForm>() {
                @Override
                public void onResponse(Call<DispenserForm> call, Response<DispenserForm> response) {
                    _flag = false;
                    if (!response.isSuccessful())
                    {
                        try
                        {
                            Log.e(TAG,response.errorBody().string());
                        } catch (IOException e) {}
                        return;
                    }
                }

                @Override
                public void onFailure(Call<DispenserForm> call, Throwable t) {
                    _flag = false;
                    Log.e("on Failure", t.toString());
                }
            });
        }
        _flag = false;*/
    }

    private void updateGraphics(Dispenser dispenserData)
    {
        _lblDate.setText( android.text.format.DateFormat.format("dd MMMM yyyy, HH:MM:ss", new java.util.Date()) );
        if( dispenserData.getListConfigurationParameter().size() != 0 )
        {
            ConfigurationParameter _config = dispenserData.getListConfigurationParameter().get(0);
            double _amountDailyFood = _config.getAmountDailyFood();
            double _amountBowlFoodWater = _config.getAmountBowlFoodWater();
            double _amountDailyWater = _config.getAmountDailyWater();

            int _progressDailyFood = Constant._MIN_PROGRESS;
            int _progressConsumedFood = Constant._MIN_PROGRESS;
            int _progressDailyWater = Constant._MIN_PROGRESS;
            int _progressConsumedWater = Constant._MIN_PROGRESS;

            double _amountDailyFoodDownloaded = 0;
            double _amountBowlFoodCurrent = 0;
            double _amountDailyFoodConsumed = 0;
            double _amountWaterDailyDownloaded = 0;
            double _amountBowlWaterCurrent = 0;
            double _amountDailyWaterConsumed = 0;

            if( dispenserData.getListLastLog().size() != 0 )
            {
                _amountBowlFoodCurrent = (dispenserData.getListLastLog().get(0).getCurrentAmountFood()<_amountBowlFoodWater)?dispenserData.getListLastLog().get(0).getCurrentAmountFood():_amountBowlFoodWater;
                _amountBowlWaterCurrent = (dispenserData.getListLastLog().get(0).getCurrentAmountWater()<_amountBowlFoodWater)?dispenserData.getListLastLog().get(0).getCurrentAmountWater():_amountBowlFoodWater;
            }

            if( dispenserData.getListFoodDispenser().size() != 0 )
            {
                for(int i=0; i<dispenserData.getListFoodDispenser().size();i++)
                    _amountDailyFoodDownloaded+=dispenserData.getListFoodDispenser().get(i).getAmountFoodDownloaded();
                _progressDailyFood = (int)((_amountDailyFoodDownloaded*100)/_amountDailyFood);
            }

            if( dispenserData.getListWaterDispenser().size() != 0 )
            {
                for(int i=0; i<dispenserData.getListWaterDispenser().size();i++)
                    _amountWaterDailyDownloaded+=dispenserData.getListWaterDispenser().get(i).getAmountWaterDownloaded();
                _progressDailyWater = (int)((_amountWaterDailyDownloaded*100)/_amountDailyWater);
            }

            _amountDailyFoodConsumed = ((_amountDailyFoodDownloaded - _amountBowlFoodCurrent)<0)?0:_amountDailyFoodDownloaded - _amountBowlFoodCurrent;
            _progressConsumedFood = (int)((_amountDailyFoodConsumed*100)/_amountDailyFood);

            _amountDailyWaterConsumed = ((_amountWaterDailyDownloaded - _amountBowlWaterCurrent)<0)?0:_amountWaterDailyDownloaded - _amountBowlWaterCurrent;
            _progressConsumedWater = (int)((_amountDailyWaterConsumed*100)/_amountDailyWater);

            _cirFoodDailyDownloaded.setProgressWithAnimation(_progressDailyFood);
            _cirFoodConsumed.setProgressWithAnimation(_progressConsumedFood);
            _cirWaterDailyDownloaded.setProgressWithAnimation(_progressDailyWater);
            _cirWaterConsumed.setProgressWithAnimation(_progressConsumedWater);

            _valFoodDailyDownloaded.setText(String.valueOf(_amountDailyFoodDownloaded)+" / "+String.valueOf(_amountDailyFood)+" "+Constant._UNIT_FOOD);
            _valFoodConsumed.setText(String.valueOf(_amountDailyFoodConsumed)+" / "+String.valueOf(_amountDailyFood)+" "+Constant._UNIT_FOOD);
            _valWaterDailyDownloaded.setText(String.valueOf(_amountWaterDailyDownloaded)+" / "+String.valueOf(_amountDailyWater)+" "+Constant._UNIT_WATER);
            _valWaterConsumed.setText(String.valueOf(_amountDailyWaterConsumed)+" / "+String.valueOf(_amountDailyWater)+" "+Constant._UNIT_WATER);
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() )
        {
            case R.id._action_settings: // Importar los contactos
                Intent intent = new Intent(this, ParametersConfigurationActivity.class);
                startActivityForResult(intent, Constant._REQUEST_CONFIGURATION);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == Constant._REQUEST_CONFIGURATION) {
            if (resultCode == Activity.RESULT_OK)
            {
                Toast.makeText(this, R.string.msn_save_data, Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Constant._ERROR_CONFIGURATION)
            {
                Toast.makeText(this, R.string.error_save_data, Toast.LENGTH_SHORT).show();
            }
        }
    }

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
