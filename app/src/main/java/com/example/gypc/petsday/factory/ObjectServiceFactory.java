package com.example.gypc.petsday.factory;

import com.example.gypc.petsday.service.ObjectService;
import com.example.gypc.petsday.utils.AppContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by XUJIJUN on 2017/12/30.
 */

public class ObjectServiceFactory {
    private static final String BASE_URL = "http://120.78.169.206:3000";
    private static ObjectService service;

    public static ObjectService getService() {
        if (service == null)
            service = createRetrofit().create(ObjectService.class);
        return service;
    }

    private static OkHttpClient createHttpClient() {
        return AppContext.getInstance().getHttpClient();
    }

    private static Retrofit createRetrofit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createHttpClient())
                .build();
    }
}
