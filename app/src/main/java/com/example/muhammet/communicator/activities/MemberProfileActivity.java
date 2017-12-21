package com.example.muhammet.communicator.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.muhammet.communicator.R;

public class MemberProfileActivity extends AppCompatActivity {

    TextView memberProfileName;
    TextView memberProfileDebt;

    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        memberProfileName = findViewById(R.id.tv_member_name);
        memberProfileDebt = findViewById(R.id.tv_member_debt);

        Intent intent = getIntent();

        id = intent.getLongExtra("id", 0);



    }

}
