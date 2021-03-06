package com.example.muhammet.communicator.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.tasks.CheckHousesTask;
import com.example.muhammet.communicator.tasks.CheckUserTask;
import com.example.muhammet.communicator.utilities.NetworkUtilities;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.ProfileTracker;

public class HouseCheckActivity extends AppCompatActivity{
    Context mContext;

    private EditText invitation;
    private Button confirm;
    private Button addNewHouse;
    ProgressBar progressBar;

    ProfileTracker profileTracker;

    private String first_name;
    private String last_name;
    private String photo_url;
    private String facebook_id;

    private String invitation_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_house_check);

        mContext = this;

        Intent intent = getIntent();
        facebook_id = intent.getStringExtra("facebook_id");

        invitation = findViewById(R.id.activity_house_check_invitation);
        progressBar = findViewById(R.id.activity_house_check_progress);

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged (Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    first_name = currentProfile.getFirstName();
                    last_name = currentProfile.getLastName();
                    photo_url = currentProfile.getProfilePictureUri(150,150).toString();
                    facebook_id = currentProfile.getId();
                    checkIfUserExists();
                }
            }
        };

        checkIfUserHasToken();

        confirm = findViewById(R.id.activity_house_check_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invitation_code = invitation.getText().toString();
                checkIfInvitationCodeValid(invitation_code);
            }
        });

        addNewHouse = findViewById(R.id.activity_house_check_add_house);
        addNewHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddNewHouseActivity.class);
                intent.putExtra("facebook_id", facebook_id);
                startActivity(intent);
            }
        });
    }

    public void checkIfUserExists(){
        try {
            //ServiceUtils.checkUserService(mContext, ServiceTasks.ACTION_CHECK_USER, first_name, last_name, photo_url, facebook_id);

            CheckUserTask checkUserTask = new CheckUserTask(mContext, first_name,last_name,photo_url,facebook_id, progressBar);
            checkUserTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "signup/check");
        } catch (Exception e) {e.printStackTrace();}
    }

    public void checkIfUserHasToken(){
        if (AccessToken.getCurrentAccessToken() != null) {

            Profile currentProfile = Profile.getCurrentProfile();
            if (currentProfile != null) {
                first_name = currentProfile.getFirstName();
                last_name = currentProfile.getLastName();
                photo_url = currentProfile.getProfilePictureUri(150,150).toString();
                facebook_id = currentProfile.getId();
                checkIfUserExists();
            }
            else {
                // Fetch the profile, which will trigger the onCurrentProfileChanged receiver
                Profile.fetchProfileForCurrentAccessToken();
            }
        }
    }

    private void checkIfInvitationCodeValid(String invitation_code){
        try {
            //ServiceUtils.checkHousesService(mContext, ServiceTasks.ACTION_CHECK_HOUSES, invitation_code, facebook_id);

            CheckHousesTask checkHousesTask = new CheckHousesTask(mContext, facebook_id, first_name, last_name, photo_url);
            checkHousesTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/" + facebook_id + "/houses/" + invitation_code);
        } catch (Exception e) {e.printStackTrace();}
    }
}
