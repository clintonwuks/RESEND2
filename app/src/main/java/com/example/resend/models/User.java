package com.example.resend.models;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
    public String fullName;
    public String username;
    public LocalDate dateOfBirth;
    public String password;
    public Double wallet = 0.0;

    public User() {
    }

    public User(String fullName, String username, LocalDate dateOfBirth, String password) {
        this.fullName = fullName;
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
    }

    public User(String fullName, String username, LocalDate dateOfBirth, String password, Double wallet) {
        this.fullName = fullName;
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.wallet = wallet;
    }

    @Override
    @NonNull
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", password='" + password + '\'' +
                ", wallet='" + wallet + '\'' +
                '}';
    }
}
