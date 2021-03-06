package com.example.muhammet.communicator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.muhammet.communicator.activities.BaseActivity;
import com.example.muhammet.communicator.activities.HouseCheckActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    LoginButton loginButton;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        loginButton = findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions("email");

        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String facebook_id = loginResult.getAccessToken().getUserId();
                launchHouseCheckActivity(facebook_id);
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Facebook login is needed to use the app", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                String toastMessage = exception.getMessage();
                Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_LONG).show();
            }
        });

        com.facebook.AccessToken loginToken = com.facebook.AccessToken.getCurrentAccessToken();
        if (loginToken != null) {
            launchHouseCheckActivity(loginToken.getUserId());
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void launchHouseCheckActivity(String facebook_id) {
        Context context = MainActivity.this;
        Class houseCheckActivity = HouseCheckActivity.class;
        Intent intent = new Intent(context, houseCheckActivity);
        intent.putExtra("facebook_id", facebook_id);
        startActivity(intent);
    }
}
