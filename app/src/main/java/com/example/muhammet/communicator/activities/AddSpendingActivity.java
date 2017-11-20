package com.example.muhammet.communicator.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.muhammet.communicator.R;

public class AddSpendingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spending);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
