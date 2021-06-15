package com.example.resend;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.resend.models.User;
import com.example.resend.models.firestore.FireStoreUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;

public class SignUpActivity extends AppCompatActivity {
    EditText dobTV;
   // EditText pickDateBtn;
    EditText fullNameEDT;
    EditText usernameEDT;
    EditText passwordEDT;
    EditText passwordVerificationEDT;
    Button registerBtn;
    ImageView backButton;
    CheckBox cb_clickme;


    LocalDate selectedDate;
    DatePickerDialog datePickerDialog;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private final String TAG = "APP_TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initElements();

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            gotoHomepage();
        }

        dobTV.setOnClickListener(v -> datePickerDialog.show());

        backButton.setOnClickListener(v -> {
            Intent i = new Intent(SignUpActivity.this, LoadingPage.class);
            startActivity(i);

        });
    }

    public void initElements() {
        fullNameEDT = findViewById(R.id.fullName);
        usernameEDT = findViewById(R.id.username);
        backButton = findViewById(R.id.backImage);
        dobTV = findViewById(R.id.dob);
        cb_clickme = findViewById(R.id.checkBox);
        passwordEDT = findViewById(R.id.password);
        passwordVerificationEDT = findViewById(R.id.password_repeat);
        registerBtn = findViewById(R.id.login_button);
        selectedDate = LocalDate.now();
        datePickerDialog = new DatePickerDialog(SignUpActivity.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    // set day of month , month and year value in the edit text
                    if (year <= 2003){
                        dobTV.setText(
                                getString(
                                        R.string.format_dob,
                                        dayOfMonth,
                                        monthOfYear + 1,
                                        year
                                )
                        );
                    }


                },
                selectedDate.getYear(),
                selectedDate.getMonthValue(),
                selectedDate.getDayOfMonth());

        registerBtn.setOnClickListener(v -> register());

        // implementation of on checked change function that is required
        cb_clickme.setOnCheckedChangeListener((button, state) -> {
            if(!state)
                registerBtn.setOnClickListener(v -> showToast());
            else
                registerBtn.setOnClickListener(v -> register());
        });
    }

    private void showToast() {
        Toast.makeText(getApplicationContext(),
                "Please Accept Terms and Conditions",
                Toast.LENGTH_SHORT)
                .show();
    }

    public void register() {
        // Todo start screen loader here
        if (verifyPassword()) {
            User user = initUser();
            String domain = getString(R.string.domain);
            String email = user.username.concat("@").concat(domain);


            firebaseAuth.createUserWithEmailAndPassword(email, user.password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            saveUserDetails(user);
                            Log.v(TAG, "Registration success");
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Log.v(TAG, "Registration failed");

                            // Todo end screen loader here
                        }
                    });
        }else {
            Log.v(TAG, "Password does not match");
            // Todo end screen loader here
        }
    }

    private Boolean verifyPassword() {
        String password = passwordEDT.getText().toString();
        String verifyPassword = passwordVerificationEDT.getText().toString();

        return password.equals(verifyPassword);
    }

    private User initUser() {
        return new User(
                fullNameEDT.getText().toString(),
                usernameEDT.getText().toString(),
                selectedDate,
                passwordEDT.getText().toString()
        );
    }

    private void saveUserDetails(User user) {
        FirebaseUser loggedInUser = firebaseAuth.getCurrentUser();
        if (loggedInUser != null) {
            String uuid = firebaseAuth.getCurrentUser().getUid();
            final FireStoreUser frUser = new FireStoreUser(
                    uuid,
                    user.fullName.toLowerCase(),
                    user.username.toLowerCase(),
                    user.dateOfBirth.toString()
            );

            db.collection("Users").document(uuid).set(frUser)
                    .addOnSuccessListener(v -> {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + uuid);
                        gotoLogin();
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error adding document", e);
                        // Todo End screen loader here
                    });
        }else{
            Log.v(TAG, "Error getting logged in user");
        }
    }

    private void gotoLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private void gotoHomepage() {
        Intent intent = new Intent(this, HomepageActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}