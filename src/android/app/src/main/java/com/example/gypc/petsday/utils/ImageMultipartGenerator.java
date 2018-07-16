package com.example.gypc.petsday.utils;

import android.util.Log;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;

/**
 * Created by XUJIJUN on 2017/12/25.
 */

public class ImageMultipartGenerator {

    private static final String IMG_NAME = "uploadFile"; //后台接收图片流的参数名

    public static List<MultipartBody.Part> getParts(String filePath) {
        try {
            File file = new File(filePath);//filePath 图片地址
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);//表单类型
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart(IMG_NAME, file.getName(), imageBody);

            List<MultipartBody.Part> parts = builder.build().parts();

            return parts;
        } catch (Exception e) {
            Log.e("ImageMultipartGenerator", "getParts", e);
            return null;
        }
    }
}
