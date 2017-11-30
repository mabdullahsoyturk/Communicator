package com.example.muhammet.communicator.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muhammet.communicator.MainActivity;
import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.fragments.BuyMeFragment;
import com.example.muhammet.communicator.fragments.HomeFragment;
import com.example.muhammet.communicator.fragments.SpendingsFragment;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    ProfileTracker profileTracker;
    private String mail;
    private String first_name;
    private String last_name;
    private String photo_url;
    private String facebook_id;
    private String user_id;
    private String house_id;

    private TextView name;
    private TextView email;
    private ImageView image;
    public TextView house_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        house_name = findViewById(R.id.tv_house_name);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        house_id = intent.getStringExtra("house_id");

        //////////////////////////TOOLBAR CONFIGS///////////////////////////////////////////////////
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addLandingFragment();

        ///////////////////////////BOTTOM BAR CONFIGS///////////////////////////////////////////////
        BottomNavigationView bottomBar = findViewById(R.id.bottom_navigation);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fragment = new HomeFragment();
                        Bundle bundleHome = new Bundle();
                        bundleHome.putString("user_id", user_id);
                        bundleHome.putString("house_id", house_id);
                        fragment.setArguments(bundleHome);
                        break;
                    case R.id.navigation_buy_me:
                        fragment = new BuyMeFragment();
                        Bundle bundleBuyMe = new Bundle();
                        bundleBuyMe.putString("user_id", user_id);
                        bundleBuyMe.putString("house_id", house_id);
                        fragment.setArguments(bundleBuyMe);
                        break;
                    case R.id.navigation_spendings:
                        fragment = new SpendingsFragment();
                        Bundle bundleSpendings = new Bundle();
                        bundleSpendings.putString("user_id", user_id);
                        bundleSpendings.putString("house_id", house_id);
                        fragment.setArguments(bundleSpendings);
                        break;
            }
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.base_container, fragment).commit();
                return true;
        }});

        //////////////////////////////////NAVIGATION DRAWER CONFIGS/////////////////////////////////

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        name = navigationView.getHeaderView(0).findViewById(R.id.name_surname);
        email = navigationView.getHeaderView(0).findViewById(R.id.email);
        image = navigationView.getHeaderView(0).findViewById(R.id.imageView);
        /////////////////////////////////FACEBOOK USER CHECK////////////////////////////////////////
        // register a receiver for the onCurrentProfileChanged event
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged (Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    fetchMail();
                    first_name = currentProfile.getFirstName();
                    last_name = currentProfile.getLastName();
                    photo_url = currentProfile.getProfilePictureUri(100,100).toString();
                    name.setText(currentProfile.getFirstName() + currentProfile.getLastName());
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
                name.setText(first_name + " " + last_name);
                displayProfileInfo(currentProfile);
                Log.i("Link uri:", currentProfile.getLinkUri().toString());
                Log.i("Photo:", currentProfile.getProfilePictureUri(100,100).toString());
            }
            else {
                // Fetch the profile, which will trigger the onCurrentProfileChanged receiver
                Profile.fetchProfileForCurrentAccessToken();
            }
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this,SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        if(id == R.id.action_logout){
            onLogout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addLandingFragment(){
        fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user_id", user_id);
        bundle.putString("house_id",house_id);
        fragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.base_container, fragment).commit();
    }

    public void onLogout(){
        LoginManager.getInstance().logOut();
        launchLoginActivity();
    }

    ///////////////////////////////////////FORMAT IMAGES////////////////////////////////////////////
    private void displayProfilePic(Uri uri) {
        // helper method to load the profile pic in a circular imageview
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(this)
                .load(uri)
                .transform(transformation)
                .into(image);
    }

    private void displayProfileInfo(Profile profile) {
        Uri profilePicUri = profile.getProfilePictureUri(100, 100);
        displayProfilePic(profilePicUri);
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
                                email.setText(mail);
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
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////DIRECT TO ANOTHER ACTIVITY//////////////////////////////////
    private void launchLoginActivity() {
        Context context = BaseActivity.this;
        Class mainActivity = MainActivity.class;
        Intent intent = new Intent(context, mainActivity);
        startActivity(intent);
        finish();
    }

    public void sendToPrefs(View view){
        Intent startSettingsActivity = new Intent(this,SettingsActivity.class);
        startActivity(startSettingsActivity);
    }

    public void sendToPay(View view){
        Intent startPayActivity = new Intent(this,PayActivity.class);
        startActivity(startPayActivity);
    }

    public void sendToAddSpending(View view){
        Intent startAddSpendingActivity = new Intent(this,AddSpendingActivity.class);
        startActivity(startAddSpendingActivity);
    }

    public void sendToAddBuyMe(View view){
        Intent startAddBuyMeActivity = new Intent(this,AddBuyMeActivity.class);
        startActivity(startAddBuyMeActivity);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
