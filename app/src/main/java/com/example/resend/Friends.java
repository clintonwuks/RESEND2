package com.example.resend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Friends extends Fragment {
    private RecyclerView lv_mainlist;
    private SearchView searchView;
    private ArrayList<FireStoreUser> users;
    private FriendsListAdapter friendsListAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private final String TAG = "APP_TEST";
    private Gson gson;
    SharedPreferences preferences;

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
        gson = new Gson();
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
            preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            lv_mainlist = getActivity().findViewById(R.id.lv_mainlist);
            searchView = getActivity().findViewById(R.id.search_view);
            RecyclerView.LayoutManager lManager = new LinearLayoutManager(requireActivity());
            FireStoreUser user = fetchUser();

            // create an array adapter for al_strings and set it on the listview
            if (user != null) {
                List<String> friends = user.friends != null ? user.friends : new ArrayList<>();
                List<String> request = user.friendRequest != null ? user.friendRequest : new ArrayList<>();
                List<String> sentRequest = user.sentFriendRequest != null ? user.sentFriendRequest : new ArrayList<>();

                Log.d(TAG, "friends: " + friends.toString());
                Log.d(TAG, "request: " + request.toString());
                Log.d(TAG, "pending request: " + sentRequest.toString());

                friendsListAdapter = new FriendsListAdapter(
                        requireContext(),
                        users,
                        friends,
                        request,
                        sentRequest
                );
                lv_mainlist.setLayoutManager(lManager);
                lv_mainlist.setAdapter(friendsListAdapter);
            }
            //fetchUser();
        }
    }

    private void findUser(String username) {
        Log.v(TAG, username);
        FireStoreUser user = fetchUser();

        if (user != null) {
            Query query = db.collection("Users")
                    .whereEqualTo("username", username.toLowerCase())
                    .whereNotEqualTo("username", user.username);

            query.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    QuerySnapshot res = task.getResult();
                    List<FireStoreUser> users =
                            (res != null && !res.isEmpty())
                                    ? res.toObjects(FireStoreUser.class)
                                    : new ArrayList<>();

                    this.users.clear();
                    this.users.addAll(users);
                    friendsListAdapter.notifyDataSetChanged();

                    for (FireStoreUser u : users) {
                        Log.v(TAG, u.toString());
                    }
                } else {
                    Log.v(TAG, "Error getting documents: ", task.getException());
                }
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