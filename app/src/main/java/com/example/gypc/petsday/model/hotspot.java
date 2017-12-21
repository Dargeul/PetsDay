package com.example.gypc.petsday.model;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by StellaSong on 2017/12/21.
 */

public class hotspot {
    private Date hs_time;
    private int hs_user;
    private String hs_content;
    private int hs_id;
    private Bitmap hs_photo;

    public Date getHs_time() {
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

    public Bitmap getHs_photo() {
        return hs_photo;
    }
}
