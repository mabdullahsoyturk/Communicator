package com.example.muhammet.communicator.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.muhammet.communicator.adapters.BuyMeAdapter;
import com.example.muhammet.communicator.models.BuyMe;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchBuyMeTask extends AsyncTask<String, Void, BuyMe[]> {

    Context mContext;
    BuyMeAdapter buyMeAdapter;
    
    public FetchBuyMeTask(Context context, BuyMeAdapter buyMeAdapter) throws MalformedURLException {
        mContext = context;
        this.buyMeAdapter = buyMeAdapter;
    }

    @Override
    protected BuyMe[] doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String communicatorJsonStr = null;

        try {
            URL communicatorURL = new URL(strings[0]);
            urlConnection = (HttpURLConnection) communicatorURL.openConnection();
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
                    communicatorJsonStr = buffer.toString();
                }
            }

            Log.i("communicatorJsonStr", communicatorJsonStr);
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
            return getBuyMesDataFromJson(communicatorJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        BuyMe[] temp = new BuyMe[0];
        return temp;
    }

    private BuyMe[] getBuyMesDataFromJson(String communicatorJsonStr)
            throws JSONException {

        JSONArray memberJson = new JSONArray(communicatorJsonStr);

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
