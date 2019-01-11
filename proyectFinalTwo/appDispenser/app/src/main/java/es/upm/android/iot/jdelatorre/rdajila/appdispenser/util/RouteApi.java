package es.upm.android.iot.jdelatorre.rdajila.appdispenser.util;

import java.util.List;

import es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity.ConfigurationParameter;
import es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity.Dispenser;
import es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity.DispenserForm;
import es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity.LogDispenser;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RouteApi
{
    @GET("api/Log/Last")
    public Call<LogDispenser> getLastLog();

    @GET("api/Logs")
    public Call<List<LogDispenser>> getLogs();

    @GET("api/ConfigurationParameters")
    public Call<List<ConfigurationParameter>> getConfigurationParameters();

    @GET("api/Dispenser")
    public Call<Dispenser> getDispenser();

    @POST("api/Logs")
    Call<DispenserForm> createLog(@Body DispenserForm dispenser);

    @POST("api/ConfigurationParameters")
    Call<ConfigurationParameter> createConfiguratonParam(@Body ConfigurationParameter config);
}