package com.example.gypc.petsday.utils;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by XUJIJUN on 2017/12/30.
 */

public class JSONRequestBodyGenerator {

    public static RequestBody getBody(HashMap<String, Object> data) {
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), convert(data));
        return requestBody;
    }

    private static String convert(HashMap<String, Object> data) {
        String res = "";
        JSONObject entireObj = new JSONObject();
        try {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                entireObj.put(entry.getKey(), entry.getValue());
            }
            res = entireObj.toString();
            Log.i("ObjectToJsonStr", "result = \n" + entireObj.toString(2));
        } catch (Exception e) {
            Log.e("ObjectToJsonStr", "convert", e);
        }
        return res;
    }
}
