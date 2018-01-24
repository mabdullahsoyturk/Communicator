package com.example.muhammet.communicator.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.muhammet.communicator.utilities.NetworkUtilities;
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

public class AddNewHouseTask extends AsyncTask<String, Void, String> {

    Context mContext;
    private String facebook_id;
    private String house_name;

    public AddNewHouseTask(Context context, String facebook_id, String house_name) throws MalformedURLException {
        mContext = context;
        this.facebook_id = facebook_id;
        this.house_name = house_name;
    }

    @Override
    protected String doInBackground(String... strings) {
        String house_id = "";

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

            JSONObject jsonParam = SQLiteUtils.addHouseToLocal(mContext, house_name, facebook_id);
            house_id = jsonParam.getString("id");

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
        String house_id_server = "";

        JSONObject resultJson = null;
        try {
            resultJson  = new JSONObject(resultJsonStr);
            success = resultJson.getString("success");
            JSONObject jsonObject = resultJson.getJSONObject("data");
            house_id_server = jsonObject.getString("_id");

        } catch (JSONException e) {e.printStackTrace();}

        SQLiteUtils.updateHouse(mContext, house_id, house_id_server);

        UpdateUserTask updateUserTask = new UpdateUserTask(mContext, facebook_id, house_id, house_id_server);
        updateUserTask.execute(NetworkUtilities.buildWithFacebookId(facebook_id));

        return success;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
