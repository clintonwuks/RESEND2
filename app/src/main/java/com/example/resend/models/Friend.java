package com.example.resend.models;

public class Friend {
    public String username;
    public String fullName;
    public String uuid;

    public Friend() {
    }

    public Friend(String username, String fullName, String uuid) {
        this.username = username;
        this.fullName = fullName;
        this.uuid = uuid;
    }
}
