package com.example.gypc.petsday.utils;

import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by XUJIJUN on 2017/12/30.
 */

public class JSONRequestBodyGenerator {

    public static RequestBody getJsonObjBody(HashMap<String, Object> data) {
        String dataStr = convertSingleData(data);
        Log.i("ObjectToJsonStr", "getJsonObjBody: dataStr: " + dataStr);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), convertSingleData(data));
        return requestBody;
    }

    public static RequestBody getJsonArrayBody(List<HashMap<String, Object>> datas) {
        String dataStr = "multiPost=" +convertDataArray(datas);
        Log.i("ObjectArrayToJsonStr", "getJsonArrayBody: dataStr: " + dataStr);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/x-www-form-urlencoded"), dataStr);
        return requestBody;
    }

    private static String convertDataArray(List<HashMap<String, Object>> datas) {
        JSONObject entireObj = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            for (HashMap<String, Object> data : datas) {
                jsonArray.put(getJsonObjFromMap(data));
            }
            return jsonArray.toString();
        } catch (Exception e) {
            Log.e("ObjectArrayToJsonStr", "convertDataArray", e);
            return "";
        }
    }

    private static String convertSingleData(HashMap<String, Object> data) {
        return getJsonObjFromMap(data).toString();
    }

    @Nullable
    private static JSONObject getJsonObjFromMap(HashMap<String, Object> data) {
        try {
            JSONObject object = new JSONObject();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                object.put(entry.getKey(), entry.getValue());
            }
            return object;
        } catch (Exception e) {
            Log.e("ObjectToJsonStr", "getJsonObjFromMap", e);
            return null;
        }
    }
}
