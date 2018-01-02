package com.example.gypc.petsday.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.gypc.petsday.model.Good;
import com.example.gypc.petsday.model.Notification;
import com.example.gypc.petsday.model.Pet;
import com.example.gypc.petsday.model.hotspot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by XUJIJUN on 2017/12/19.
 */

public class AppContext extends Application {

    private static AppContext instance;
    private static LoginController loginController;
    private String username;

    private List<Pet> mypets;
    private List<Pet> followpets;
    private List<hotspot> datas;
    private List<Notification> notifications;
    private List<Good> goods;
    private static OkHttpClient httpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        loginController = new LoginController();

        mypets = new ArrayList<Pet>();
        followpets = new ArrayList<Pet>();
        datas = new ArrayList<hotspot>();
        notifications = new ArrayList<Notification>();
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

    public String getLoginUsername() {
        if (username == null)
            username = loginController.getUsername();
        return username;
    }

    public boolean setLoginUsername(String username) {
        if (loginController.setUsername(username)) {
            this.username = username;
            return true;
        }
        return false;
    }

    private class LoginController {
        private static final String LOGIN_STATUS_PREF = "loginStatusPreference";

        public String getUsername() {
            try {
                String name = getSharedPreferences(LOGIN_STATUS_PREF, MODE_PRIVATE).getString("username", null);
                return name;
            } catch (Exception e) {
                Log.e("LoginController", "getUsername", e);
                return null;
            }
        }

        public boolean setUsername(String username) {
            try {
                SharedPreferences.Editor editor = getSharedPreferences(LOGIN_STATUS_PREF, MODE_PRIVATE).edit();
                editor.putString("username", username);
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

    public List<hotspot> getDatas() {
        return datas;
    }

    public List<Notification> getNotifications(){return  notifications;}

    public List<Good> getGoods(){return goods;}
}
