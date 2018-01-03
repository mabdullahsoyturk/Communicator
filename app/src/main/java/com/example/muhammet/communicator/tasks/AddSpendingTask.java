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

public class AddSpendingTask extends AsyncTask<String, Void, String> {

    Context mContext;
    private String name;
    private double cost;
    private String facebook_id;
    private String house_id;

    public AddSpendingTask(Context context, String name, double cost, String facebook_id, String house_id) throws MalformedURLException {
        mContext = context;
        this.name = name;
        this.cost = cost;
        this.facebook_id = facebook_id;
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
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            JSONObject jsonParam = addSpendingToSqlite();

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

        return success;
    }

    public JSONObject addSpendingToSqlite() throws JSONException{
        String formattedDate = DateUtilities.getFormattedDate();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("cost", cost);
        contentValues.put("facebook_id", facebook_id);
        contentValues.put("house_id", house_id);
        contentValues.put("created_time", formattedDate);

        Uri uri = mContext.getContentResolver().insert(CommunicatorContract.SpendingEntry.CONTENT_URI, contentValues);

        long spendingId;

        Cursor cursor = mContext.getContentResolver().query(uri,
                null,
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(CommunicatorContract.SpendingEntry._ID);
            spendingId = cursor.getLong(idIndex);
        }else{
            spendingId = ContentUris.parseId(uri);
        }

        cursor.close();

        JSONObject jsonParam = new JSONObject();
        jsonParam.put("id", spendingId);
        jsonParam.put("name", name);
        jsonParam.put("cost", cost);
        jsonParam.put("facebook_id", facebook_id);
        jsonParam.put("house_id", house_id);
        jsonParam.put("created_time", formattedDate);

        return jsonParam;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Intent intent = new Intent(mContext, BaseActivity.class);
        intent.putExtra("facebook_id", facebook_id);
        intent.putExtra("house_id", house_id);
        mContext.startActivity(intent);
    }
}