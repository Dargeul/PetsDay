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
    private String pet_birth;
    private String pet_photo;
    private int count; // 表示粉丝数

    public Pet(int pet_id, String pet_nickname, int pet_owner, String pet_type, int pet_weight,
               String pet_sex, String pet_birth, String pet_photo, int count) {
        this.pet_id = pet_id;
        this.pet_nickname = pet_nickname;
        this.pet_owner = pet_owner;
        this.pet_type = pet_type;
        this.pet_weight = pet_weight;
        this.pet_sex = pet_sex;
        this.pet_birth = pet_birth;
        this.pet_photo = pet_photo;
        this.count = count;
    }


    public int getPet_id() {
        return pet_id;
    }

    public String getPet_nickname() {
        return pet_nickname;
    }

    public void setPet_nickname(String pet_nickname) {
        this.pet_nickname = pet_nickname;
    }

    public int getPet_owner() {
        return pet_owner;
    }

    public String getPet_birth() {
        return pet_birth;
    }

    public void setPet_birth(String pet_birth) {
        this.pet_birth = pet_birth;
    }

    public String getPet_photo() {
        return pet_photo;
    }

    public void setPet_photo(String pet_photo) {
        this.pet_photo = pet_photo;
    }

    public int getPet_weight() {
        return pet_weight;
    }

    public void setPet_weight(int weight) {
        this.pet_weight = weight;
    }

    public String getPet_sex() {
        return pet_sex;
    }

    public void setPet_sex(String pet_sex) {
        this.pet_sex = pet_sex;
    }

    public String getPet_type() {
        return pet_type;
    }

    public void setPet_type(String pet_type) {
        this.pet_type = pet_type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
