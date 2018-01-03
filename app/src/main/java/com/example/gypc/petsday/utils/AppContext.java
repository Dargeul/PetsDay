package com.example.gypc.petsday.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.gypc.petsday.model.Good;
import com.example.gypc.petsday.model.Hotspot;
import com.example.gypc.petsday.model.Pet;
import com.example.gypc.petsday.model.UserNotification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by XUJIJUN on 2017/12/19.
 */

public class AppContext extends Application {

    public static final int NOT_LOGIN = -1;

    private static AppContext instance;
    private static LoginController loginController;

    private List<Pet> mypets;
    private List<Pet> followpets;
    private List<Hotspot> datas;

    private List<UserNotification> notifications;
    private List<Good> goods;

    private static OkHttpClient httpClient;

    private HashMap<String, Object> userInfoMap;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        loginController = new LoginController();

        mypets = new ArrayList<Pet>();
        followpets = new ArrayList<Pet>();
        datas = new ArrayList<Hotspot>();
        notifications = new ArrayList<UserNotification>();
        goods = new ArrayList<Good>();

        httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public static AppContext getInstance() {
        return instance;
    }

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public HashMap<String, Object> getLoginUserInfo() {
        if (userInfoMap == null)
            userInfoMap = loginController.getUserInfo();
        return userInfoMap;
    }

    public boolean setLoginUserInfo(HashMap<String, String> infoMap) {
        userInfoMap.put("user_id", Integer.parseInt(infoMap.get("user_id")));
        userInfoMap.put("username", infoMap.get("username"));
        userInfoMap.put("user_nickname", infoMap.get("user_nickname"));
        userInfoMap.put("password", infoMap.get("password"));
        return loginController.setUserInfo(infoMap);
    }

    private class LoginController {
        private static final String LOGIN_STATUS_PREF = "loginStatusPreference";

        public HashMap<String, Object> getUserInfo() {
            try {
                HashMap<String, Object> resData = new HashMap<>();
                SharedPreferences localData = getSharedPreferences(LOGIN_STATUS_PREF, MODE_PRIVATE);
                resData.put("user_id", localData.getInt("user_id", NOT_LOGIN));
                resData.put("username", localData.getString("username", null));
                resData.put("user_nickname", localData.getString("user_nickname", null));
                resData.put("password", localData.getString("password", null));

                return resData;
            } catch (Exception e) {
                Log.e("LoginController", "getUserId", e);
                return null;
            }
        }

        public boolean setUserInfo(HashMap<String, String> infoMap) {
            try {
                SharedPreferences.Editor editor = getSharedPreferences(LOGIN_STATUS_PREF, MODE_PRIVATE).edit();
                editor.putInt("user_id", Integer.valueOf(infoMap.get("user_id")));
                editor.putString("username", infoMap.get("username"));
                editor.putString("user_nickname", infoMap.get("user_nickname"));
                editor.putString("password", infoMap.get("password"));
                editor.commit();
                return true;
            } catch (Exception e) {
                Log.e("LoginController", "setUsername", e);
                return false;
            }
        }
    }

    public List<Pet> getMypets() {
        return mypets;
    }

    public List<Pet> getFollowpets() {
        return followpets;
    }

    public List<Hotspot> getDatas() {
        return datas;
    }

    public List<UserNotification> getNotifications(){
        return  notifications;
    }

    public List<Good> getGoods(){
        return goods;
    }
}
