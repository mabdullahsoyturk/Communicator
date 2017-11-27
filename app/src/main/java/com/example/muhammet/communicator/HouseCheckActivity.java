package com.example.muhammet.communicator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.muhammet.communicator.tasks.FetchUserTask;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public class HouseCheckActivity extends AppCompatActivity {

    private EditText invitation;
    private Button confirm;
    private Button addNewHouse;

    ProfileTracker profileTracker;

    private String mail;
    private String first_name;
    private String last_name;
    private String photo_url;
    private String facebook_id;

    private String invitation_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_check);

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

            fetchMail();

            Profile currentProfile = Profile.getCurrentProfile();
            if (currentProfile != null) {
                first_name = currentProfile.getFirstName();
                last_name = currentProfile.getLastName();
                photo_url = currentProfile.getProfilePictureUri(100,100).toString();
                facebook_id = currentProfile.getId();
                Log.i("Link uri:", currentProfile.getLinkUri().toString());
                Log.i("Photo:", currentProfile.getProfilePictureUri(100,100).toString());
            }
            else {
                // Fetch the profile, which will trigger the onCurrentProfileChanged receiver
                Profile.fetchProfileForCurrentAccessToken();
            }
        }

        confirm = findViewById(R.id.activity_house_check_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invitation_code = invitation.getText().toString();

            }
        });

        addNewHouse = findViewById(R.id.activity_house_check_add_house);
        addNewHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        FetchUserTask fetchUserTask = null;
        try {
            fetchUserTask = new FetchUserTask(this, first_name, last_name, photo_url,mail, facebook_id);
            fetchUserTask.execute("https://warm-meadow-40773.herokuapp.com/signup");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void fetchMail(){
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            if (object.has("email")) {
                                mail = object.getString("email");
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
