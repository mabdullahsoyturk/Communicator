package com.example.muhammet.communicator.tasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.muhammet.communicator.activities.BaseActivity;
import com.example.muhammet.communicator.utilities.SQLiteUtils;

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

public class AddPaymentTask extends AsyncTask<String, Void, String> {

    Context mContext;
    private String facebook_id;
    private String house_id_server;
    private double how_much;
    private String to;

    public AddPaymentTask(Context context, String facebook_id, double how_much, String to, String house_id_server) throws MalformedURLException {
        mContext = context;
        this.facebook_id = facebook_id;
        this.how_much = how_much;
        this.to = to;
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

            SQLiteUtils.addPayment(mContext, facebook_id, how_much, to);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("facebook_id", facebook_id);
            jsonParam.put("how_much", how_much);
            jsonParam.put("to", to);

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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Intent intent = new Intent(mContext, BaseActivity.class);
        intent.putExtra("facebook_id", facebook_id);
        intent.putExtra("house_id_server", house_id_server);
        mContext.startActivity(intent);
    }
}
