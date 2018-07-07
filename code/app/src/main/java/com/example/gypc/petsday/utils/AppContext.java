package com.example.gypc.petsday.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.gypc.petsday.HotSpotFragment;
import com.example.gypc.petsday.MainActivity;
import com.example.gypc.petsday.MineFragment;
import com.example.gypc.petsday.factory.ObjectServiceFactory;
import com.example.gypc.petsday.model.Good;
import com.example.gypc.petsday.model.Hotspot;
import com.example.gypc.petsday.model.HotspotLike;
import com.example.gypc.petsday.model.Pet;
import com.example.gypc.petsday.model.UserNotification;
import com.example.gypc.petsday.service.ObjectService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XUJIJUN on 2017/12/19.
 */

public class AppContext extends Application {

    public static final int NOT_LOGIN = -1;

    private static AppContext instance;
    private static LoginController loginController;

    private List<Pet> mypets;
    private List<Pet> followpets;
    private List<Hotspot> initHotspots;
    private List<HotspotLike> initLikeList;
    private List<UserNotification> notifications;
    private List<Good> goods;

    private static OkHttpClient httpClient;

    private HashMap<String, Object> userInfoMap;

    private ObjectService objectService;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        loginController = new LoginController();

        mypets = new ArrayList<Pet>();
        followpets = new ArrayList<Pet>();
        initHotspots = new ArrayList<Hotspot>();
        notifications = new ArrayList<UserNotification>();
        goods = new ArrayList<Good>();
        initLikeList = new ArrayList<>();

        httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        objectService = ObjectServiceFactory.getService();
    }

    public static AppContext getInstance() {
        return instance;
    }

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public HashMap<String, Object> getLoginUserInfo() {
        if (userInfoMap == null) {
            userInfoMap = loginController.getUserInfo();
            initAppDataFromRemote();
        }
        return userInfoMap;
    }

    public boolean setLoginUserInfo(HashMap<String, String> infoMap) {
        userInfoMap.put("user_id", Integer.parseInt(infoMap.get("user_id")));
        userInfoMap.put("username", infoMap.get("username"));
        userInfoMap.put("user_nickname", infoMap.get("user_nickname"));
        userInfoMap.put("password", infoMap.get("password"));
        return loginController.setUserInfo(infoMap);
    }

    public void initPetLists() {
        updateOwnPetList();
        updateFollowPetList(null, false);
    }

    public void updateOwnPetList() {
        objectService
                .getPetListForUser((int)userInfoMap.get("user_id"))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Pet>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("AppContext", "updateOwnPetList: complete, mypet.size() = " + String.valueOf(mypets.size()));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("AppContext", "updateOwnPetList", throwable);
                    }

                    @Override
                    public void onNext(List<Pet> pets) {
                        if (pets == null)
                            pets = new ArrayList<>();
                        mypets = pets;
                    }
                });
    }

    public void updateFollowPetList(Pet pet, boolean toAdd) {
        if (pet != null) {
            if (toAdd) {
                pet.setCount(pet.getCount() + 1);
                followpets.add(pet);
                Log.i("AppContext", "updateFollowPetList: fans success");
                MineFragment.getInstance().updateFollowPetList();
            } else {
                int position;
                boolean exist = false;

                for (position = 0; position < followpets.size(); position++) {
                    if (followpets.get(position).getPet_id() == pet.getPet_id()) {
                        exist = true;
                        break;
                    }
                }

                if (exist) {
                    followpets.remove(position);
                    Log.i("AppContext", "updateFollowPetList: cancel fans success");
                    MineFragment.getInstance().updateFollowPetList();
                } else {
                    Log.e("AppContext", "updateFollowPetList: cancel fans fail!");
                }
            }
            return;
        }
        objectService
                .getUserFollowPetList((int)userInfoMap.get("user_id"))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Pet>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("AppContext", "updateFollowPetList: complete, followpets.size() = " + String.valueOf(followpets.size()));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("AppContext", "updateFollowPetList", throwable);
                    }

                    @Override
                    public void onNext(List<Pet> pets) {
                        if (pets == null)
                            pets = new ArrayList<>();
                        followpets = pets;
                    }
                });
    }

    private void initAppDataFromRemote() {
        initPetLists();
        objectService
                .getLikeListByUserId(String.valueOf(userInfoMap.get("user_id")))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<HotspotLike>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("AppContext", "initAppDataFromRemote: complete, initLikeList.size() = " + String.valueOf(initLikeList.size()));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("AppContext", "initAppDataFromRemote", throwable);
                    }

                    @Override
                    public void onNext(List<HotspotLike> hotspotLikes) {
                        if (hotspotLikes == null)
                            hotspotLikes = new ArrayList<>();
                        initLikeList = hotspotLikes;
                    }
                });
    }

    public List<HotspotLike> getInitLikeList() {
        return initLikeList;
    }

    public void cancelLikeHotspot(int hotspotId) {
        for (HotspotLike like : initLikeList) {
            if (like.getLike_hotspot() == hotspotId)
                initLikeList.remove(like);
        }
    }

    public void likeHotspot(HotspotLike like) {
        initLikeList.add(like);
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

    public List<UserNotification> getNotifications(){
        return  notifications;
    }

    public List<Good> getGoods(){
        return goods;
    }

    // 全局用户信息更新接口
    public void updateUserInfo(HashMap<String, Object> newInfo) {
        this.userInfoMap = newInfo;
    }
}
