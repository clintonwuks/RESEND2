package com.example.resend.models;

public class FriendRequest {
    public String username;
    public String uuid;

    public FriendRequest() {
    }

    public FriendRequest(String username, String uuid) {
        this.username = username;
        this.uuid = uuid;
    }
}
