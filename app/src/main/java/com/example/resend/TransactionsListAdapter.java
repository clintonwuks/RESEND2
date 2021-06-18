package com.example.resend;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resend.models.Transaction;

import java.util.List;

public class TransactionsListAdapter extends RecyclerView.Adapter<TransactionsListAdapter.ViewHolder> {

    Context c;
    List<Transaction> transactions;

    public TransactionsListAdapter(Context c, List<Transaction> transactions) {
        this.c = c;
        this.transactions = transactions;
        Log.v("APP_TEST", "Started yes in vind");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.transaction_item_layout, parent, false);
        Log.v("APP_TEST", "Started yes in vinds");
        return new TransactionsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(c, transactions.get(position));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView date;
        private final TextView type;
        private final TextView amount;

        public ViewHolder(View view) {
            super(view);

            date = view.findViewById(R.id.date);
            type = view.findViewById(R.id.type);
            amount = view.findViewById(R.id.amount);
        }

        public void bindData(Context c, Transaction transaction) {
            date.setText(transaction.date);
            type.setText(transaction.type);
            amount.setText(c.getString(R.string.transaction_amount, transaction.amount));

            if (transaction.type.equalsIgnoreCase("DEBIT")) {
                type.setTextColor(Color.RED);
            } else type.setTextColor(Color.GREEN);
        }
    }
}
