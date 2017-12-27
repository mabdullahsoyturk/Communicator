package com.example.muhammet.communicator.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.data.CommunicatorContract;

import java.util.ArrayList;
import java.util.List;

public class PayActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int ACTIVITY_PAY_LOADER_ID = 1001;
    private static final String TAG = PayActivity.class.getSimpleName();

    Context mContext;

    private String facebook_id;
    private String house_id;
    private Spinner spinner;
    private Button addPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner = findViewById(R.id.activitiy_pay_spinner);
        addPayment = findViewById(R.id.add_payment_button);

        mContext = this;

        Intent intent = getIntent();
        facebook_id = intent.getStringExtra("facebook_id");
        house_id = intent.getStringExtra("house_id");

        getSupportLoaderManager().initLoader(ACTIVITY_PAY_LOADER_ID, null, this);
    }

    public void onClickMakePayment(View view){

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mTaskData = null;

            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public Cursor loadInBackground() {

                try {
                    return mContext.getContentResolver().query(CommunicatorContract.UserEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<String> list = new ArrayList<String>();
        Log.i("data size", "" + data.getCount());

        for(int i = 0; i < data.getCount(); i++){
            data.moveToPosition(i);
            String first_name = data.getString(data.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_FIRST_NAME));
            list.add(first_name);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}
