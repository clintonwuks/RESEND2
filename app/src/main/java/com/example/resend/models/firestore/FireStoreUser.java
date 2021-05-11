package com.example.resend.models.firestore;

public class FireStoreUser {
    public String uuid;
    public String fullName;
    public String username;
    public String dateOfBirth;

    public FireStoreUser() {}

    public FireStoreUser(String uuid, String fullName, String username, String dateOfBirth) {
        this.uuid = uuid;
        this.fullName = fullName;
        this.username = username;
        this.dateOfBirth = dateOfBirth;
    }
}
