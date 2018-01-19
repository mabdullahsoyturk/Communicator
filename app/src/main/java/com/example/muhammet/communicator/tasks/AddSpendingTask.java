package com.example.muhammet.communicator.tasks;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.muhammet.communicator.activities.BaseActivity;
import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.utilities.DateUtilities;
import com.example.muhammet.communicator.utilities.SQLiteUtils;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddSpendingTask extends AsyncTask<String, Void, String> {

    List<String> userList;
    Context mContext;
    private String name;
    private double cost;
    private String facebook_id;
    private String house_id;

    public AddSpendingTask(Context context, String name, double cost, String facebook_id, String house_id, List<String> userList) throws MalformedURLException {
        mContext = context;
        this.name = name;
        this.cost = cost;
        this.facebook_id = facebook_id;
        this.house_id = house_id;
        this.userList = userList;
    }

    @Override
    protected String doInBackground(String... strings) {

        HttpURLConnection urlConnection   = null;
        BufferedReader reader          = null;
        String 		      communicatorJsonStr = null;

        try {
            URL communicatorURL = new URL(strings[0]);
            urlConnection  = (HttpURLConnection) communicatorURL.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            JSONObject jsonParam = SQLiteUtils.addSpendingToLocal(mContext, name, cost, facebook_id, house_id, userList);

            Log.i("JSON", jsonParam.toString());

            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
            os.writeBytes(jsonParam.toString());
            os.flush();
            os.close();

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
            Log.e("SpendingTask", "Error ", e);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        String success = "";
        try {
            JSONObject communicatorJson  = new JSONObject(communicatorJsonStr);
            success = communicatorJson.getString("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(mContext, BaseActivity.class);
        intent.putExtra("facebook_id", facebook_id);
        intent.putExtra("house_id", house_id);
        mContext.startActivity(intent);

        return success;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}