package com.example.gypc.petsday.model;

/**
 * Created by XUJIJUN on 2018/1/5.
 */

public class HotspotLike {
    private int like_user;
    private int like_hotspot;
    private int like_id;

    HotspotLike(int like_id, int like_hotspot, int like_user) {
        this.like_id = like_id;
        this.like_hotspot = like_hotspot;
        this.like_user = like_user;
    }

    public int getLike_hotspot() {
        return like_hotspot;
    }

    public int getLike_id() {
        return like_id;
    }
}
