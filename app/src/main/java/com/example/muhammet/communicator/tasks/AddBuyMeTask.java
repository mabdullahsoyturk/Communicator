package com.example.muhammet.communicator.tasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.muhammet.communicator.activities.BaseActivity;
import com.example.muhammet.communicator.utilities.NotificationUtilities;

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

import static com.example.muhammet.communicator.utilities.SQLiteUtils.addBuyMeToLocal;

public class AddBuyMeTask extends AsyncTask<String, Void, String> {

    Context mContext;
    private String name;
    private String description;
    private String facebook_id;
    private String house_id_server;

    public AddBuyMeTask(Context context, String name, String description, String facebook_id, String house_id_server) throws MalformedURLException {
        mContext = context;
        this.name = name;
        this.description = description;
        this.facebook_id = facebook_id;
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
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            JSONObject jsonParam = addBuyMeToLocal(mContext, name, description, facebook_id, house_id_server);

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
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        NotificationUtilities.remindUserBecauseBuyMeAdded(mContext);

        Intent intent = new Intent(mContext,BaseActivity.class);
        intent.putExtra("facebook_id", facebook_id);
        intent.putExtra("house_id_server", house_id_server);
        mContext.startActivity(intent);
    }
}
