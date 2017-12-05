package com.example.muhammet.communicator.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.adapters.MemberAdapter;
import com.example.muhammet.communicator.models.Member;
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

public class FetchMembersTask extends AsyncTask<String, Void, Member[]> {

    Context mContext;
    MemberAdapter memberAdapter;
    
    public FetchMembersTask(Context context, MemberAdapter memberAdapter) throws MalformedURLException {
        mContext = context;
        this.memberAdapter = memberAdapter;
    }

    @Override
    protected Member[] doInBackground(String... strings) {
        String 		      communicatorJsonStr = NetworkUtilities.getStringResponse(strings[0]);

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