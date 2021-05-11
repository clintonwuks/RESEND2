package com.example.resend;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.resend.models.User;
import com.example.resend.models.firestore.FireStoreUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;

public class SignUpActivity extends AppCompatActivity {
    TextView dobTV;
    Button pickDateBtn;
    TextView fullNameTV;
    TextView usernameTV;
    TextView passwordTV;
    TextView passwordVerificationTV;
    Button registerBtn;


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

        pickDateBtn.setOnClickListener(v -> datePickerDialog.show());
    }

    public void initElements() {
        fullNameTV = findViewById(R.id.fullName);
        usernameTV = findViewById(R.id.username);
        dobTV = findViewById(R.id.dob);
        pickDateBtn = findViewById(R.id.date_button);
        passwordTV = findViewById(R.id.password);
        passwordVerificationTV = findViewById(R.id.password_repeat);
        registerBtn = findViewById(R.id.register_button);
        selectedDate = LocalDate.now();
        datePickerDialog = new DatePickerDialog(SignUpActivity.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    // set day of month , month and year value in the edit text
                    dobTV.setText(
                            getString(
                                    R.string.format_dob,
                                    dayOfMonth,
                                    monthOfYear + 1,
                                    year
                            )
                    );

                },
                selectedDate.getYear(),
                selectedDate.getMonthValue(),
                selectedDate.getDayOfMonth());

        registerBtn.setOnClickListener(v -> register());
    }

    public void register() {
        if (verifyPassword()) {
            User user = initUser();
            String domain = getString(R.string.domain);
            String email = user.username.concat("@").concat(domain);


            firebaseAuth.createUserWithEmailAndPassword(email, user.password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            saveUserDetails(user);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Log.v(TAG, "Registration failed");
                        }
                    });
        }else {
            Log.v(TAG, "Password does not match");
        }
    }

    private Boolean verifyPassword() {
        String password = passwordTV.getText().toString();
        String verifyPassword = passwordVerificationTV.getText().toString();

        return password.equals(verifyPassword);
    }

    private User initUser() {
        return new User(
                fullNameTV.getText().toString(),
                usernameTV.getText().toString(),
                selectedDate,
                passwordTV.getText().toString()
        );
    }

    private void saveUserDetails(User user) {
        String uuid = firebaseAuth.getCurrentUser().getUid();
        final FireStoreUser frUser = new FireStoreUser(
                uuid,
                user.fullName,
                user.username,
                user.dateOfBirth.toString()
        );

        db.collection("Users").add(frUser)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    gotoHomepage();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);
                });
    }

    private void gotoHomepage() {
        Intent intent = new Intent(this, HomepageActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}