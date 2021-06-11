package com.example.resend;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resend.models.firestore.FireStoreUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Friends extends Fragment {
    private RecyclerView lv_mainlist;
    private SearchView searchView;
    private ArrayList<FireStoreUser> users;
    private CustomArrayAdapter customArrayAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private final String TAG = "APP_TEST";

    private FireStoreUser user;

    public Friends() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        users = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        initElements();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                findUser(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void initElements() {
        if (getActivity() != null) {
            lv_mainlist = getActivity().findViewById(R.id.lv_mainlist);
            searchView = getActivity().findViewById(R.id.search_view);
            RecyclerView.LayoutManager lManager = new LinearLayoutManager(requireActivity());

            // create an array adapter for al_strings and set it on the listview
            customArrayAdapter = new CustomArrayAdapter(requireContext(), users);
            lv_mainlist.setLayoutManager(lManager);
            lv_mainlist.setAdapter(customArrayAdapter);
            //fetchUser();
        }
    }

    private void findUser(String username) {
        Log.v(TAG, username);
        Query query = db.collection("Users").whereGreaterThanOrEqualTo("username", username);
        query.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                QuerySnapshot res = task.getResult();

                if (res != null && !res.isEmpty()) {
                    List<FireStoreUser> users = res.toObjects(FireStoreUser.class);
                    this.users.clear();
                    this.users.addAll(users);
                    customArrayAdapter.notifyDataSetChanged();
                    for (FireStoreUser u : users) {
                        Log.v(TAG, u.toString());
                    }
                }
            } else {
                Log.v(TAG, "Error getting documents: ", task.getException());
            }
        });
    }
}