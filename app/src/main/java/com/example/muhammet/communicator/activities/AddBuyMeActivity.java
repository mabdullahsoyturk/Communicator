package com.example.muhammet.communicator.activities;

import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.tasks.AddBuyMeTask;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

import java.net.MalformedURLException;

public class AddBuyMeActivity extends AppCompatActivity {

    private TextView tv_name;
    private TextView tv_description;

    private String facebook_id;
    private String house_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_buy_me);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        facebook_id = intent.getStringExtra("facebook_id");
        house_id = intent.getStringExtra("house_id");

        tv_name = findViewById(R.id.activity_add_buy_me_name);
        tv_description = findViewById(R.id.activity_add_buy_me_description);
    }

    public void onClickAddBuyMe(View view) throws MalformedURLException {
        String name = tv_name.getText().toString();
        String description = tv_description.getText().toString();

        if(name.length() == 0 || description.length() == 0){
            return;
        }

        AddBuyMeTask addBuyMeTask = new AddBuyMeTask(this,name,description, facebook_id, house_id);
        addBuyMeTask.execute(NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id, house_id) + "/buy_mes");
    }
}
