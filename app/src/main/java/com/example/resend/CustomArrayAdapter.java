package com.example.resend;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resend.models.firestore.FireStoreUser;

import java.util.ArrayList;
import java.util.List;

public class CustomArrayAdapter extends RecyclerView.Adapter<CustomArrayAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FireStoreUser> users;
    private List<String> friends;
    private List<String> request;

    public CustomArrayAdapter(Context context, ArrayList<FireStoreUser> users, List<String> friends, List<String> request) {
        this.context = context;
        this.users = users;
        this.friends = friends;
        this.request = request;
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
        holder.bindData(context, users.get(position), friends, request);
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

        public ViewHolder(View view) {
            super(view);
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
                List<String> request
        ) {
            fullName.setText(user.fullName);
            username.setText(user.username);
            frame.setText(user.getUserAcronym());

            if (friends.contains(user.uuid)) {
                action.setText(context.getString(R.string.send_money));
                action.setOnClickListener(v -> sendMoney(user.uuid));
            } else if (request.contains(user.uuid)) {
                action.setText(context.getString(R.string.accept_request));
                action.setOnClickListener(v -> acceptRequest(user.uuid));
            } else {
                action.setText(context.getString(R.string.add_friend));
                action.setOnClickListener(v -> addFriend(user.uuid));
            }
        }

        private void sendMoney(String userId) {
            // todo goto to the send money page (create a page with amount input and send button and link to that page here)
            Log.v("APP_TEST", "Send money to " + userId);
        }

        private void acceptRequest(String userId) {
            Log.v("APP_TEST", "Accepting request from " + userId);
        }

        private void addFriend(String userId) {
            Log.v("APP_TEST", "Adding as friend" + userId);
        }
    }

}




