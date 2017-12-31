package com.example.gypc.petsday.model;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by StellaSong on 2017/12/21.
 */

public class hotspot {
    private String hs_time;
    private int hs_user;
    private String hs_content;
    private int hs_id;
    private String hs_photo;
    private int hs_comment;
    private int hs_like;
    private boolean hs_islike;

    public hotspot(String hs_time, int hs_user, String hs_content, int hs_id, String hs_photo,
                   int hs_comment, int hs_like, boolean hs_islike){
        this.hs_time = hs_time;
        this.hs_user = hs_user;
        this.hs_content = hs_content;
        this.hs_id = hs_id;
        this.hs_photo = hs_photo;
        this.hs_comment = hs_comment;
        this.hs_like = hs_like;
        this.hs_islike = hs_islike;
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

    public int getHs_comment() {
        return hs_comment;
    }

    public int getHs_like() {
        return hs_like;
    }

    public boolean getHs_islike() {
        return hs_islike;
    }
}
