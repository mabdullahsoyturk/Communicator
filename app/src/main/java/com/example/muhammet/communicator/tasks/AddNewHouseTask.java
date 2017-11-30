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

/**
 * Created by Muhammet on 29.11.2017.
 */

public class AddNewHouseTask extends AsyncTask<String, Void, String> {

    Context mContext;
    private String user_id;
    private String house_name;

    public AddNewHouseTask(Context context, String user_id, String house_name) throws MalformedURLException {
        mContext = context;
        this.user_id = user_id;
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

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("name", house_name);

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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        String success = "";
        String house_id = "";

        JSONObject resultJson = null;
        JSONObject jsonObject = null;
        try {
            resultJson  = new JSONObject(s);
            jsonObject = resultJson.getJSONObject("data");
            house_id = jsonObject.getString("_id");
            success = resultJson.getString("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(success.equals("true")){
            Intent intent = new Intent(mContext, BaseActivity.class);
            intent.putExtra("user_id", user_id);
            intent.putExtra("house_id", house_id);
            mContext.startActivity(intent);
        }

    }
}
