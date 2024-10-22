package com.example.exmanager.views.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.exmanager.adapters.TransactionsAdapter;
import com.example.exmanager.model.Transaction;
import com.example.exmanager.utils.Constants;
import com.example.exmanager.utils.Helper;
import com.example.exmanager.viewmodel.MainViewModel;
import com.example.exmanager.views.fragments.AddTransactionFragment;
import com.example.exmanager.R;
import com.example.exmanager.databinding.ActivityMainBinding;
import com.example.exmanager.views.fragments.StatsFragment;
import com.example.exmanager.views.fragments.TransactionsFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mainViewModel=new ViewModelProvider(this).get(MainViewModel.class);


        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("Transaction");

        Constants.setCategories();
        // this method returns claender object whose calender field have been initialized with current date time
        calendar= Calendar.getInstance();


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new TransactionsFragment());
        transaction.commit();

// after click on stats button StatsFragment will open
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if(item.getItemId() ==  R.id.transactions){
                    getSupportFragmentManager().popBackStack();
                }else if(item.getItemId()==R.id.stats){
                    transaction.replace(R.id.content, new StatsFragment());
                    // after cliking device back button will redirect to transaction fragment
                    transaction.addToBackStack(null);
                }
                transaction.commit();
                return true;
            }
        });
    }


    public void getTransaction(){
        mainViewModel.getTransactions(calendar);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}