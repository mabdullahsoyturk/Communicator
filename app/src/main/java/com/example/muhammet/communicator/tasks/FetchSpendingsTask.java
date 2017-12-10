package com.example.muhammet.communicator.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.adapters.MemberAdapter;
import com.example.muhammet.communicator.adapters.SpendingAdapter;
import com.example.muhammet.communicator.models.Member;
import com.example.muhammet.communicator.models.Spending;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Muhammet on 13.11.2017.
 */

public class FetchSpendingsTask extends AsyncTask<String, Void, Spending[]> {

    Context mContext;
    SpendingAdapter spendingAdapter;
    
    public FetchSpendingsTask(Context context, SpendingAdapter spendingAdapter) throws MalformedURLException {
        mContext = context;
        this.spendingAdapter = spendingAdapter;
    }

    @Override
    protected Spending[] doInBackground(String... strings) {
        String 		      communicatorJsonStr = NetworkUtilities.getStringResponse(strings[0]);

        try {
            return getSpendingsDataFromJson(communicatorJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Spending[] temp = new Spending[0];

        return temp;
    }

    private Spending[] getSpendingsDataFromJson(String communicatorJsonStr)
            throws JSONException {

        JSONArray memberJson  = new JSONArray(communicatorJsonStr);
        JSONObject memberObject  = memberJson.getJSONObject(0);

        Spending[] spendings = new Spending[memberJson.length()];

        for(int i = 0; i < memberJson.length(); i++) {

            String name = memberJson.getJSONObject(i).getString("name");
            String created_time = memberJson.getJSONObject(i).getString("created_time");
            String cost = memberJson.getJSONObject(i).getString("cost");
            String formatted_created_time = created_time.substring(0,10);

            SharedPreferences prefs       = PreferenceManager.getDefaultSharedPreferences(mContext);
            String            currency    = prefs.getString("currency2", "Turkish_Lira");

            double costWithCurrency = 0;

            if(currency.equals("Turkish_Lira")){
                costWithCurrency = Double.parseDouble(cost);
            }else if(currency.equals("Dollar")){
                costWithCurrency = Double.parseDouble(cost);
                costWithCurrency = costWithCurrency * 3.94;
            }else if(currency.equals("Pound")){
                costWithCurrency = Double.parseDouble(cost);
                costWithCurrency = costWithCurrency * 5.26;
            }

            Spending spending = new Spending(name, formatted_created_time, costWithCurrency, R.drawable.ic_utilities_black_24dp);

            spendings[i] = spending;
        }
        return spendings;
    }

    @Override
    protected void onPostExecute(Spending[] spendings) {
        super.onPostExecute(spendings);

    }
}