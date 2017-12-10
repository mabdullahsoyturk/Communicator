package com.example.muhammet.communicator.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.activities.BaseActivity;
import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.tasks.AddNewHouseTask;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

import java.net.MalformedURLException;
import java.sql.Date;

public class AddNewHouseActivity extends AppCompatActivity {

    Context mContext;

    private String user_id;

    private EditText et_house_name;
    private Button btn_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_house);

        mContext = this;

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");

        et_house_name = findViewById(R.id.et_add_new_house);
        btn_confirm = findViewById(R.id.activity_add_new_house_confirm);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String house_name = et_house_name.getText().toString();

                Date currentDate = new Date(System.currentTimeMillis());

                ContentValues contentValues = new ContentValues();
                contentValues.put("name", house_name);
                contentValues.put("created_time", currentDate.toString());

                getContentResolver().insert(CommunicatorContract.HouseEntry.CONTENT_URI, contentValues);

                Intent intent = new Intent(mContext, BaseActivity.class);
                mContext.startActivity(intent);

//                try {
//                    AddNewHouseTask addNewHouseTask = new AddNewHouseTask(mContext, user_id, house_name);
//                    addNewHouseTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/" + user_id + "/houses");
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }
}
