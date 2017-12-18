package com.example.muhammet.communicator.tasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.muhammet.communicator.activities.BaseActivity;

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

public class CheckUserTask extends AsyncTask<String, Void, String> {

    Context mContext;

    private String first_name;
    private String last_name;
    private String photo_url;
    private String facebook_id;

    public CheckUserTask(Context context, String first_name, String last_name,
                         String photo_url, String facebook_id) throws MalformedURLException {
        this.first_name = first_name;
        this.last_name = last_name;
        this.photo_url = photo_url;
        this.facebook_id = facebook_id;
        mContext = context;
    }

    @Override
    protected String doInBackground(String... strings) {

        HttpURLConnection urlConnection   = null;
        BufferedReader    reader          = null;
        String 		      communicatorJsonStr = null;

        try {
            URL communicatorURL = new URL(strings[0]);
            urlConnection  = (HttpURLConnection) communicatorURL.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("first_name", first_name);
            jsonParam.put("last_name", last_name);
            jsonParam.put("photo_url", photo_url);
            jsonParam.put("facebook_id", facebook_id);

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
            Log.e("MainActivity", "Error ", e);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return communicatorJsonStr;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        String success = "";

        JSONObject communicatorJson = null;
        JSONObject jsonObject1 = null;

        try {
            communicatorJson  = new JSONObject(s);
            success = communicatorJson.getString("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String houses = "12";
        String house_id = "deneme";

        if (success.equals("false")){
            try {
                jsonObject1 = communicatorJson.getJSONObject("data");
                houses      = jsonObject1.getString("houses");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(houses.length() != 2){
                house_id = houses.substring(2, houses.length()-2);
                Intent intent = new Intent(mContext, BaseActivity.class);
                intent.putExtra("facebook_id", facebook_id);
                intent.putExtra("house_id", house_id);
                mContext.startActivity(intent);
            }
        }else{
            try{
                jsonObject1 = communicatorJson.getJSONObject("data");
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
