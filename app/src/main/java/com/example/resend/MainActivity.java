package com.example.resend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.resend.models.firestore.FireStoreUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView login, signUp;
    ImageView backButton;

    EditText usernameEDT;
    EditText passwordEDT;
    Button loginBTN;

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
        setContentView(R.layout.activity_main);
        initElements();

        gson = new Gson();
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);


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
        // pull text views
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signup_hint);
        backButton = findViewById(R.id.backImage);
        loginBTN.setOnClickListener(v -> login());
    }

    private void login() {
        // Todo Start screen loader here

        if( TextUtils.isEmpty(usernameEDT.getText()) || TextUtils.isEmpty(passwordEDT.getText()) ){
            Toast.makeText(getApplicationContext(),
                    "Check Textfields are not empty",
                    Toast.LENGTH_SHORT)
                    .show();
            Log.v(TAG, "empty field Button clicked");
            usernameEDT.setError( "First name is required!" );
            passwordEDT.setError("passcode is required!" );

        }else{
            Log.d(TAG, "Button clicked");

            String domain = getString(R.string.domain);
            String username = usernameEDT.getText().toString();
            String email = username.concat("@").concat(domain);
            String password = passwordEDT.getText().toString();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            gotoHomepage();
                            Log.d(TAG, "signInWithEmail:success");
                        }else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            // Todo end screen loader here
                            Toast.makeText(getApplicationContext(),
                                    "Incorrect Credentials",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        }


    }

    private void gotoHomepage() {
        Intent intent = new Intent(this, HomepageActivity.class);
        FirebaseUser fsUser = firebaseAuth.getCurrentUser();
        if (fsUser != null) {
            String uuid = fsUser.getUid();
            Log.v(TAG, "uuid: " + uuid);
            Query query = db.collection("Users").whereEqualTo("uuid", uuid);
            query.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    QuerySnapshot res = task.getResult();

                    if (res != null) {
                        List<FireStoreUser> users = res.toObjects(FireStoreUser.class);

                        if (users.size() > 0) {
                            FireStoreUser user = users.get(0);
                            String userId = res.getDocuments().get(0).getId();
                            String userKey = getString(R.string.user_key);
                            String userIdKey = getString(R.string.user_id_key);
                            String userJson = gson.toJson(user);

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(userKey, userJson);
                            editor.putString(userIdKey, userId);
                            editor.apply();

                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Log.v(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                } else {
                    Log.v(TAG, "Error getting documents: ", task.getException());
                }
            });
        }
    }

}