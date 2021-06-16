package com.example.resend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.resend.models.firestore.FireStoreUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class sendMoney extends AppCompatActivity {

    private EditText amountTV;
    private Button sendMoneyBtn;

    private String recipientId;
    private FireStoreUser recipient;
    private FireStoreUser user;

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
        DocumentReference ref = db.collection("Users").document(recipientId);
        ref.get().addOnSuccessListener(task -> {
            recipient = task.toObject(FireStoreUser.class);

            if (recipient != null) {
                debitUser();
            } else Log.v(TAG, "User not found");
        });
    }

    private void debitUser() {
        FirebaseUser fbUser = firebaseAuth.getCurrentUser();
        Double amount = Double.parseDouble(amountTV.getText().toString());

        if (fbUser != null) {
            String uid = fbUser.getUid();
            DocumentReference ref = db.collection("Users").document(uid);
            ref.get().addOnSuccessListener(task -> {
                user = task.toObject(FireStoreUser.class);

                if (user != null) {
                    if (user.wallet >= amount) {
                        user.wallet -= amount;
                        ref.update("wallet", user.wallet)
                                .addOnSuccessListener(task2 -> {
                                    creditRecipient(amount);
                                    updateUser(user);
                                });

                    } else Log.v(TAG, "Insufficient Funds");
                } else Log.v(TAG, "User not found");
            });
        }
    }

    private void creditRecipient(Double amount) {
        DocumentReference ref = db.collection("Users").document(recipientId);
        ref.get().addOnSuccessListener(task -> {
            recipient = task.toObject(FireStoreUser.class);

            if (recipient != null) {
                recipient.wallet += amount;
                ref.update("wallet", recipient.wallet)
                        .addOnSuccessListener(task2 -> {
                            goToHomepage();
                        });
            } else Log.v(TAG, "User not found");
        });
    }

    private void goToHomepage() {
        // todo end loader
        Intent intent = new Intent(this, HomepageActivity.class);
        startActivity(intent);
    }

    private void updateUser(FireStoreUser user) {
        String userKey = getString(R.string.user_key);
        String userJson = gson.toJson(user);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(userKey, userJson);
        editor.apply();
    }
}