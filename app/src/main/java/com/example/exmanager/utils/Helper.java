package com.example.exmanager.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helper {

    public static String formatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
        return dateFormat.format(date);
    }

    public static String formatDateMonthly(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, yyyy");
        return dateFormat.format(date);
    }





}
