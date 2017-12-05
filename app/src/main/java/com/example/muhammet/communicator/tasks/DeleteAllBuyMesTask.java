package com.example.muhammet.communicator.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.muhammet.communicator.adapters.BuyMeAdapter;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DeleteAllBuyMesTask extends AsyncTask<String, Void, String> {

    Context mContext;
    BuyMeAdapter toBuyAdapter;
    String user_id;
    String house_id;
    
    public DeleteAllBuyMesTask(Context context, BuyMeAdapter buyMeAdapter, String user_id, String house_id) throws MalformedURLException {
        mContext = context;
        toBuyAdapter = buyMeAdapter;
        this.user_id = user_id;
        this.house_id = house_id;
    }

    @Override
    protected String doInBackground(String... strings) {

        HttpURLConnection urlConnection   = null;
        BufferedReader reader          = null;
        String 		      communicatorJsonStr = null;

        try {
            URL communicatorURL = new URL(strings[0]);
            urlConnection  = (HttpURLConnection) communicatorURL.openConnection();
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoInput(true);

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer     = new StringBuffer();

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

            Log.i("RESULT", communicatorJsonStr);

        } catch (IOException e) {
            Log.e("MainActivity", "Error ", e);
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        String success = "";
        try {
            JSONArray communicatorJson  = new JSONArray(communicatorJsonStr);
            Log.i("success", success);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return success;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        FetchBuyMeTask fetchBuyMeTask = null;
        try {
            fetchBuyMeTask = new FetchBuyMeTask(mContext, toBuyAdapter);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        fetchBuyMeTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/" + user_id + "/houses/" + house_id + "/buy_mes");
    }
}