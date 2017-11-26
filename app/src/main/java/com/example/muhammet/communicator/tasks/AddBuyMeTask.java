package com.example.muhammet.communicator.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
 * Created by Muhammet on 26.11.2017.
 */

public class AddBuyMeTask extends AsyncTask<String, Void, String> {

    Context mContext;
    private String name;
    private String description;

    private String facebook_id = "10215415549690496";

    public AddBuyMeTask(Context context,String name, String description) throws MalformedURLException {
        mContext = context;
        this.name = name;
        this.description = description;
    }

    @Override
    protected String doInBackground(String... strings) {

        HttpURLConnection urlConnection   = null;
        BufferedReader reader          = null;
        String 		      forecastJsonStr = null;

        try {
            URL weatherURL = new URL(strings[0]);
            urlConnection  = (HttpURLConnection) weatherURL.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("name", name);
            jsonParam.put("description", description);

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
                    forecastJsonStr = buffer.toString();
                }
            }


            Log.i("FORECAST", forecastJsonStr);

            Log.i("STATUS", String.valueOf(urlConnection.getResponseCode()));
            Log.i("MSG" , urlConnection.getResponseMessage());
            Log.i("RESPONSE", urlConnection.getContent().toString());
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
            JSONObject forecastJson  = new JSONObject(forecastJsonStr);
            success = forecastJson.getString("success");
            Log.i("success", success);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return success;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
