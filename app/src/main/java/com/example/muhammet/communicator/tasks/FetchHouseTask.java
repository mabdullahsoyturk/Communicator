package com.example.muhammet.communicator.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.muhammet.communicator.adapters.MemberAdapter;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchHouseTask extends AsyncTask<String, Void, String> {

    Context mContext;

    private TextView house_name;
    private String facebook_id;
    private String name;

    public FetchHouseTask(Context context, TextView house_name, String facebook_id) throws MalformedURLException {
        mContext = context;
        this.house_name = house_name;
        this.facebook_id = facebook_id;
    }

    @Override
    protected String doInBackground(String... strings) {

        String 		      communicatorJsonStr = NetworkUtilities.getStringResponse(strings[0]);
        return communicatorJsonStr;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        JSONArray jsonArray = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(s);
            jsonArray = jsonObject.getJSONArray("data");
            Log.i("data", jsonArray.toString());
            name = jsonArray.getJSONObject(0).getString("name");
            house_name.setText(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
