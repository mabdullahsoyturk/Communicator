package com.example.muhammet.communicator.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Muhammet on 8.12.2017.
 */

public class PreferenceUtilities {

    public static final String KEY_TIME_PERIOD = "time_period";
    public static final String KEY_CURRENCY = "currency2";
    public static final String KEY_LANGUAGE = "language";

    public static String getTimePeriod(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String timePeriod = prefs.getString(KEY_TIME_PERIOD, "All");
        return timePeriod;
    }

    public static String getCurrency(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String currency = prefs.getString(KEY_CURRENCY, "Turkish_Lira");
        return currency;
    }

    public static String getLanguage(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String language = prefs.getString(KEY_LANGUAGE, "English");
        return language;
    }

}
