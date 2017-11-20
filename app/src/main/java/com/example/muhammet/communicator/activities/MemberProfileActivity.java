package com.example.muhammet.communicator.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.muhammet.communicator.R;

public class MemberProfileActivity extends AppCompatActivity {

    TextView memberProfileName;
    TextView memberProfileDebt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        memberProfileName = findViewById(R.id.tv_member_name);
        memberProfileDebt = findViewById(R.id.tv_member_debt);

        Intent intent = getIntent();

        String member_name = intent.getStringExtra("member_name");
        String member_debt = intent.getStringExtra("member_debt");

        memberProfileName.setText(member_name);
        memberProfileDebt.setText(member_debt);

    }

}
