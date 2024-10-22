package com.example.exmanager.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exmanager.R;
import com.example.exmanager.databinding.RowTransactionBinding;
import com.example.exmanager.model.Category;
import com.example.exmanager.model.Transaction;
import com.example.exmanager.utils.Constants;
import com.example.exmanager.utils.Helper;
import com.example.exmanager.views.activities.MainActivity;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>{

    Context context;
    RealmResults<Transaction> transactions;
    public TransactionsAdapter(Context context, RealmResults<Transaction> transactions){
        this.context=context;
        this.transactions=transactions;
    }




    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transaction,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction  transaction = transactions.get(position);

        holder.binding.transactionAmount.setText(String.valueOf(transaction.getAmount()));
        holder.binding.accountLabel.setText(transaction.getAccount());
        holder.binding.transactionDate.setText(Helper.formatDate(transaction.getDate()));
        holder.binding.transactionCategory.setText(transaction.getCategory());

        Category transactionCategory = Constants.getCategoryDetails(transaction.getCategory());

        holder.binding.categoryIcon.setImageResource(transactionCategory.getCategoryImage());
        holder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(transactionCategory.getCategoryColor()));

        holder.binding.accountLabel.setBackgroundTintList(context.getColorStateList( Constants.getAccountColor(transaction.getAccount())));
        if(transaction.getType().equals(Constants.INCOME)){
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.greenColor));

        }else if(transaction.getType().equals(Constants.EXPENSE)){
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.redColor));

        }

// after long press you can get popup dailog for delete transaction
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog deleteDailog = new AlertDialog.Builder(context).create();
                deleteDailog.setTitle("Delete Transaction");
                deleteDailog.setMessage("Are you sure to delete this transaction ?");
                deleteDailog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", (dialog, i) -> {
                    ((MainActivity)context).mainViewModel.deleteTransaction(transaction);
                });
                deleteDailog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", (dialog, i) -> {
                    deleteDailog.dismiss();
                });
                    deleteDailog.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder{

        RowTransactionBinding binding;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= RowTransactionBinding.bind(itemView);
        }
    }
}
