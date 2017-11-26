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

import java.net.MalformedURLException;

public class AddBuyMeActivity extends AppCompatActivity {

    private TextView tv_name;
    private TextView tv_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_buy_me);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        addBuyMeTask.execute("https://warm-meadow-40773.herokuapp.com/api/users/5a1b0d816058c0001439ae35/houses/5a1b12128351e60014b50505/buy_mes");

        Intent intent = new Intent(this,BaseActivity.class);
        startActivity(intent);

    }
}
