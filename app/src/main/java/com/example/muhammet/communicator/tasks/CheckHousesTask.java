package com.example.muhammet.communicator.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.example.muhammet.communicator.activities.BaseActivity;
import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CheckHousesTask extends AsyncTask<String, Void, String> {

    Context mContext;
    private String facebook_id;
    private String first_name;
    private String last_name;
    private String photo_url;
    private String house_id;
    
    public CheckHousesTask(Context context, String facebook_id, String first_name, String last_name, String photo_url) throws MalformedURLException {
        mContext = context;
        this.facebook_id = facebook_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.photo_url = photo_url;
    }

    @Override
    protected String doInBackground(String... strings) {

        HttpURLConnection urlConnection   = null;
        BufferedReader    reader          = null;
        String 		      communicatorJsonStr = null;

        try {
            URL communicatorURL = new URL(strings[0]);
            urlConnection  = (HttpURLConnection) communicatorURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

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

            Log.i("communicatorJsonStr", communicatorJsonStr);
        } catch (IOException e) {
            Log.e("MainActivity", "Error ", e);
        } finally{
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

        JSONObject jsonObject = null;
        JSONObject jsonObject1 = null;

        try {
            jsonObject = new JSONObject(communicatorJsonStr);
            String success = jsonObject.getString("success");

            if(success.equals("true")){

                jsonObject1 = jsonObject.getJSONObject("data");
                house_id = jsonObject1.getString("id");

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

                    mContext.getContentResolver().update(CommunicatorContract.UserEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(user_id)).build(), cv, null,null);
                }
                cursorForUser.close();

                AddMemberTask addMemberTask = new AddMemberTask(mContext, facebook_id, house_id);
                addMemberTask.execute(NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id, house_id) + "/members");

                Intent intent = new Intent(mContext, BaseActivity.class);
                intent.putExtra("facebook_id", facebook_id);
                intent.putExtra("house_id", house_id);
                mContext.startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return communicatorJsonStr;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}