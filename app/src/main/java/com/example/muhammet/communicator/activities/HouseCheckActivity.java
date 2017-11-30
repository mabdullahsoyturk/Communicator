package com.example.muhammet.communicator.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.muhammet.communicator.AsyncResponse;
import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.tasks.CheckHousesTask;
import com.example.muhammet.communicator.tasks.CheckUserTask;
import com.example.muhammet.communicator.utilities.NetworkUtilities;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public class HouseCheckActivity extends AppCompatActivity{

    Context mContext;

    private EditText invitation;
    private Button confirm;
    private Button addNewHouse;

    ProfileTracker profileTracker;

    private String mail;
    private String first_name;
    private String last_name;
    private String photo_url;
    private String facebook_id;
    private String user_id;

    private String invitation_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_check);

        mContext = this;
        invitation = findViewById(R.id.activity_house_check_invitation);

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged (Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    fetchMail();
                    first_name = currentProfile.getFirstName();
                    last_name = currentProfile.getLastName();
                    photo_url = currentProfile.getProfilePictureUri(100,100).toString();
                }
            }
        };

        if (AccessToken.getCurrentAccessToken() != null) {

            Profile currentProfile = Profile.getCurrentProfile();
            if (currentProfile != null) {
                first_name = currentProfile.getFirstName();
                last_name = currentProfile.getLastName();
                photo_url = currentProfile.getProfilePictureUri(100,100).toString();
                facebook_id = currentProfile.getId();
            }
            else {
                // Fetch the profile, which will trigger the onCurrentProfileChanged receiver
                Profile.fetchProfileForCurrentAccessToken();
            }

            fetchMail();
        }

        confirm = findViewById(R.id.activity_house_check_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invitation_code = invitation.getText().toString();
                try {
                    Log.i("HouseCheckId", user_id);
                    CheckHousesTask checkHousesTask = new CheckHousesTask(mContext, user_id);
                    checkHousesTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/" + user_id + "/houses/" + invitation_code);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        addNewHouse = findViewById(R.id.activity_house_check_add_house);
        addNewHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddNewHouseActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });
    }

    private void fetchMail(){
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        first_name = Profile.getCurrentProfile().getFirstName();
                        last_name = Profile.getCurrentProfile().getLastName();
                        photo_url = Profile.getCurrentProfile().getProfilePictureUri(100,100).toString();
                        facebook_id = Profile.getCurrentProfile().getId();
                        try {
                            if (object.has("email")) {
                                mail = object.getString("email");
                                CheckUserTask checkUserTask = null;
                                try {
                                    Log.i("facebook_id", facebook_id);
                                    checkUserTask = new CheckUserTask(mContext, first_name, last_name, photo_url, mail, facebook_id, new AsyncResponse() {
                                        @Override
                                        public void processFinish(String output) {
                                            user_id = output;
                                            Log.i("FetchUserId", "id is " + user_id);
                                        }
                                    });
                                    checkUserTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "signup");
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,gender,birthday"); // id,first_name,last_name,email,gender,birthday,cover,picture.type(large)
        request.setParameters(parameters);
        request.executeAsync();
    }
}
