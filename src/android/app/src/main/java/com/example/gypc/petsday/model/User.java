package com.example.gypc.petsday.model;

public class User {
    private int user_id;
    private String password;
    private String username;
    private String user_nickname;

    public User(int user_id, String password, String username, String user_nickname) {
        this.user_id = user_id;
        this.password = password;
        this.username = username;
        this.user_nickname = user_nickname;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getUser_nickname() {
        return user_nickname;
    }
}
