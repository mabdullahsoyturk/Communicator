package com.example.muhammet.communicator.tasks;

import android.content.ContentResolver;
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
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class AddBuyMeTask extends AsyncTask<String, Void, String> {

    Context mContext;
    private String name;
    private String description;
    private String facebook_id;
    private String house_id;

    public AddBuyMeTask(Context context, String name, String description, String facebook_id, String house_id) throws MalformedURLException {
        mContext = context;
        this.name = name;
        this.description = description;
        this.facebook_id = facebook_id;
        this.house_id = house_id;
    }

    @Override
    protected String doInBackground(String... strings) {

        HttpURLConnection urlConnection   = null;
        BufferedReader reader          = null;
        String 		      resultJsonStr = null;

        try {
            URL communicatorURL = new URL(strings[0]);
            urlConnection  = (HttpURLConnection) communicatorURL.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            JSONObject jsonParam = addBuyMeToSqlite();

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
                    resultJsonStr = buffer.toString();
                }
            }

            Log.i("RESULT", resultJsonStr);
        } catch (IOException e) {
            Log.e("MainActivity", "Error ", e);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        String success = "";
        try {
            JSONObject resultJson  = new JSONObject(resultJsonStr);
            success = resultJson.getString("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return success;
    }


    public JSONObject addBuyMeToSqlite() throws JSONException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        java.util.Date date = new java.util.Date();
        String formattedDate = dateFormat.format(date);

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("facebook_id", facebook_id);
        contentValues.put("house_id", house_id);
        contentValues.put("created_time", formattedDate);

        Uri uri = mContext.getContentResolver().insert(CommunicatorContract.BuyMeEntry.CONTENT_URI, contentValues);

        long buyMeId;

        Cursor cursor = mContext.getContentResolver().query(uri,
                null,
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            int buyMeIndex = cursor.getColumnIndex(CommunicatorContract.BuyMeEntry._ID);
            buyMeId = cursor.getLong(buyMeIndex);
        }else{
            buyMeId = ContentUris.parseId(uri);
        }

        cursor.close();

        JSONObject jsonParam = new JSONObject();
        jsonParam.put("id", buyMeId);
        jsonParam.put("name", name);
        jsonParam.put("description", description);
        jsonParam.put("facebook_id", facebook_id);
        jsonParam.put("house_id", house_id);
        jsonParam.put("created_time", formattedDate);

        return jsonParam;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Intent intent = new Intent(mContext,BaseActivity.class);
        intent.putExtra("facebook_id", facebook_id);
        intent.putExtra("house_id", house_id);
        mContext.startActivity(intent);
    }
}
