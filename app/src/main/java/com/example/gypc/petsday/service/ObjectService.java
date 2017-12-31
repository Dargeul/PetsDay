package com.example.gypc.petsday.service;

import org.json.JSONObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.Result;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import rx.Observable;

/**
 * Created by XUJIJUN on 2017/12/30.
 */

public interface ObjectService {
    @POST("/pet")
    Observable<Result<Integer>> insertPet(@Body RequestBody jsonBody);

//    @POST("/hotspot")
//    Observable<>

    @PUT("/pet")
    Observable<ResponseBody> updatePet(@Body RequestBody jsonBody);
}
