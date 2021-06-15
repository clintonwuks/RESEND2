package com.example.resend.models.firestore;

import androidx.annotation.NonNull;

import com.example.resend.models.Friend;
import com.example.resend.models.FriendRequest;
import com.example.resend.models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class FireStoreUser {
    public String uuid;
    public String fullName;
    public String username;
    public String dateOfBirth;
    public Double wallet = 0.0;
    public List<String> friends = new ArrayList<>();
    public List<String> friendRequest = new ArrayList<>();
    public List<String> sentFriendRequest = new ArrayList<>();

    public FireStoreUser() {}

    public FireStoreUser(String uuid, String fullName, String username, String dateOfBirth) {
        this.uuid = uuid;
        this.fullName = fullName;
        this.username = username;
        this.dateOfBirth = dateOfBirth;
    }

    public String getUserAcronym() {
        return String.valueOf(this.fullName.charAt(0));
    }

    @NonNull
    @Override
    public String toString() {
        return "FireStoreUser{" +
                "uuid='" + uuid + '\'' +
                ", fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", wallet=" + wallet +
                ", friends=" + friends +
                ", friendRequest=" + friendRequest +
                ", sentFriendRequest=" + sentFriendRequest +
                '}';
    }
}
