package com.example.muhammet.communicator.activities;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.services.ServiceTasks;
import com.example.muhammet.communicator.services.ServiceUtils;
import com.example.muhammet.communicator.tasks.AddBuyMeTask;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

import java.net.MalformedURLException;

public class AddBuyMeActivity extends AppCompatActivity {

    Context mContext;
    private TextView tv_name;
    private TextView tv_description;

    private String facebook_id;
    //private String house_id;
    private String house_id_server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_buy_me);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;

        Intent intent = getIntent();
        facebook_id = intent.getStringExtra("facebook_id");
        //house_id = intent.getStringExtra("house_id");
        house_id_server = intent.getStringExtra("house_id_server");
        Log.i("HouseIdInAddBuyMe", house_id_server);

        tv_name = findViewById(R.id.activity_add_buy_me_name);
        tv_description = findViewById(R.id.activity_add_buy_me_description);
    }

    public void onClickAddBuyMe(View view) throws MalformedURLException {
        String name = tv_name.getText().toString();
        String description = tv_description.getText().toString();

        if(name.length() == 0 || description.length() == 0){
            Toast.makeText(mContext, "Name or description field cannot be left blank.", Toast.LENGTH_LONG).show();
            return;
        }

        //ServiceUtils.addBuyMeService(mContext, ServiceTasks.ACTION_ADD_BUY_ME, name, description, facebook_id, house_id);
        AddBuyMeTask addBuyMeTask = new AddBuyMeTask(this,name,description, facebook_id, house_id_server);
        addBuyMeTask.execute(NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id, house_id_server) + "/buy_mes");
    }
}
