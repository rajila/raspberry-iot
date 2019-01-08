package es.upm.android.iot.jdelatorre.rdajila.appdispenser.util;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DispenserService
{
    private static DispenserService mInstance;
    private static final String BASE_URL = Constant._PATH_SERVICE;
    private Retrofit mRetrofit;

    private DispenserService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public static DispenserService getInstance() {
        if (mInstance == null) {
            mInstance = new DispenserService();
        }
        return mInstance;
    }

    public RouteApi getJSONApi() {
        return mRetrofit.create(RouteApi.class);
    }
}