package com.example.resend;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.resend.models.User;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {
    TextView dob;
    Button pickDateBtn;
    TextView fullNameTV;
    TextView usernameTV;
    TextView passwordTV;
    TextView passwordVerificationTV;
    Button registerBtn;
    LocalDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initElements();

        /*dob = findViewById(R.id.dob);
        pick_date = (Button) findViewById(R.id.date_button);
        final DatePickerDialog[] datePickerDialog = new DatePickerDialog[1];

        pick_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog[0] = new DatePickerDialog(SignUpActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dob.setText(
                                    getString(
                                        R.string.format_dob,
                                        dayOfMonth,
                                        monthOfYear + 1,
                                        year
                                    )
                                );

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog[0].show();
            }
        });*/

    }

    public void initElements() {
        this.fullNameTV = findViewById(R.id.fullName);
        this.usernameTV = findViewById(R.id.username);
        this.pickDateBtn = findViewById(R.id.date_button);
        this.passwordTV = findViewById(R.id.password);
        this.passwordVerificationTV = findViewById(R.id.password_repeat);
        this.registerBtn = findViewById(R.id.register_button);
        this.selectedDate = LocalDate.of(2020, 07, 23);

        registerBtn.setOnClickListener(register());
    }

    public View.OnClickListener register() {
        return v -> {
            if (verifyPassword()) {
                User user = new User(
                        fullNameTV.getText().toString(),
                        usernameTV.getText().toString(),
                        LocalDate.now(),
                        passwordTV.getText().toString()
                );

                Log.v("APP_TEST", user.toString());
            }else {
                Log.v("APP_TEST", "Password does not match");
            }
        };
    }

    public Boolean verifyPassword() {
        String password = passwordTV.getText().toString();
        String verifyPassword = passwordVerificationTV.getText().toString();

        return password.equals(verifyPassword);
    }


    public LocalDate getDate() {
        /*return LocalDate.of(
                dateOfBirthDT.getYear(),
                dateOfBirthDT.getMonth(),
                dateOfBirthDT.getDayOfMonth()
        );*/
        return null;
    }
}