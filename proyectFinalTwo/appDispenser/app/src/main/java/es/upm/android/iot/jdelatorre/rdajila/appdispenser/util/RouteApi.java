package es.upm.android.iot.jdelatorre.rdajila.appdispenser.util;

import java.util.List;

import es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity.Dispenser;
import es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity.LogDispenser;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RouteApi
{
    @GET("/api/Log/Last")
    public Call<LogDispenser> getLastLog();

    @GET("/api/Logs")
    public Call<List<LogDispenser>> getLogs();

    @GET("api/Dispenser")
    public Call<Dispenser> getDispenser();
}