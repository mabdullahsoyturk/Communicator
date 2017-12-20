package com.example.muhammet.communicator.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.models.Spending;
import com.example.muhammet.communicator.tasks.AddSpendingTask;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddSpendingActivity extends AppCompatActivity {

    private EditText et_add_spending_name;
    private EditText et_add_spending_cost;

    private String facebook_id;
    private String house_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spending);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        facebook_id = intent.getStringExtra("facebook_id");
        house_id = intent.getStringExtra("house_id");

        Log.i("Spending'de fid", facebook_id);
        Log.i("Spending'de hid", house_id);

        et_add_spending_name = findViewById(R.id.et_add_spending_name);
        et_add_spending_cost = findViewById(R.id.et_add_spending_cost);
    }

    public void onClickAddSpending(View view) throws MalformedURLException {
        String name = et_add_spending_name.getText().toString();
        String cost = et_add_spending_cost.getText().toString();

        AddSpendingTask addSpendingTask = new AddSpendingTask(this, name,cost, facebook_id, house_id);
        addSpendingTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/" + facebook_id + "/houses/" + house_id + "/spendings");

        Intent intent = new Intent(this, BaseActivity.class);
        intent.putExtra("facebook_id", facebook_id);
        intent.putExtra("house_id", house_id);
        startActivity(intent);
    }
}
