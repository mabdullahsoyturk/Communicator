package com.example.muhammet.communicator.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.muhammet.communicator.listeners.AsyncTaskFinishedObserver;
import com.example.muhammet.communicator.data.CommunicatorContract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeleteSpendingTask extends AsyncTask<String, Void, String> {

    AsyncTaskFinishedObserver observer;
    Context mContext;
    String facebook_id;
    String house_id;
    String spending_id;

    public DeleteSpendingTask(Context mContext, String facebook_id, String house_id, String spending_id, AsyncTaskFinishedObserver observer) {
        this.observer = observer;
        this.mContext = mContext;
        this.facebook_id = facebook_id;
        this.house_id = house_id;
        this.spending_id = spending_id;
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

            Uri uri = CommunicatorContract.SpendingEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(spending_id).build();

            Log.i("uri", uri.toString());

            mContext.getContentResolver().delete(
                    uri,
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

        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        observer.isFinished(s);
    }

}
