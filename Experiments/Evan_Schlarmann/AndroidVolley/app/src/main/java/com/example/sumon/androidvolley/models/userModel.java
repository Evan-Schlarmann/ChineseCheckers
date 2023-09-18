package com.example.sumon.androidvolley.models;

public class userModel {

    private String username;

    private String email;

    public userModel(String username, String email){
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String printUser()
    {
        return "Name: " + username + ", Email: " + email;
    }
    }
