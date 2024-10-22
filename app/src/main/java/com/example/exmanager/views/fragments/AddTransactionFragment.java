package com.example.exmanager.views.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.exmanager.R;

import com.example.exmanager.adapters.AccountsAdapter;
import com.example.exmanager.adapters.CategoryAdapter;
import com.example.exmanager.databinding.FragmentAddTransactionBinding;
import com.example.exmanager.databinding.ListDailogBinding;
import com.example.exmanager.model.Account;
import com.example.exmanager.model.Category;
import com.example.exmanager.model.Transaction;
import com.example.exmanager.utils.Constants;
import com.example.exmanager.utils.Helper;
import com.example.exmanager.views.activities.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

// used bottom sheet dialog fragment
public class AddTransactionFragment extends BottomSheetDialogFragment {



    public AddTransactionFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    // this class generated class for xml layout file fragment_add_tranaction.xml
    FragmentAddTransactionBinding binding;
    Transaction transaction;

    boolean isIncomeSelected = false;
    boolean isExpenseSelected = false;

    int isSelected=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //The purpose of this line is to initialize the ViewBinding for the Fragment. Once initialized,
        // you can use binding to access and interact with the views in the layout safely and easily.
        binding = FragmentAddTransactionBinding.inflate(inflater);

        transaction = new Transaction();

        // to make income btn green after click
        binding.incomeBtn.setOnClickListener(view ->{
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.expenseBtn.setBackground((getContext().getDrawable(R.drawable.default_selector)));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.greenColor));

            transaction.setType(Constants.INCOME);

            isIncomeSelected=true;
            isExpenseSelected=false;
        });
        // to make expense btn red after click
        binding.expenseBtn.setOnClickListener(view ->{
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setBackground((getContext().getDrawable(R.drawable.expense_selector )));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.redColor));

            transaction.setType(Constants.EXPENSE);

            isExpenseSelected=true;
            isIncomeSelected=false;

        });

        // date picker code
        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
                datePickerDialog.setOnDateSetListener((datePicker, i, i1, i2) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    calendar.set(Calendar.MONTH, datePicker.getMonth());
                    calendar.set(Calendar.YEAR, datePicker.getYear());
                // date format
                   // SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
                    String dateToShow = Helper.formatDate(calendar.getTime());
                    binding.date.setText(dateToShow);

                    transaction.setDate(calendar.getTime());
                    transaction.setId(calendar.getTime().getTime());
                });
                datePickerDialog.show();
            }
        });


        binding.category.setOnClickListener(c ->{
            // ListDialogBinding class generated for xml layout
            // inflate(inflater) return the instance to listdailogBinding that contain refernce of all views
            ListDailogBinding listDailogBinding = ListDailogBinding.inflate(inflater);
            AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
            categoryDialog.setView(listDailogBinding.getRoot());

            //list of category
//            ArrayList<Category> categories = new ArrayList<>();
//            categories.add(new Category("Salary",R.drawable.salary,R.color.category1));
//            categories.add(new Category("Business",R.drawable.business, R.color.category2));
//            categories.add(new Category("Investment",R.drawable.investment, R.color.category3));
//            categories.add(new Category("Loan",R.drawable.loan, R.color.category4));
//            categories.add(new Category("Rent",R.drawable.rent, R.color.category5));
//            categories.add(new Category("Other",R.drawable.other, R.color.category6));

            CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), Constants.categories, new CategoryAdapter.CategoryClickListner() {
                // set selected category to textview then dismiss dialog box
                @Override
                public void onCategoryClicked(Category category) {
                    binding.category.setText(category.getCategoryName());
                    transaction.setCategory(category.getCategoryName());
                    categoryDialog.dismiss();
                }
            });
            listDailogBinding.recycleView.setLayoutManager(new GridLayoutManager(getContext(),3));
            listDailogBinding.recycleView.setAdapter(categoryAdapter);

            categoryDialog.show();
        });

        binding.account.setOnClickListener(c ->{
            // ListDialogBinding class generated for xml layout
            // inflate(inflater) return the instance to listdailogBinding that contain refernce of all views
            ListDailogBinding dailogBinding = ListDailogBinding.inflate(inflater);
            AlertDialog accountsDialog = new AlertDialog.Builder(getContext()).create();
            accountsDialog.setView(dailogBinding.getRoot());


            ArrayList<Account> accounts =new ArrayList<>();
            accounts.add(new Account(0, "Cash"));
            accounts.add(new Account(0, "Bank"));
            accounts.add(new Account(0, "PhonePay"));
            accounts.add(new Account(0 , "Online"));
            accounts.add(new Account(0 , "Other"));


            AccountsAdapter adapter = new AccountsAdapter(getContext(), accounts, new AccountsAdapter.AccountsClickListner() {
                @Override
                public void onAccountSelected(Account account) {
                    binding.account.setText(account.getAccountName());
                    transaction.setAccount(account.getAccountName());
                    accountsDialog.dismiss();
                }
            });

            dailogBinding.recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
            dailogBinding.recycleView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
            dailogBinding.recycleView.setAdapter(adapter);

            accountsDialog.show();

        });

        // save transaction button event //  getting amount and note set into new transactions
        binding.transactionBtn.setOnClickListener(c->{

            if(!isIncomeSelected && !isExpenseSelected){
                // Show error message if neither button is selected
                Toast.makeText(getContext(), "Please select either Income or Expense", Toast.LENGTH_SHORT).show();
                //Snackbar.make(binding.getRoot(), "Please select either Income or Expense", Snackbar.LENGTH_SHORT).show();

            } else if (binding.date.getText().toString().isEmpty()) {
                // Show an error message or Toast if category is not selected
                Toast.makeText(getContext(), "Please select a date", Toast.LENGTH_SHORT).show();
            }
            else if (binding.amount.getText().toString().isEmpty()) {
                // Show an error message or Toast if category is not selected
                Toast.makeText(getContext(), "Please select a amount", Toast.LENGTH_SHORT).show();
            }else if (binding.category.getText().toString().isEmpty()) {
                // Show an error message or Toast if category is not selected
                Toast.makeText(getContext(), "Please select a category", Toast.LENGTH_SHORT).show();
            }
            else if (binding.account.getText().toString().isEmpty()) {
                // Show an error message or Toast if category is not selected
                Toast.makeText(getContext(), "Please select a account", Toast.LENGTH_SHORT).show();
            }

            else {
                double amount = Double.parseDouble(binding.amount.getText().toString());
                String note = binding.note.getText().toString();

                if (transaction.getType().equals(Constants.EXPENSE)) {
                    transaction.setAmount(amount * -1);

                } else {
                    transaction.setAmount(amount);

                }
                transaction.setNote(note);

                ((MainActivity) getActivity()).mainViewModel.addTransactions(transaction);
                ((MainActivity) getActivity()).getTransaction();
                dismiss();
            }
            });

        return binding.getRoot();
    }
}