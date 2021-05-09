package com.example.resend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView titlepage, subtitlepage, login, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //pull textviews
        titlepage = findViewById(R.id.titlepage);
        subtitlepage = findViewById(R.id.subtitlepage);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup_hint);

        //import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "font/MLight.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "font/MMedium.ttf");
        Typeface MRegular = Typeface.createFromAsset(getAssets(), "font/MRegular.ttf");

        // apply font
        titlepage.setTypeface(MRegular);
        subtitlepage.setTypeface(MLight);
        login.setTypeface(MMedium);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                Intent i = new Intent(MainActivity.this, signupActivity.class);
                startActivity(i);

            }

        });


    }
}