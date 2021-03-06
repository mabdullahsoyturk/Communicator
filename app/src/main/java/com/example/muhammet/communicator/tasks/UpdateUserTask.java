package com.example.muhammet.communicator.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import java.net.URL;

public class UpdateUserTask extends AsyncTask<String, Void, String> {

    Context mContext;
    private String facebook_id;
    private String house_id;
    private String house_id_server;

    public UpdateUserTask(Context context, String facebook_id, String house_id, String house_id_server){
        mContext = context;
        this.facebook_id = facebook_id;
        this.house_id = house_id;
        this.house_id_server = house_id_server;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection urlConnection   = null;
        BufferedReader reader          = null;
        String 		      resultJsonStr = null;

        try {
            URL communicatorURL = new URL(strings[0]);
            urlConnection  = (HttpURLConnection) communicatorURL.openConnection();
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            JSONObject jsonParam = new JSONObject();

            Cursor cursorForUser = mContext.getContentResolver().query(CommunicatorContract.UserEntry.CONTENT_URI,
                    null,
                    "facebook_id=?",
                    new String[]{facebook_id},
                    null);

            if(cursorForUser.moveToFirst()){
                long user_id = cursorForUser.getLong(cursorForUser.getColumnIndex(CommunicatorContract.UserEntry._ID));
                String first_name = cursorForUser.getString(cursorForUser.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_FIRST_NAME));
                String last_name = cursorForUser.getString(cursorForUser.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_LAST_NAME));
                double balance = cursorForUser.getDouble(cursorForUser.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_BALANCE));
                String photo_url = cursorForUser.getString(cursorForUser.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_PHOTO_URL));
                int status = cursorForUser.getInt(cursorForUser.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_STATUS));
                String created = cursorForUser.getString(cursorForUser.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_CREATED_TIME));
                String fid = cursorForUser.getString(cursorForUser.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_FACEBOOK_ID));
                String hid = house_id;
                String hid_server = house_id_server;

                ContentValues cv = new ContentValues();
                cv.put("_id", user_id);
                cv.put("first_name", first_name);
                cv.put("last_name", last_name);
                cv.put("balance", balance);
                cv.put("photo_url", photo_url);
                cv.put("status", status);
                cv.put("created_time", created);
                cv.put("facebook_id", fid);
                cv.put("house_id", hid);
                cv.put("house_id_server", hid_server);

                mContext.getContentResolver().update(CommunicatorContract.UserEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(user_id)).build(), cv, null,null);
            }
            cursorForUser.close();

            jsonParam.put("house_id", house_id);
            jsonParam.put("house_id_server", house_id_server);

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

        if(success.equals("true")){
            Intent intent = new Intent(mContext, BaseActivity.class);
            intent.putExtra("facebook_id", facebook_id);
            intent.putExtra("house_id", String.valueOf(house_id));
            intent.putExtra("house_id_server", house_id_server);
            mContext.startActivity(intent);
        }

        return success;
    }
}
