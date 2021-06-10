package com.example.resend;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.resend.models.firestore.FireStoreUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class Account extends Fragment {
    private TextView amountTV;
    private TextView fullNameTV;
    private TextView usernameTV;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private final String TAG = "APP_TEST";

    private FireStoreUser user;

    public Account() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        initElements();
    }

    private void initElements() {
        if (getActivity() != null) {
            amountTV = getActivity().findViewById(R.id.balance);
            fullNameTV = getActivity().findViewById(R.id.fullName);
            usernameTV = getActivity().findViewById(R.id.acct_Number);
            Log.v(TAG, "Clicked Add money" + amountTV + fullNameTV);
            fetchUser();
        }
    }


    private void fetchUser() {
        FirebaseUser fsUser = firebaseAuth.getCurrentUser();
        if (fsUser != null) {
            String uuid = fsUser.getUid();
            Query query = db.collection("Users").whereEqualTo("uuid", uuid);
            query.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    QuerySnapshot res = task.getResult();

                    if (res != null && !res.isEmpty()) {
                        DocumentSnapshot userSnapshot = res.getDocuments().get(0);
                        String documentId = userSnapshot.getId();
                        FireStoreUser user = userSnapshot.toObject(FireStoreUser.class);
                        if(user != null) this.user = user;
                        setUserDetails();
                    }
                } else {
                    Log.v(TAG, "Error getting documents: ", task.getException());
                }
            });
        }
    }

    private void setUserDetails() {
        amountTV.setText(getString(R.string.balance, user.wallet));
        fullNameTV.setText(getString(R.string.fullName, user.fullName));
        usernameTV.setText(getString(R.string.acct_number, user.username));
    }
}