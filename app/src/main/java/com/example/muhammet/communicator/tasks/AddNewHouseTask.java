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

import javax.security.auth.login.LoginException;

public class AddNewHouseTask extends AsyncTask<String, Void, String> {

    Context mContext;
    private String facebook_id;
    private String house_name;
    private long house_id;

    public AddNewHouseTask(Context context, String facebook_id, String house_name) throws MalformedURLException {
        mContext = context;
        this.facebook_id = facebook_id;
        this.house_name = house_name;
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

            JSONObject jsonParam = addMembersToSqlite();

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

        return resultJsonStr;
    }

    public JSONObject addMembersToSqlite() throws JSONException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        java.util.Date date = new java.util.Date();
        String formattedDate = dateFormat.format(date);

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", house_name);
        contentValues.put("facebook_id", facebook_id);
        contentValues.put("created_time", formattedDate);

        Uri uri = mContext.getContentResolver().insert(CommunicatorContract.HouseEntry.CONTENT_URI, contentValues);

        Cursor cursor = mContext.getContentResolver().query(uri,
                null,
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            int houseIndex = cursor.getColumnIndex(CommunicatorContract.HouseEntry._ID);
            house_id = cursor.getLong(houseIndex);
            String face = cursor.getString(cursor.getColumnIndex(CommunicatorContract.HouseEntry.COLUMN_FACEBOOK_ID));
            Log.i("face", face);
        }else{
            house_id = ContentUris.parseId(uri);
        }

        cursor.close();

        JSONObject jsonParam = new JSONObject();
        jsonParam.put("id",house_id);
        jsonParam.put("name", house_name);
        jsonParam.put("facebook_id", facebook_id);
        jsonParam.put("created_time", formattedDate);

        return jsonParam;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        String success = "";

        JSONObject resultJson = null;
        try {
            resultJson  = new JSONObject(s);
            success = resultJson.getString("success");
        } catch (JSONException e) {e.printStackTrace();}

        if(success.equals("true")){
            Intent intent = new Intent(mContext, BaseActivity.class);
            intent.putExtra("facebook_id", facebook_id);
            intent.putExtra("house_id", String.valueOf(house_id));
            mContext.startActivity(intent);
        }
    }
}
