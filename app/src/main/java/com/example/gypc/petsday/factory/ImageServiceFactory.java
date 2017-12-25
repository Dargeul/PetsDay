package com.example.gypc.petsday.factory;

import com.example.gypc.petsday.service.ImageService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by XUJIJUN on 2017/12/24.
 */

public class ImageServiceFactory {
    public static final String FORMAT_ERR= "formatError";
    public static final String SUCCESS = "success";

    private static final String BASE_URL = "http://159.89.140.129:3000";
    private static ImageService service;
    private static OkHttpClient httpClient;

    public static ImageService getService() {
        if (service == null)
            service = createRetrofit().create(ImageService.class);
        return service;
    }

    private static OkHttpClient createHttpClient() {
        if (httpClient == null)
            httpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();
        return httpClient;
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
