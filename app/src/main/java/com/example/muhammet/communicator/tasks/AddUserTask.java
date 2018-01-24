package com.example.muhammet.communicator.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.muhammet.communicator.utilities.SQLiteUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class AddUserTask extends AsyncTask<String, Void, Void> {

    Context mContext;

    private String first_name;
    private String last_name;
    private String photo_url;
    private String facebook_id;

    public AddUserTask(Context context, String first_name, String last_name,
                       String photo_url, String facebook_id){
        this.first_name = first_name;
        this.last_name = last_name;
        this.photo_url = photo_url;
        this.facebook_id = facebook_id;
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... strings) {
        Log.i("idInAddTask", facebook_id);

        HttpURLConnection urlConnection   = null;
        BufferedReader reader          = null;
        String 		      communicatorJsonStr = null;

        try {
            URL communicatorURL = new URL(strings[0]);
            urlConnection  = (HttpURLConnection) communicatorURL.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            JSONObject jsonParam = SQLiteUtils.addMemberToLocal(mContext, first_name,last_name,photo_url,facebook_id);

            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
            os.writeBytes(jsonParam.toString());
            os.flush();
            os.close();

            Log.i("JSON", jsonParam.toString());

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

//            Log.i("RESULT", communicatorJsonStr);

        } catch (IOException e) {
            Log.e("MainActivity", "Error ", e);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }
}
