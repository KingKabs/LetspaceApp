package com.sp.letspace.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sp.letspace.R;
import com.sp.letspace.models.Transaction;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactions;
    private Context context;

    // Constructor
    public TransactionAdapter(Context context, List<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        // Format amount with commas and decimals
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        String formattedAmount = formatter.format(transaction.getAmount());

        if ("invoice".equalsIgnoreCase(transaction.getCategory())) {
            holder.txtType.setText("Invoice");
            holder.txtType.setTextColor(ContextCompat.getColor(context, R.color.blue));

            holder.txtAmount.setText("- KSh " + formattedAmount);
            holder.txtAmount.setTextColor(ContextCompat.getColor(context, R.color.red));

        } else if ("payment".equalsIgnoreCase(transaction.getCategory())) {
            holder.txtType.setText("Payment");
            holder.txtType.setTextColor(ContextCompat.getColor(context, R.color.green));

            holder.txtAmount.setText("+ KSh " + formattedAmount);
            holder.txtAmount.setTextColor(ContextCompat.getColor(context, R.color.green));

        } else {
            holder.txtType.setText(transaction.getCategory());
            holder.txtType.setTextColor(ContextCompat.getColor(context, R.color.black));

            holder.txtAmount.setText("KSh " + formattedAmount);
            holder.txtAmount.setTextColor(ContextCompat.getColor(context, R.color.black));
        }

        String formattedBalance = formatter.format(transaction.getBalanceAfter());
        holder.txtBalanceAfter.setText("Balance After: KSh " + formattedBalance);
        holder.txtDate.setText(transaction.getDate());
        holder.txtComments.setText(transaction.getComments());
    }


    @Override
    public int getItemCount() {
        return transactions.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView txtType, txtAmount,txtBalanceAfter, txtDate, txtComments;

        TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            txtType = itemView.findViewById(R.id.txtType);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtBalanceAfter = itemView.findViewById(R.id.txtBalanceAfter);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtComments = itemView.findViewById(R.id.txtComments);
        }
    }
}
