package com.example.muhammet.communicator.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtilities {

    public static String getFormattedDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        java.util.Date date = new java.util.Date();
        String formattedDate = dateFormat.format(date);

        return formattedDate;
    }

    public static long getDateAsSeconds(String date){
        Date newDate = new Date(date);

        return newDate.getTime();
    }
}
