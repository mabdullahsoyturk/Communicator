package com.example.muhammet.communicator.tasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.muhammet.communicator.activities.BaseActivity;
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

        return communicatorJsonStr;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        JSONObject jsonObject = null;
        JSONObject jsonObject1 = null;

        try {
            jsonObject = new JSONObject(s);
            String success = jsonObject.getString("success");

            jsonObject1 = jsonObject.getJSONObject("data");
            house_id = jsonObject1.getString("id");

            if(success.equals("true")){

                AddMemberTask addMemberTask = new AddMemberTask(mContext);
                addMemberTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/" + facebook_id + "/houses/" + house_id + "/members");

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
    }
}