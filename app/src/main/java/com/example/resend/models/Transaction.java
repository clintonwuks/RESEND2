package com.example.resend.models;

import androidx.annotation.NonNull;

public class Transaction {
    public String type;
    public Double amount;
    public String date;
    public String status = "success";
    public String reference = "";

    public Transaction(String type, Double amount, String date) {
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    public Transaction(String type, Double amount, String date, String reference) {
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.reference = reference;
    }

    @NonNull
    @Override
    public String toString() {
        return "Transaction{" +
                "type='" + type + '\'' +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", reference='" + reference + '\'' +
                '}';
    }
}
