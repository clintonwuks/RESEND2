package com.example.resend;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingPage extends AppCompatActivity {

    TextView login, signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_loading_page);

        //pull textviews
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signup_hint);

        // import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "font/MLight.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "font/MMedium.ttf");
        Typeface MRegular = Typeface.createFromAsset(getAssets(), "font/MRegular.ttf");

        // apply font
        signUp.setTypeface(MMedium);
        login.setTypeface(MMedium);

        signUp.setOnClickListener(v -> {
            Intent i = new Intent(LoadingPage.this, SignUpActivity.class);
            startActivity(i);

        });

        login.setOnClickListener(v -> {
            Intent i = new Intent(LoadingPage.this, MainActivity.class);
            startActivity(i);

        });
    }


}