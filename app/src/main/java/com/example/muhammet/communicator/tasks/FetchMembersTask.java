package com.example.muhammet.communicator.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

public class FetchMembersTask extends AsyncTask<String, Void, Member[]> {

    Context mContext;
    MemberAdapter memberAdapter;
    
    public FetchMembersTask(Context context, MemberAdapter memberAdapter) throws MalformedURLException {
        mContext = context;
        this.memberAdapter = memberAdapter;
    }

    @Override
    protected Member[] doInBackground(String... strings) {
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

        try {
            return getMembersDataFromJson(communicatorJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Member[] getMembersDataFromJson(String communicatorJsonStr)
            throws JSONException {

        JSONArray memberJson  = new JSONArray(communicatorJsonStr);
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