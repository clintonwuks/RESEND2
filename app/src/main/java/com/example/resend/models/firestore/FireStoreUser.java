package com.example.resend.models.firestore;

import com.example.resend.Friends;
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
    public List<FriendRequest> friendRequest = new ArrayList<>();
    public List<Friends> friends = new ArrayList<>();
    public List<Transaction> transactions = new ArrayList<>();

    public FireStoreUser() {}

    public FireStoreUser(String uuid, String fullName, String username, String dateOfBirth) {
        this.uuid = uuid;
        this.fullName = fullName;
        this.username = username;
        this.dateOfBirth = dateOfBirth;
    }

    public FireStoreUser(String uuid, String fullName, String username, String dateOfBirth, Double wallet) {
        this.uuid = uuid;
        this.fullName = fullName;
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.wallet = wallet;
    }
}
