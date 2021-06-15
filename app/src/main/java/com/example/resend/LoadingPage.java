package com.example.resend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.resend.models.firestore.FireStoreUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.List;

public class LoadingPage extends AppCompatActivity {

    TextView login, signUp;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private final String TAG = "APP_TEST";
    private Gson gson;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading_page);

        gson = new Gson();
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (currentUser != null) {
            gotoHomepage();
        }

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

    private void gotoHomepage() {
        Intent intent = new Intent(this, HomepageActivity.class);
        FirebaseUser fsUser = firebaseAuth.getCurrentUser();
        if (fsUser != null) {
            String uuid = fsUser.getUid();
            Log.v(TAG, "uuid: " + uuid);
            DocumentReference query = db.collection("Users").document(uuid);
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot res = task.getResult();

                    if (res != null) {
                        FireStoreUser user = res.toObject(FireStoreUser.class);
                        String userKey = getString(R.string.user_key);
                        String userJson = gson.toJson(user);

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(userKey, userJson);
                        editor.apply();

                        startActivity(intent);
                        finishAffinity();
                    } else {
                        Log.v(TAG, "Error getting documents: ", task.getException());
                    }
                } else {
                    Log.v(TAG, "Error getting documents: ", task.getException());
                }
            });
        }
    }
}