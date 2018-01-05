package com.example.gypc.petsday.model;

/**
 * Created by XUJIJUN on 2018/1/3.
 */

public class UserNotification {
    private int notice_status;
    private int notice_user;
    private String user_nickname;
    private int notice_comment;
    private int notice_id;
    private String com_time;
    private int com_user;
    private int com_hs;
    private String com_content;

    public UserNotification(
            int notice_status,
            int notice_user,
            String user_nickname,
            int notice_comment,
            int notice_id,
            String com_time,
            int com_user,
            int com_hs,
            String com_content) {
        this.notice_status = notice_status;
        this.notice_user = notice_user;
        this.user_nickname = user_nickname;
        this.notice_comment = notice_comment;
        this.notice_id = notice_id;
        this.com_time = com_time;
        this.com_user = com_user;
        this.com_hs = com_hs;
        this.com_content = com_content;
    }


    public int getNotice_status() {
        return notice_status;
    }

    public void setNotice_status(int notice_status) {
        this.notice_status = notice_status;
    }

    public int getNotice_user() {
        return notice_user;
    }

    public void setNotice_user(int notice_user) {
        this.notice_user = notice_user;
    }

    public int getNotice_comment() {
        return notice_comment;
    }

    public void setNotice_comment(int notice_comment) {
        this.notice_comment = notice_comment;
    }

    public int getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(int notice_id) {
        this.notice_id = notice_id;
    }

    public String getCom_time() {
        return com_time;
    }

    public void setCom_time(String com_time) {
        this.com_time = com_time;
    }

    public int getCom_user() {
        return com_user;
    }

    public void setCom_user(int com_user) {
        this.com_user = com_user;
    }

    public int getCom_hs() {
        return com_hs;
    }

    public void setCom_hs(int com_hs) {
        this.com_hs = com_hs;
    }

    public String getCom_content() {
        return com_content;
    }

    public void setCom_content(String com_content) {
        this.com_content = com_content;
    }

    public String getUser_nickname(){return user_nickname;}

    public void setUser_nickname(String user_nickname){this.user_nickname = user_nickname;}
}
