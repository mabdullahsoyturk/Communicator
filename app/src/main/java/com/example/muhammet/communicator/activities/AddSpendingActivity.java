package com.example.muhammet.communicator.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.models.Spending;
import com.example.muhammet.communicator.tasks.AddSpendingTask;

import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddSpendingActivity extends AppCompatActivity {

    private EditText et_add_spending_name;
    private EditText et_add_spending_cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spending);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_add_spending_name = findViewById(R.id.et_add_spending_name);
        et_add_spending_cost = findViewById(R.id.et_add_spending_cost);
    }

    public void onClickAddSpending(View view) throws MalformedURLException {
        String name = et_add_spending_name.getText().toString();
        String cost = et_add_spending_cost.getText().toString();

        DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
        String currentDateandTime = df.format(new Date());
        Log.i("currentDateandTime", currentDateandTime);

        AddSpendingTask addSpendingTask = new AddSpendingTask(this,name,cost,currentDateandTime);
        addSpendingTask.execute("https://warm-meadow-40773.herokuapp.com/api/users/5a1b0d816058c0001439ae35/houses/5a1b12128351e60014b50505/spendings");

        Intent intent = new Intent(this, BaseActivity.class);
        startActivity(intent);
    }
}
