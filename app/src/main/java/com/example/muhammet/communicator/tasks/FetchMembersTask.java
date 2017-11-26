package com.example.muhammet.communicator.tasks;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.adapters.MemberAdapter;
import com.example.muhammet.communicator.models.Member;

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
import java.util.Set;

/**
 * Created by Muhammet on 13.11.2017.
 */

public class FetchMembersTask extends AsyncTask<String, Void, Member[]> {

    Context mContext;
    MemberAdapter memberAdapter;

    private String facebook_id = "10215415549690496";

    public FetchMembersTask(Context context, MemberAdapter memberAdapter) throws MalformedURLException {
        mContext = context;
        this.memberAdapter = memberAdapter;
    }

    @Override
    protected Member[] doInBackground(String... strings) {
        HttpURLConnection urlConnection   = null;
        BufferedReader    reader          = null;
        String 		      forecastJsonStr = null;

        try {
            URL weatherURL = new URL(strings[0]);
            urlConnection  = (HttpURLConnection) weatherURL.openConnection();
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
                    forecastJsonStr = buffer.toString();
                }
            }

            Log.i("forecastJsonStr", forecastJsonStr);
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

        try {
            return getMembersDataFromJson(forecastJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Member[] getMembersDataFromJson(String forecastJsonStr)
            throws JSONException {

        JSONArray memberJson  = new JSONArray(forecastJsonStr);
        JSONObject  memberObject  = memberJson.getJSONObject(0);

        Member[] members = new Member[memberJson.length()];

        for(int i = 0; i < memberJson.length(); i++) {

            String first_name = memberJson.getJSONObject(i).getString("first_name");
            String debt = memberJson.getJSONObject(i).getString("balance");
            Member member = new Member(R.drawable.icon_profile_empty, first_name, debt);

            members[i] = member;
        }
        return members;
    }

    @Override
    protected void onPostExecute(Member[] members) {
        super.onPostExecute(members);

        memberAdapter.setMemberData(members);
    }
}