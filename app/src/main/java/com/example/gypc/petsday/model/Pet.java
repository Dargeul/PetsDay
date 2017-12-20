package com.example.gypc.petsday.model;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by StellaSong on 2017/12/19.
 */

public class Pet {
    private int pet_id;
    private String pet_nickname;
    private int pet_owner;
    private String pet_type;
    private int pet_weight;
    private String pet_sex;
    private Date pet_birth;
    private Bitmap pet_photo;
    private int pet_follow; // 需要SELECT数据库

    public Pet(String pet_nickname, String pet_type, int pet_follow) {
        this.pet_nickname = pet_nickname;
        this.pet_type = pet_type;
        this.pet_follow = pet_follow;
    }


    public int getPet_id() {
        return pet_id;
    }

    public String getPet_nickname() {
        return pet_nickname;
    }

    public int getPet_owner() {
        return pet_owner;
    }

    public Date getPet_birth() {
        return pet_birth;
    }

    public Bitmap getPet_photo() {
        return pet_photo;
    }

    public int getPet_weight() {
        return pet_weight;
    }

    public String getPet_sex() {
        return pet_sex;
    }

    public String getPet_type() {
        return pet_type;
    }

    public int getPet_follow() {
        return pet_follow;
    }
}
