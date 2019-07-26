package com.example.feedonomic;

public class UserLoginData {

    public UserLoginData() {

    }

    public UserLoginData(String name, String email, String password, String userId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userId = userId;
    }

    public String name, email, password, userId;

}
