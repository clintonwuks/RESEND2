package com.example.resend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resend.models.firestore.FireStoreUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CustomArrayAdapter extends RecyclerView.Adapter<CustomArrayAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FireStoreUser> users;
    private List<String> friends;
    private List<String> request;
    private List<String> sentRequest;


    public CustomArrayAdapter(
            Context context,
            ArrayList<FireStoreUser> users,
            List<String> friends,
            List<String> request,
            List<String> sentRequest
    ) {
        this.context = context;
        this.users = users;
        this.friends = friends;
        this.request = request;
        this.sentRequest = sentRequest;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.friends_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(context, users.get(position), friends, request, sentRequest);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView fullName;
        private final TextView username;
        private final TextView frame;
        private final Button action;
        private View view;

        public ViewHolder(View view) {
            super(view);
            this.view=view;
            // Define click listener for the ViewHolder's View

            fullName = view.findViewById(R.id.fullName);
            username = view.findViewById(R.id.username);
            frame = view.findViewById(R.id.acr);
            action = view.findViewById(R.id.action);
        }

        public void bindData(
                Context context,
                FireStoreUser user,
                List<String> friends,
                List<String> request,
                List<String> sentRequest
        ) {
            fullName.setText(user.fullName);
            username.setText(user.username);
            frame.setText(user.getUserAcronym());

            if (friends.contains(user.uuid)) {
                action.setText(context.getString(R.string.send_money));
                action.setOnClickListener(v -> sendMoney(context, user.uuid));
            } else if (request.contains(user.uuid)) {
                action.setText(context.getString(R.string.accept_request));
                action.setOnClickListener(v -> acceptRequest(context, user.uuid));
            } else if (sentRequest.contains(user.uuid)) {
                action.setText(context.getString(R.string.pending_request));
            } else {
                action.setText(context.getString(R.string.add_friend));
                action.setOnClickListener(v -> addFriend(context, user.uuid));
            }
        }

        private void sendMoney(Context context, String userId) {
            // todo goto to the send money page (create a page with amount input and send button and link to that page here)
            Intent intent = new Intent(context, sendMoney.class);
            context.startActivity(intent);
            Log.v("APP_TEST", "Send money to " + userId);
        }

        private void acceptRequest(Context context, String userId) {
            Log.v("APP_TEST", "Accepting request from " + userId);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FireStoreUser user = fetchUser(context);

            CollectionReference ref = db.collection("Users");
            ref.document(userId).get()
                    .addOnSuccessListener(v -> {
                        FireStoreUser addUser = v.toObject(FireStoreUser.class);

                        if (addUser != null) {
                            List<String> sentFriendRequest =
                                    addUser.sentFriendRequest != null ? addUser.sentFriendRequest : new ArrayList<>();
                            List<String> friends =
                                    addUser.friends != null ? addUser.friends : new ArrayList<>();
                            sentFriendRequest.removeIf(id -> id.equals(user.uuid));
                            friends.add(user.uuid);


                            ref.document(userId)
                                    .update("sentFriendRequest", sentFriendRequest, "friends", friends)
                                    .addOnSuccessListener(v1 -> {
                                        user.friendRequest.removeIf(id -> id.equals(userId));
                                        user.friends.add(userId);
                                        db.collection("Users").document(user.uuid)
                                                .update(
                                                        "friendRequest", user.friendRequest,
                                                        "friends", user.friends
                                                )
                                                .addOnSuccessListener(v2 -> {
                                                    action.setText(context.getString(R.string.send_money));
                                                    action.setOnClickListener(v3 -> sendMoney(context, user.uuid));
                                                    updateUser(context, user);
                                                });
                                    });
                        }
                    });
        }

        private void addFriend(Context context, String userId) {
            Log.v("APP_TEST", "Adding as friend" + userId);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FireStoreUser user = fetchUser(context);

            CollectionReference ref = db.collection("Users");
            ref.document(userId).get()
                    .addOnSuccessListener(v -> {
                        FireStoreUser addUser = v.toObject(FireStoreUser.class);
                        if (addUser != null) {
                            List<String> friendRequest =
                                    addUser.friendRequest != null ? addUser.friendRequest : new ArrayList<>();
                            friendRequest.add(user.uuid);

                            ref.document(userId)
                                    .update("friendRequest", friendRequest)
                                    .addOnSuccessListener(v1 -> {
                                        user.sentFriendRequest.add(userId);
                                        db.collection("Users").document(user.uuid)
                                                .update("sentFriendRequest", user.sentFriendRequest)
                                                .addOnSuccessListener(v2 -> {
                                                    action.setText(context.getString(R.string.pending_request));
                                                    action.setOnClickListener(null);
                                                    updateUser(context, user);
                                                });
                                    });
                        }
                    });
        }

        private FireStoreUser fetchUser(Context context) {
            Gson gson = new Gson();
            String userKey = context.getString(R.string.user_key);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            
            return gson.fromJson(
                    preferences.getString(userKey, ""),
                    FireStoreUser.class
            );
        }

        private void updateUser(Context context, FireStoreUser user) {
            Gson gson = new Gson();
            String userKey = context.getString(R.string.user_key);
            String userJson = gson.toJson(user);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(userKey, userJson);
            editor.apply();
        }
    }

}




