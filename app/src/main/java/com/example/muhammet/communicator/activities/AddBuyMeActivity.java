package com.example.muhammet.communicator.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.models.BuyMe;
import com.example.muhammet.communicator.tasks.AddBuyMeTask;
import com.example.muhammet.communicator.tasks.FetchBuyMeTask;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

import java.net.MalformedURLException;

public class AddBuyMeActivity extends AppCompatActivity {

    private TextView tv_name;
    private TextView tv_description;

    private String user_id;
    private String house_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_buy_me);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
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

        AddBuyMeTask addBuyMeTask = new AddBuyMeTask(this, name,description);
        addBuyMeTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/" + user_id + "/houses/" + house_id + "/buy_mes");

        Intent intent = new Intent(this,BaseActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("house_id", house_id);
        startActivity(intent);

    }
}
