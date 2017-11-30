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
    private String user_id;
    private String house_id;
    private String name;
    MemberAdapter memberAdapter;

    public FetchHouseTask(Context context, TextView house_name, MemberAdapter memberAdapter, String user_id) throws MalformedURLException {
        mContext = context;
        this.house_name = house_name;
        this.memberAdapter = memberAdapter;
        this.user_id = user_id;
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

        JSONArray jsonArray = null;
        JSONObject jsonObject = null;
        try {
            jsonArray = new JSONArray(s);
            jsonObject = jsonArray.getJSONObject(0);
            name = jsonObject.getString("name");
            house_name.setText(name);
            house_id = jsonObject.getString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            FetchMembersTask fetchMembersTask = new FetchMembersTask(mContext, memberAdapter);
            fetchMembersTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/" + user_id + "/houses/" + house_id + "/members");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
