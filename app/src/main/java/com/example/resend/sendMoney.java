package com.example.resend;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class sendMoney extends AppCompatActivity {

    private EditText amountTV;
    private Button sendMoneyBtn;

    private String recipientId;

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private final String TAG = "APP_TEST";

    SharedPreferences preferences;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money2);

        recipientId = getIntent().getStringExtra("recipientId");
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        gson = new Gson();

        initElements();
    }

    public void initElements() {
        amountTV = findViewById(R.id.amount);
        sendMoneyBtn = findViewById(R.id.sendMoneyBtn);
        sendMoneyBtn.setOnClickListener(v -> getRecipient());
    }

    private void getRecipient() {

    }
}