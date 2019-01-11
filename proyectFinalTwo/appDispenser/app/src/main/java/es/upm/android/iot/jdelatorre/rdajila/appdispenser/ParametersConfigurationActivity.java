package es.upm.android.iot.jdelatorre.rdajila.appdispenser;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.List;

import es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity.ConfigurationParameter;
import es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity.Dispenser;
import es.upm.android.iot.jdelatorre.rdajila.appdispenser.util.Constant;
import es.upm.android.iot.jdelatorre.rdajila.appdispenser.util.DispenserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParametersConfigurationActivity extends AppCompatActivity
{
    private static final String TAG = ParametersConfigurationActivity.class.getSimpleName();

    private Button _btnSave;

    private int _idConfig = -1;

    private TextInputLayout _til_food;
    private EditText _field_food;

    private TextInputLayout _til_water;
    private EditText _field_water;

    private TextInputLayout _til_bowl;
    private EditText _field_bowl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters_configuration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _til_food = (TextInputLayout)findViewById(R.id._til_food);
        _field_food = (EditText)findViewById(R.id._field_food);

        _til_water = (TextInputLayout)findViewById(R.id._til_water);
        _field_water = (EditText)findViewById(R.id._field_water);

        _til_bowl = (TextInputLayout)findViewById(R.id._til_bowl);
        _field_bowl = (EditText)findViewById(R.id._field_bowl);

        _btnSave = (Button)findViewById(R.id._btnSave);
        _btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { saveConfiguration(); }
        });

        loadParameterCondif();
    }

    private void loadParameterCondif()
    {
        new GetParameterConfigByIdTask().execute();
    }

    private void saveConfiguration()
    {
        ConfigurationParameter _config = new ConfigurationParameter();
        _config.setAmountBowlFoodWater(Double.parseDouble(_field_bowl.getText().toString()));
        _config.setAmountDailyFood(Double.parseDouble(_field_food.getText().toString()));
        _config.setAmountDailyWater(Double.parseDouble(_field_water.getText().toString()));

        Call<ConfigurationParameter> _call = DispenserService.getInstance().getJSONApi().createConfiguratonParam(_config);
        _call.enqueue(new Callback<ConfigurationParameter>() {
            @Override
            public void onResponse(Call<ConfigurationParameter> call, Response<ConfigurationParameter> response) {
                if (!response.isSuccessful())
                {
                    try
                    {
                        Log.e(TAG,response.errorBody().string());
                        setResult(Constant._ERROR_CONFIGURATION);
                        finish();
                    } catch (IOException e) {}
                    return;
                }
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<ConfigurationParameter> call, Throwable t) {
                Log.e("on Failure", t.toString());
                setResult(Constant._ERROR_CONFIGURATION);
                finish();
            }
        });
    }

    /**
     * Actualiza el valor de los campos del contacto en la pantalla
     * @param data
     */
    private void showConfiguration(List<ConfigurationParameter> data)
    {
        if(data.size() != 0 )
        {
            _idConfig = data.get(0).getConfigurationId();
            _field_food.setText(String.valueOf((int)data.get(0).getAmountDailyFood()));
            _field_water.setText(String.valueOf((int)data.get(0).getAmountDailyWater()));
            _field_bowl.setText(String.valueOf((int)data.get(0).getAmountBowlFoodWater()));
        }
    }

    /**
     * Clase gestiona la obtencion de los datos del contacto de DB
     */
    private class GetParameterConfigByIdTask extends AsyncTask<Void, Void, ConfigurationParameter>
    {
        @Override
        protected ConfigurationParameter doInBackground(Void... voids)
        {
            DispenserService.getInstance()
                    .getJSONApi()
                    .getConfigurationParameters()
                    .enqueue(new Callback<List<ConfigurationParameter>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<ConfigurationParameter>> call, @NonNull Response<List<ConfigurationParameter>> response) {
                            showConfiguration(response.body());
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<ConfigurationParameter>> call, @NonNull Throwable t)
                        {
                            Log.e("on Failure", t.toString());
                        }
                    });
            return  null;
        }

        @Override
        protected void onPostExecute(ConfigurationParameter data) { }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}