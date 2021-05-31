package com.example.resend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HomepageActivity extends AppCompatActivity {

    Button logoutBtn;

    private FirebaseAuth firebaseAuth;
    private final String TAG = "APP_TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        initElements();

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void initElements() {
        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(v -> logout());
    }

    private void logout() {
        // Todo Start screen loader here
        Log.v(TAG, "Clicked logout");
        firebaseAuth.signOut();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}