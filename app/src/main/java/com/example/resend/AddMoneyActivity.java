package com.example.resend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.resend.models.firestore.FireStoreUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class AddMoneyActivity extends AppCompatActivity {

    private EditText amountInput;
    private EditText cardNumberInput;
    private EditText expMonthInput;
    private EditText expYearInput;
    private EditText cvvInput;
    private Button addMoneyButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private final String TAG = "APP_TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        initElements();

        PaystackSdk.initialize(this);
        PaystackSdk.setPublicKey("pk_test_87b43cb03070ea4c4f584656222db9aa18ff7472");

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null) {
            goToLogin();
        }

        addMoneyButton.setOnClickListener(v -> {
            // todo start loader
            chargeCard();
        });
    }

    private void initElements() {
        amountInput = findViewById(R.id.amount);
        addMoneyButton = findViewById(R.id.creditAccountBtn);
        cardNumberInput = findViewById(R.id.cardNumber);
        expMonthInput = findViewById(R.id.expMonth);
        expYearInput = findViewById(R.id.expYear);
        cvvInput = findViewById(R.id.cvv);
    }

    private void chargeCard() {
        double amount = Integer.parseInt(amountInput.getText().toString());
        int paystackAmount = (int) amount * 100;

        // todo validate input not empty here

        String cardNumber = cardNumberInput.getText().toString();
        int expMonth = Integer.parseInt(expMonthInput.getText().toString());
        int expYear = Integer.parseInt(expYearInput.getText().toString());
        String cvv = cvvInput.getText().toString();

        Log.d(TAG, "card: " + amount);
        Log.d(TAG, "card: " + cardNumber);
        Log.d(TAG, "card: " + expMonth);
        Log.d(TAG, "card: " + expYear);
        Log.d(TAG, "card: " + cvv);

        Charge charge = new Charge();
        Card card = new Card(
                cardNumber,
                expMonth,
                expYear,
                cvv
        );
        charge.setAmount(paystackAmount);
        charge.setEmail("festusyuma@gmail.com");
        charge.setCard(card);

        PaystackSdk.chargeCard(this, charge, new Paystack.TransactionCallback() {

            @Override
            public void onSuccess(Transaction transaction) {
                creditAccount(amount);
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                Log.d(TAG, "beforeValidate: " + transaction.getReference());
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                Log.d(TAG, "onError: " + error.getLocalizedMessage());
                Log.d(TAG, "onError: " + error);
                // todo show error and end loader
            }
        });
    }

    private void creditAccount(Double amount) {
        String uuid = firebaseAuth.getCurrentUser().getUid();
        Query query = db.collection("Users").whereEqualTo("uuid", uuid);
        query.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                QuerySnapshot res = task.getResult();

                if (res != null && !res.isEmpty()) {
                    DocumentSnapshot userSnapshot = res.getDocuments().get(0);
                    String documentId = userSnapshot.getId();
                    FireStoreUser user = userSnapshot.toObject(FireStoreUser.class);

                    if(user != null) {
                        user.wallet += amount;
                        db.collection("Users").document(documentId).set(user)
                                .addOnSuccessListener(v -> {
                                    goToHomepage();
                                })
                                .addOnFailureListener(e -> {
                                    // todo show error and end loader
                                    Log.v(TAG, "Error getting documents: ", e);
                                });
                    }
                }
            } else {
                Log.v(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    private void goToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private void goToHomepage() {
        // todo end loader
        Intent intent = new Intent(this, HomepageActivity.class);
        startActivity(intent);
    }
}