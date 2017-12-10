package com.example.muhammet.communicator.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.tasks.AddBuyMeTask;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

import java.net.MalformedURLException;
import java.sql.Date;

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

        Date currentDate = new Date(System.currentTimeMillis());

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("user_id", user_id);
        contentValues.put("house_id", house_id);
        contentValues.put("created_time", currentDate.toString());

        Uri uri = getContentResolver().insert(CommunicatorContract.BuyMeEntry.CONTENT_URI, contentValues);
        Cursor cursor = getContentResolver().query(uri,
                null,
                null,
                null,
                null);

        cursor.moveToPosition(0);

        int idIndex = cursor.getColumnIndex(CommunicatorContract.BuyMeEntry._ID);
        final int id = cursor.getInt(idIndex);

        if(uri != null){
            Toast.makeText(getBaseContext(), "Buy me has been added!", Toast.LENGTH_LONG).show();
        }

        AddBuyMeTask addBuyMeTask = new AddBuyMeTask(this, id, name,description, user_id, house_id, currentDate.toString());
        addBuyMeTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/" + user_id + "/houses/" + house_id + "/buy_mes");

        cursor.close();

        Intent intent = new Intent(this,BaseActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("house_id", house_id);
        startActivity(intent);

    }
}
