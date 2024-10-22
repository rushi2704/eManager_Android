package com.example.exmanager.utils;

import com.example.exmanager.R;
import com.example.exmanager.model.Category;

import java.util.ArrayList;

public class Constants {
    public static String  INCOME = "INCOME";
    public static String  EXPENSE = "EXPENSE";

    public static ArrayList<Category> categories;

    public static  int DAILY = 0;
    public static  int MONTHLY = 1;
    public static  int CALENDAR = 2;
    public static  int SUMMARY = 3;
    public static  int NOTES = 4;
    public static int SELECTED_TAB=0;

    public static int SELECTED_TAB_STATS=0;
    public static String SELECTED_STATS_TYPE= Constants.INCOME;

    public static void setCategories(){
        categories = new ArrayList<>();
        categories.add(new Category("Salary", R.drawable.salary,R.color.category1));
        categories.add(new Category("Business",R.drawable.business, R.color.category2));
        categories.add(new Category("Investment",R.drawable.investment, R.color.category3));
        categories.add(new Category("Loan",R.drawable.loan, R.color.category4));
        categories.add(new Category("Rent",R.drawable.rent, R.color.category5));
        categories.add(new Category("Other",R.drawable.other, R.color.category6));
    }

    public static Category getCategoryDetails(String categoryName){
        for ( Category cat:
             categories ) {
            if(cat.getCategoryName().equals(categoryName)){
                return cat;
            }
            
        }
        return null;
    }

    public static int getAccountColor(String accountName){
        switch(accountName){
            case "Bank":
                return R.color.bank_color;
            case "Cash":
                return R.color.cash_color;
            case "PhonePay":
                return R.color.phonepay_color;
            case "Online":
                return R.color.online_color;
            default:
                return R.color.other_color;
        }
    }
}
