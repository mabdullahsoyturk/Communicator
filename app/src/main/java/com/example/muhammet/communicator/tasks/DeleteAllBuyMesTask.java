package com.example.muhammet.communicator.tasks;

import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.muhammet.communicator.DeleteObserver;
import com.example.muhammet.communicator.adapters.BuyMeAdapter;
import com.example.muhammet.communicator.data.CommunicatorContract;
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

    DeleteObserver observer;
    Context mContext;
    BuyMeAdapter toBuyAdapter;
    String facebook_id;
    String house_id;
    
    public DeleteAllBuyMesTask(Context context, BuyMeAdapter buyMeAdapter, String facebook_id, String house_id, DeleteObserver observer) throws MalformedURLException {
        mContext = context;
        toBuyAdapter = buyMeAdapter;
        this.facebook_id = facebook_id;
        this.house_id = house_id;
        this.observer = observer;
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

            mContext.getContentResolver().delete(
                    CommunicatorContract.BuyMeEntry.CONTENT_URI,
                    null,
                    null);

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
        Log.i("success", success);

        return success;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        toBuyAdapter.swapCursor(null);
        observer.isFinished(s);
    }
}