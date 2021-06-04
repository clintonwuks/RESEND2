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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import co.paystack.android.PaystackSdk;

public class AddMoneyActivity extends AppCompatActivity {

    private EditText amount;
    private Button addMoneyButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private final String TAG = "APP_TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);

        PaystackSdk.initialize(this);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null) {
            goToLogin();
        }

        amount = findViewById(R.id.amount);
        addMoneyButton = findViewById(R.id.creditAccountBtn);
        addMoneyButton.setOnClickListener(v -> {
            Double creditAmount = Double.parseDouble(amount.getText().toString());
            creditAccount(creditAmount);
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
                                    // todo show error
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
        Intent intent = new Intent(this, HomepageActivity.class);
        startActivity(intent);
    }
}