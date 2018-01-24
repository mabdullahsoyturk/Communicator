package com.example.muhammet.communicator.fragments;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.muhammet.communicator.R;

import java.util.Locale;

public class SettingsFragment extends PreferenceFragment implements
                                                        SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_base);
    }

    @Override
    public void onStop() {
        super.onStop();
        /* Unregister the preference change listener */
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        /* Register the preference change listener */
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Log.i("key", key);

        if(key.equals("language")){
            SharedPreferences prefs       = sharedPreferences;
            String            language    = prefs.getString("language", "en");
            Log.i("language", language);
            if(language.equals("English") || language.equals("İngilizce")){
                Locale myLocale = new Locale("en");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
            }else if(language.equals("Turkish") || language.equals("Türkçe")){
                Locale myLocale = new Locale("tr");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
            }
        }else if(key.equals("time_period")){

        }else if(key.equals("currency2")){

        }
    }
}
