package com.example.muhammet.communicator.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.services.ServiceTasks;
import com.example.muhammet.communicator.services.ServiceUtils;
import com.example.muhammet.communicator.tasks.AddNewHouseTask;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

import java.net.MalformedURLException;

public class AddNewHouseActivity extends AppCompatActivity {

    Context mContext;

    private String facebook_id;

    private EditText et_house_name;
    private Button btn_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_house);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;

        Intent intent = getIntent();
        facebook_id = intent.getStringExtra("facebook_id");

        et_house_name = findViewById(R.id.et_add_new_house);
        btn_confirm = findViewById(R.id.activity_add_new_house_confirm);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String house_name = et_house_name.getText().toString();

                ServiceUtils.addNewHouseService(mContext, ServiceTasks.ACTION_ADD_NEW_HUOSE, house_name, facebook_id);
//                try {
//                    AddNewHouseTask addNewHouseTask = new AddNewHouseTask(mContext, facebook_id, house_name);
//                    addNewHouseTask.execute(NetworkUtilities.buildWithFacebookId(facebook_id) + "/houses?facebook_id=" + facebook_id);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }
}
