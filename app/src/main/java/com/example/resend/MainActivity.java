package com.example.resend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    TextView titlePage, subtitlePage, login, signUp;

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private final String TAG = "APP_TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            gotoHomepage();
        }

        // pull text views
        titlePage = findViewById(R.id.titlePage);
        subtitlePage = findViewById(R.id.subtitlePage);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signup_hint);

        // import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "font/MLight.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "font/MMedium.ttf");
        Typeface MRegular = Typeface.createFromAsset(getAssets(), "font/MRegular.ttf");

        // apply font
        titlePage.setTypeface(MRegular);
        subtitlePage.setTypeface(MLight);
        login.setTypeface(MMedium);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);

            }
        });


    }

    private void gotoHomepage() {
        Intent intent = new Intent(this, HomepageActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}