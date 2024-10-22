package com.example.exmanager.views.fragments;

import static com.example.exmanager.utils.Constants.SELECTED_STATS_TYPE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exmanager.R;
import com.example.exmanager.adapters.TransactionsAdapter;
import com.example.exmanager.databinding.FragmentTransactionFragmentsBinding;
import com.example.exmanager.model.Transaction;
import com.example.exmanager.utils.Constants;
import com.example.exmanager.utils.Helper;
import com.example.exmanager.viewmodel.MainViewModel;
import com.example.exmanager.views.activities.MainActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

import io.realm.RealmResults;


public class TransactionsFragment extends Fragment {



    public TransactionsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentTransactionFragmentsBinding binding;


    Calendar calendar;

    /*
     * 0== daily
     * 1==monthly
     * 2==calender
     * 3==summary
     * 4== notes
     * */
    // int selectedTab =0;


    public MainViewModel mainViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentTransactionFragmentsBinding.inflate(inflater);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        // this method returns claender object whose calender field have been initialized with current date time
        calendar= Calendar.getInstance();
        updateDate();

        binding.nextDateBtn.setOnClickListener(c->{
            if(Constants.SELECTED_TAB==Constants.DAILY){
                calendar.add(Calendar.DATE,1);
            }else if(Constants.SELECTED_TAB==Constants.MONTHLY) {
                calendar.add(Calendar.MONTH,1);
            }
            updateDate();
        });

        binding.previousDateBtn .setOnClickListener(c->{
            if(Constants.SELECTED_TAB==Constants.DAILY){
                calendar.add(Calendar.DATE,-1);
            }else if(Constants.SELECTED_TAB==Constants.MONTHLY){
                calendar.add(Calendar.MONTH,-1);
            }
            updateDate();
        });

        binding.floatingActionButton.setOnClickListener(c ->{
            new AddTransactionFragment().show(getParentFragmentManager(),null);
        });

        // code for changes in tablayout
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals("Monthly")){
                    Constants.SELECTED_TAB=1;
                    updateDate();
                }else  if(tab.getText().equals("Daily")){
                    Constants.SELECTED_TAB=0;
                    updateDate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //ArrayList<Transaction> transactions = new ArrayList<>();
//        transactions.add(new Transaction(Constants.INCOME,"Business","Cash","Some note here",new Date(),500,2));
//        transactions.add(new Transaction(Constants.EXPENSE,"Investment","Bank","Some note here",new Date(),100,4));
//        transactions.add(new Transaction(Constants.INCOME,"Rent","Cash","Some note here",new Date(),500,8));

        binding.transactionList.setLayoutManager(new LinearLayoutManager(getContext()));

        mainViewModel.transactions.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {
                TransactionsAdapter transactionsAdapter = new TransactionsAdapter(getActivity(), transactions);
                binding.transactionList.setAdapter(transactionsAdapter);
                if(!transactions.isEmpty()) {
                    binding.emptyState.setVisibility(View.GONE);
                }else{
                    binding.emptyState.setVisibility(View.VISIBLE);
                }
            }
        });

        mainViewModel.totalIncome.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.incomeLbl.setText(String.valueOf(aDouble));
            }
        });

        mainViewModel.totalExpense.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expenseLbl.setText(String.valueOf(aDouble));
            }
        });

        mainViewModel.totalAmount.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.totalLbl.setText(String.valueOf(aDouble));
            }
        });

        mainViewModel.getTransactions(calendar);

        return binding.getRoot();
    }

    void updateDate(){
        // SimpleDateFormat  dateFormat  = new SimpleDateFormat( "dd MMMM, yyyy ");

        if(Constants.SELECTED_TAB==Constants.DAILY){
            //calendar = Calendar.getInstance();
            binding.currentDate.setText(Helper.formatDate(calendar.getTime()));

        }else if(Constants.SELECTED_TAB==Constants.MONTHLY){
            binding.currentDate.setText(Helper.formatDateMonthly(calendar.getTime()));

        }
        mainViewModel.getTransactions(calendar);
    }
}