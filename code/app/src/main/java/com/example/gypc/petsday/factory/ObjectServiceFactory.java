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
    public static final int REGISTER_STATUS_CODE = 0;
    public static final int LOGIN_STATUS_CODE = 1;
    public static final int USERNAME_VALIDATE_STATUS_CODE = 2;

    public static final int GET_OWN_PET_STATUS_CODE = 1;
    public static final int GET_LIKE_PET_STATUS_CODE = 0;

    public static final int SEND_COMMENT_NOTIFICATION_STATUS_CODE = 0;

    private static final String BASE_URL = "http://120.78.78.181:8080";
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
