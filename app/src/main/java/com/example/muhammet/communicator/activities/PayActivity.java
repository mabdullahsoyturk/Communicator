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
import android.widget.EditText;
import android.widget.Spinner;

import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.tasks.AddPaymentTask;
import com.example.muhammet.communicator.utilities.CustomSpinner;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int ACTIVITY_PAY_LOADER_ID = 1001;
    private static final String TAG = PayActivity.class.getSimpleName();

    Context mContext;
    private EditText et_how_much;

    private String facebook_id;
    private String house_id_server;
    private Spinner spinner;
    private Button addPayment;
    private String[] ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner = findViewById(R.id.activity_pay_spinner);
        addPayment = findViewById(R.id.add_payment_button);
        et_how_much = findViewById(R.id.activity_pay_how_much);

        mContext = this;

        Intent intent = getIntent();
        facebook_id = intent.getStringExtra("facebook_id");
        house_id_server = intent.getStringExtra("house_id_server");

        getSupportLoaderManager().initLoader(ACTIVITY_PAY_LOADER_ID, null, this);
    }

    public void onClickMakePayment(View view) throws MalformedURLException {
        String how_much = et_how_much.getText().toString();

        int index = spinner.getSelectedItemPosition();

        AddPaymentTask addPaymentTask = new AddPaymentTask(mContext, facebook_id, Double.parseDouble(how_much), ids[index], house_id_server);
        addPaymentTask.execute(NetworkUtilities.buildWithFacebookId(facebook_id) + "/payment");
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
                            "house_id_server=?",
                            new String[]{house_id_server},
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
        ids = new String[data.getCount()];

        for(int i = 0; i < data.getCount(); i++){
            data.moveToPosition(i);
            String first_name = data.getString(data.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_FIRST_NAME));
            String face_id = data.getString(data.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_FACEBOOK_ID));
            ids[i] = face_id;
            list.add(first_name);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}
