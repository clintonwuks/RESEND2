package com.example.resend;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.resend.models.User;

import java.time.LocalDate;
import java.util.Calendar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initElements();


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
            User user = new User(
                    fullNameTV.getText().toString(),
                    usernameTV.getText().toString(),
                    selectedDate,
                    passwordTV.getText().toString()
            );

            Log.v("APP_TEST", user.toString());
        }else {
            Log.v("APP_TEST", "Password does not match");
        }
    }

    public Boolean verifyPassword() {
        String password = passwordTV.getText().toString();
        String verifyPassword = passwordVerificationTV.getText().toString();

        return password.equals(verifyPassword);
    }
}