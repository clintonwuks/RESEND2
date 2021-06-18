package com.example.resend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resend.models.Transaction;
import com.example.resend.models.firestore.FireStoreUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Transactions extends Fragment {

    private List<Transaction> transactions;
    private SharedPreferences preferences;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private Gson gson;
    private final String TAG = "APP_TEST";

    private TransactionsListAdapter transactionsListAdapter;

    public Transactions() {}

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_transactions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transactions = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        gson = new Gson();

        RecyclerView transactionsList = requireActivity().findViewById(R.id.transactionsList);
        RecyclerView.LayoutManager lManager = new LinearLayoutManager(requireActivity());
        transactionsListAdapter = new TransactionsListAdapter(
                requireContext(),
                transactions
        );
        transactionsList.setLayoutManager(lManager);
        transactionsList.setAdapter(transactionsListAdapter);

        Button refreshTransactions = requireActivity().findViewById(R.id.refreshButton);
        refreshTransactions.setOnClickListener(v -> fetchTransactions());
        fetchTransactions();
    }

    private void fetchTransactions() {
        FireStoreUser user = fetchUser();

        if (user != null) {
            Query query =
                    db.collection("Users")
                            .document(user.uuid)
                            .collection("Transactions")
                            .orderBy("date")
                            .limit(5);

            query.get()
                    .addOnSuccessListener(task -> {
                        List<Transaction> transactions = task.toObjects(Transaction.class);
                        this.transactions.clear();
                        this.transactions.addAll(transactions);
                        transactionsListAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error fetching transactions", e);
                    });
        }
    }

    private FireStoreUser fetchUser() {
        if (getActivity() != null) {
            String userKey = getString(R.string.user_key);
            FireStoreUser user = gson.fromJson(
                    preferences.getString(userKey, ""),
                    FireStoreUser.class
            );

            if (user != null) return user; else gotoHomepage();
        }

        return null;
    }

    private void gotoHomepage() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), HomepageActivity.class);
            startActivity(intent);
            getActivity().finishAffinity();
        }
    }
}