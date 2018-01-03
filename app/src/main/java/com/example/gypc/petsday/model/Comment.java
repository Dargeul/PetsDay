package com.example.gypc.petsday.model;

/**
 * Created by gypc on 2017/12/28.
 */

public class Comment {
    private String com_time;
    private String com_user;
    private int com_hs;  //指向动态表的id
    private String com_content;
    private int com_id;

    public Comment(int com_id,int com_hs,String com_user,String com_time,String com_content){
        this.com_id = com_id;
        this.com_hs = com_hs;
        this.com_user = com_user;
        this.com_time = com_time;
        this.com_content = com_content;
    }

    public String getCom_user(){
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
}
