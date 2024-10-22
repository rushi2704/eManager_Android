package com.example.exmanager.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.exmanager.model.Transaction;
import com.example.exmanager.utils.Constants;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainViewModel extends AndroidViewModel {

    public MutableLiveData<RealmResults<Transaction>> transactions = new MutableLiveData<>();
    public MutableLiveData<RealmResults<Transaction>> categoriesTransactions = new MutableLiveData<>();


    public MutableLiveData<Double> totalIncome = new MutableLiveData<>();
    public MutableLiveData<Double> totalExpense = new MutableLiveData<>();
    public MutableLiveData<Double> totalAmount = new MutableLiveData<>();

    Realm  realm;

    Calendar calendar;
    public MainViewModel(@NonNull Application application) {
        super(application);
        Realm.init(application);
        setupDatabase();
    }

    public void getTransactions(Calendar calendar,String type) {
        this.calendar = calendar;
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        RealmResults<Transaction> newTransactions = null;
        if(Constants.SELECTED_TAB_STATS == Constants.DAILY) {
            // Select * from transactions
            // Select * from transactions where id = 5

            // this code gives categories wise transaction
            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type",type)
                    .findAll();





        } else if(Constants.SELECTED_TAB_STATS == Constants.MONTHLY) {
            calendar.set(Calendar.DAY_OF_MONTH,0);

            Date startTime = calendar.getTime();


            calendar.add(Calendar.MONTH,1);
            Date endTime = calendar.getTime();

            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type",type)
                    .findAll();


        }


        categoriesTransactions.setValue(newTransactions);
//        RealmResults<Transaction> newTransactions = realm.where(Transaction.class)
//                .equalTo("date", calendar.getTime())
//                .findAll();

    }

    public void getTransactions(Calendar calendar) {
        this.calendar = calendar;
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        double income = 0;
        double expense = 0;
        double total = 0;
        RealmResults<Transaction> newTransactions = null;
        if(Constants.SELECTED_TAB == Constants.DAILY) {
            // Select * from transactions
            // Select * from transactions where id = 5

            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .findAll();

            income = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type", Constants.INCOME)
                    .sum("Amount")
                    .doubleValue();

            expense = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type", Constants.EXPENSE)
                    .sum("Amount")
                    .doubleValue();

            total = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .sum("Amount")
                    .doubleValue();



        } else if(Constants.SELECTED_TAB == Constants.MONTHLY) {
            calendar.set(Calendar.DAY_OF_MONTH,0);

            Date startTime = calendar.getTime();


            calendar.add(Calendar.MONTH,1);
            Date endTime = calendar.getTime();

            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .findAll();

            income = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", Constants.INCOME)
                    .sum("Amount")
                    .doubleValue();

            expense = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", Constants.EXPENSE)
                    .sum("Amount")
                    .doubleValue();

            total = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .sum("Amount")
                    .doubleValue();
        }

        totalIncome.setValue(income);
        totalExpense.setValue(expense);
        totalAmount.setValue(total);
        transactions.setValue(newTransactions);
//        RealmResults<Transaction> newTransactions = realm.where(Transaction.class)
//                .equalTo("date", calendar.getTime())
//                .findAll();

    }


    // add transaction
    public void addTransactions(Transaction transaction){
        realm.beginTransaction();
        //crud here
        realm.copyToRealmOrUpdate(transaction);

        realm.commitTransaction();
    }
    // delete transaction
    public void deleteTransaction(Transaction transaction){
        realm.beginTransaction();
        //crud here
        transaction.deleteFromRealm();
        realm.commitTransaction();
        getTransactions(calendar);

    }

//    public void addTransactions(){
//        realm.beginTransaction();
//        //crud here
//        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME,"Business","Cash","Some note here",new Date(),500,new Date().getTime()));
//        realm.copyToRealmOrUpdate(new Transaction(Constants.EXPENSE,"Investment","Bank","Some note here",new Date(),100,new Date().getTime()));
//        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME,"Rent","Cash","Some note here",new Date(),500,new Date().getTime()));
//        realm.copyToRealmOrUpdate(new Transaction(Constants.EXPENSE,"Rent","Cash","Some note here",new Date(),500,new Date().getTime()));
//        realm.commitTransaction();
//    }

    /// initilized realm database
    void setupDatabase(){

        realm = Realm.getDefaultInstance();
    }
}
