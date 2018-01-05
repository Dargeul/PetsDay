package com.example.gypc.petsday.model;

import android.graphics.Bitmap;
import android.os.Bundle;

import java.util.Date;

/**
 * Created by StellaSong on 2017/12/21.
 */

public class Hotspot {
    private String hs_time;
    private int hs_user;
    private String hs_content;
    private int hs_id;
    private String hs_photo;
    private int count;  // 表示点赞数
    private String user_nickname;

    public Hotspot(String hs_time, int hs_user, String hs_content, int hs_id, String hs_photo, int count, String user_nickname){
        this.hs_time = hs_time;
        this.hs_user = hs_user;
        this.hs_content = hs_content;
        this.hs_id = hs_id;
        this.hs_photo = hs_photo;
        this.count = count;
        this.user_nickname = user_nickname;
    }

    public String getHs_time() {
        return hs_time;
    }

    public int getHs_user() {
        return hs_user;
    }

    public String getHs_content() {
        return hs_content;
    }

    public int getHs_id() {
        return hs_id;
    }

    public String getHs_photo() {
        return hs_photo;
    }

    public int getCount() {
        return count;
    }

    public Bundle getBundle() {
        Bundle bundle = new Bundle();

        bundle.putString("hs_time", hs_time);
        bundle.putInt("hs_user", hs_user);
        bundle.putString("hs_content", hs_content);
        bundle.putInt("hs_id", hs_id);
        bundle.putString("hs_photo", hs_photo);
        bundle.putInt("count", count);
        bundle.putString("user_nickname", user_nickname);

        return bundle;
    }

    public String getUser_nickname() {
        return user_nickname;
    }
}
