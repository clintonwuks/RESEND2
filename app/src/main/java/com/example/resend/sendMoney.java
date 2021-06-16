package com.example.resend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.resend.models.firestore.FireStoreUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class sendMoney extends AppCompatActivity {

    private EditText amountTV;
    private Button sendMoneyBtn;

    private String recipientId;
    private FireStoreUser recipient;
    private FireStoreUser user;

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private final String TAG = "APP_TEST";

    SharedPreferences preferences;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);

        recipientId = getIntent().getStringExtra("recipientId");
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        gson = new Gson();

        initElements();
    }

    public void initElements() {
        amountTV = findViewById(R.id.amount);
        sendMoneyBtn = findViewById(R.id.sendMoneyBtn);
        sendMoneyBtn.setOnClickListener(v -> getRecipient());
    }

    private void getRecipient() {
        DocumentReference ref = db.collection("Users").document(recipientId);
        ref.get().addOnSuccessListener(task -> {
            recipient = task.toObject(FireStoreUser.class);

            if (recipient != null) {
                debitUser();
            } else Log.v(TAG, "User not found");
        });
    }

    private void debitUser() {
        FirebaseUser fbUser = firebaseAuth.getCurrentUser();
        Double amount = Double.parseDouble(amountTV.getText().toString());

        if (fbUser != null) {
            String uid = fbUser.getUid();
            DocumentReference ref = db.collection("Users").document(uid);
            ref.get().addOnSuccessListener(task -> {
                user = task.toObject(FireStoreUser.class);




                if (user != null) {
                    if (user.wallet >= amount) {
                        reSend(user, amount, uid);
//                        if(reSend(user, amount));{
//                        user.wallet -= amount;
//                        ref.update("wallet", user.wallet)
//                                .addOnSuccessListener(task2 -> {
//                                    //reSend(amount);
//                                    creditRecipient(amount);
//                                    updateUser(user);
//
//                                });

                    } else
                    {Log.v(TAG, "Insufficient Funds");
                        Toast.makeText(sendMoney.this, "Insufficient Funds", Toast.LENGTH_SHORT).show();}
                } else Log.v(TAG, "User not found");
            });
        }
    }

    private void reSend(FireStoreUser user, double amount, String uid) {
         boolean val ;
       // String uid = fbUser.getUid();
        //RESEND DIALOG BOX
        // we need a builder to create the dialog for us
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
// set the title and the message to be displayed on the dialog
        builder.setTitle("Resend Dialog");
        DocumentReference ref = db.collection("Users").document(uid);
        DocumentReference ref2 = db.collection("Users").document(recipientId);
        ref2.get().addOnSuccessListener(task -> {
            recipient = task.toObject(FireStoreUser.class);

            if (recipient != null) {
                AlertDialog.Builder builder1 = builder.setMessage("You have sent £" + amount +" to "+ recipient.fullName);
               builder1.create();
               builder1.show();

            } else Log.v(TAG, "User not found");
        });

// add in a positive button here

        builder.setNegativeButton("Revert", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //cancel and go back to send money activity
                dialog.cancel();
                goToHomepage();
            }
        });
// add in a negative button here
        builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //continue send
                user.wallet -= amount;
                ref.update("wallet", user.wallet)
                        .addOnSuccessListener(task2 -> {
                            //reSend(amount);
                            creditRecipient(amount);
                            updateUser(user);

                        });

            }
        });
// create the dialog and display it
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void creditRecipient(Double amount) {
        DocumentReference ref = db.collection("Users").document(recipientId);
        ref.get().addOnSuccessListener(task -> {
            recipient = task.toObject(FireStoreUser.class);

            if (recipient != null) {
                recipient.wallet += amount;
                ref.update("wallet", recipient.wallet)
                        .addOnSuccessListener(task2 -> {
                            goToHomepage();
                        });
            } else Log.v(TAG, "User not found");
        });
    }

    private void goToHomepage() {
        // todo end loader
        Intent intent = new Intent(this, HomepageActivity.class);
        startActivity(intent);
    }

    private void updateUser(FireStoreUser user) {
        String userKey = getString(R.string.user_key);
        String userJson = gson.toJson(user);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(userKey, userJson);
        editor.apply();
    }
}