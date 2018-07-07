package com.example.gypc.petsday.model;

/**
 * Created by gypc on 2017/12/28.
 */

public class Comment {
    private String com_time;
    private int com_user;
    private int com_hs;  //指向动态表的id
    private String com_content;
    private int com_id;
    private String user_nickname;

    public Comment(int com_id,int com_hs,int com_user,String com_time,String com_content, String user_nickname){
        this.com_id = com_id;
        this.com_hs = com_hs;
        this.com_user = com_user;
        this.com_time = com_time;
        this.com_content = com_content;
        this.user_nickname = user_nickname;
    }

    public int getCom_user(){
        return com_user;
    }

    public int getCom_hs(){
        return com_hs;
    }

    public int getCom_id(){
        return  com_id;
    }

    public String getCom_time(){
        return com_time;
    }

    public String getCom_content(){
        return com_content;
    }

    public String getUser_nickname() {
        return user_nickname;
    }
}
