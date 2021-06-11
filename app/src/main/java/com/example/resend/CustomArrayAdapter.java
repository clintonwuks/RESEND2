package com.example.resend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resend.models.firestore.FireStoreUser;

import java.util.ArrayList;
import java.util.List;

public class CustomArrayAdapter extends RecyclerView.Adapter<CustomArrayAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FireStoreUser> users;

    public CustomArrayAdapter(Context context, ArrayList<FireStoreUser> users) {
        this.context = context;
        this.users = users;
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
        holder.bindData(context, users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView fullName;
        private final TextView username;
        private final TextView frame;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            fullName = (TextView) view.findViewById(R.id.fullName);
            username = (TextView) view.findViewById(R.id.username);
            frame = (TextView) view.findViewById(R.id.acr);
        }

        public void bindData(Context context, FireStoreUser user) {
            fullName.setText(user.fullName);
            username.setText(user.username);
            frame.setText(user.getUserAcronym());
        }
    }

}




