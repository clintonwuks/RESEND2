package com.example.resend;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.google.gson.Gson;

public class Account extends Fragment {
    private TextView amountTV;
    private TextView fullNameTV;
    private TextView usernameTV;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private final String TAG = "APP_TEST";
    private SharedPreferences preferences;
    private Gson gson;

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
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        gson = new Gson();
        initElements();
    }

    private void initElements() {
        if (getActivity() != null) {
            amountTV = getActivity().findViewById(R.id.balance);
            fullNameTV = getActivity().findViewById(R.id.fullName);
            usernameTV = getActivity().findViewById(R.id.acct_Number);
            Log.v(TAG, "Clicked Add money" + amountTV + fullNameTV);
            this.user = fetchUser();
            setUserDetails();
        }
    }


    private FireStoreUser fetchUser() {
        String userKey = getString(R.string.user_key);
        return gson.fromJson(
                preferences.getString(userKey, ""),
                FireStoreUser.class
        );
    }

    private void setUserDetails() {
        amountTV.setText(getString(R.string.balance, user.wallet));
        fullNameTV.setText(getString(R.string.fullName, user.fullName));
        usernameTV.setText(getString(R.string.acct_number, user.username));
    }
}