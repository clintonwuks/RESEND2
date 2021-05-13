package com.example.resend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    TextView titlePage, subtitlePage, login, signUp;
    ImageView backButton;

    EditText usernameEDT;
    EditText passwordEDT;
    Button loginBTN;

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private final String TAG = "APP_TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initElements();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            gotoHomepage();
        }

        // pull text views
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signup_hint);
        backButton = findViewById(R.id.backImage);

        // import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "font/MLight.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "font/MMedium.ttf");
        Typeface MRegular = Typeface.createFromAsset(getAssets(), "font/MRegular.ttf");

        // apply font
        login.setTypeface(MMedium);

        signUp.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(i);

        });

        backButton.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, LoadingPage.class);
            startActivity(i);

        });



    }

    private void initElements() {
        usernameEDT = findViewById(R.id.username);
        passwordEDT = findViewById(R.id.password);
        loginBTN = findViewById(R.id.login_button);
        loginBTN.setOnClickListener(v -> login());
    }

    private void login() {
        // Todo Start screen loader here

        if( TextUtils.isEmpty(usernameEDT.getText()) && TextUtils.isEmpty(passwordEDT.getText()) ){
            Toast.makeText(getApplicationContext(),
                    "Check Textfields are not empty",
                    Toast.LENGTH_SHORT)
                    .show();

            usernameEDT.setError( "First name is required!" );
            passwordEDT.setError("passcode is required!" );

        }else{
            Log.v(TAG, "Button clicked");

            String domain = getString(R.string.domain);
            String username = usernameEDT.getText().toString();
            String email = username.concat("@").concat(domain);
            String password = passwordEDT.getText().toString();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            gotoHomepage();
                            Log.d("TAG", "signInWithEmail:success");
                        }else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            // Todo end screen loader here
                        }
                    });
        }


    }

    private void gotoHomepage() {
        Intent intent = new Intent(this, HomepageActivity.class);
        startActivity(intent);
        finishAffinity();
    }

}