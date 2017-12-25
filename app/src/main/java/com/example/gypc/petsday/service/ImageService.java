package com.example.gypc.petsday.service;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by XUJIJUN on 2017/12/24.
 */

public interface ImageService {
    @Multipart
    @POST("/upload")
    Observable<Result<String>> uploadAvatar(@Part List<MultipartBody.Part> partList);
}
