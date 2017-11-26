package com.example.muhammet.communicator.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.adapters.BuyMeAdapter;
import com.example.muhammet.communicator.models.BuyMe;
import com.example.muhammet.communicator.models.BuyMe;

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

public class FetchBuyMeTask extends AsyncTask<String, Void, BuyMe[]> {

    Context mContext;
    BuyMeAdapter buyMeAdapter;

    private String facebook_id = "10215415549690496";

    public FetchBuyMeTask(Context context, BuyMeAdapter buyMeAdapter) throws MalformedURLException {
        mContext = context;
        this.buyMeAdapter = buyMeAdapter;
    }

    @Override
    protected BuyMe[] doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = null;

        try {
            URL weatherURL = new URL(strings[0]);
            urlConnection = (HttpURLConnection) weatherURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() != 0) {
                    forecastJsonStr = buffer.toString();
                }
            }

            Log.i("forecastJsonStr", forecastJsonStr);
        } catch (IOException e) {
            Log.e("MainActivity", "Error ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("MainActivity", "Error closing stream", e);
                }
            }
        }

        try {
            return getBuyMesDataFromJson(forecastJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        BuyMe[] temp = new BuyMe[0];
        return temp;
    }

    private BuyMe[] getBuyMesDataFromJson(String forecastJsonStr)
            throws JSONException {

        JSONArray memberJson = new JSONArray(forecastJsonStr);
        JSONObject memberObject = memberJson.getJSONObject(0);

        BuyMe[] spendings = new BuyMe[memberJson.length()];

        for (int i = 0; i < memberJson.length(); i++) {

            String name = memberJson.getJSONObject(i).getString("name");
            String description = memberJson.getJSONObject(i).getString("description");
            BuyMe spending = new BuyMe(name,description);

            spendings[i] = spending;
        }
        return spendings;
    }

    @Override
    protected void onPostExecute(BuyMe[] spendings) {
        super.onPostExecute(spendings);

        buyMeAdapter.setBuyMeData(spendings);
    }
}
