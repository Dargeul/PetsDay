package com.example.gypc.petsday.model;

/**
 * Created by gypc on 2017/12/31.
 */

public class Notification {
    private String com_user;
    private int com_walk;  //指向动态表的id
    private int com_id;
    private String com_time;
    private String com_content;

    private int target_user;
    private int notice_id;
    private int notice_status;

    //通知默认未读,生成通知进行上传
    public Notification(int notice_id,int target_user, int com_id){
        this.notice_id = notice_id;
        this.notice_status = 0;
        this.com_id = com_id;
        this.target_user = target_user;
    }

    public Notification(int notice_id,int notice_status,int target_user,
                        int com_id,int com_walk,String com_user,String com_time,String com_content){
        this.notice_id = notice_id;
        this.notice_status = notice_status;
        this.com_id = com_id;
        this.com_walk = com_walk;
        this.com_user = com_user;
        this.com_time = com_time;
        this.com_content = com_content;
        this.target_user = target_user;
    }

    public String getCom_user(){
        return com_user;
    }

    public int getCom_walk(){
        return com_walk;
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

    public int getTarget_user(){ return target_user; }

    public void setNotice_status(int notice_status){
        this.notice_status = notice_status;
    }
}

