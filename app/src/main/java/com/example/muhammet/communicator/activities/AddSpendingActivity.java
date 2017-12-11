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

    private String user_id;
    private String house_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spending);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        house_id = intent.getStringExtra("house_id");

        et_add_spending_name = findViewById(R.id.et_add_spending_name);
        et_add_spending_cost = findViewById(R.id.et_add_spending_cost);
    }

    public void onClickAddSpending(View view) throws MalformedURLException {
        String name = et_add_spending_name.getText().toString();
        String cost = et_add_spending_cost.getText().toString();

        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("cost", cost);
        contentValues.put("user_id", user_id);
        contentValues.put("house_id", house_id);
        contentValues.put("created_time", currentDate.toString());

        Uri uri = getContentResolver().insert(CommunicatorContract.SpendingEntry.CONTENT_URI, contentValues);

        Cursor cursor = getContentResolver().query(uri,
                null,
                null,
                null,
                null);

        cursor.moveToPosition(0);

        int idIndex = cursor.getColumnIndex(CommunicatorContract.SpendingEntry._ID);
        final int id = cursor.getInt(idIndex);

        if(uri != null){
            Toast.makeText(getBaseContext(), "Spending has been added!", Toast.LENGTH_LONG).show();
        }

        AddSpendingTask addSpendingTask = new AddSpendingTask(this,id, name,cost, user_id, house_id, currentDate.toString());
        addSpendingTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/" + user_id + "/houses/" + house_id + "/spendings");

        Intent intent = new Intent(this, BaseActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("house_id", house_id);
        startActivity(intent);
    }
}
