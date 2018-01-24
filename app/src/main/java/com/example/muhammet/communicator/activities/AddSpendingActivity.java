package com.example.muhammet.communicator.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.tasks.AddSpendingTask;
import com.example.muhammet.communicator.utilities.CustomSpinner;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class AddSpendingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int ACTIVITY_ADD_SPENDING_LOADER_ID = 1000;
    private static final String TAG = AddSpendingActivity.class.getSimpleName();

    private EditText et_add_spending_name;
    private EditText et_add_spending_cost;
    Context mContext;

    private String facebook_id;
    //private String house_id;
    private String house_id_server;
    private CustomSpinner spinner;
    private String[] listOfIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spending);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;

        Intent intent = getIntent();
        facebook_id = intent.getStringExtra("facebook_id");
        //house_id = intent.getStringExtra("house_id");
        house_id_server = intent.getStringExtra("house_id_server");
        Log.i("HouseIdServerSpending", house_id_server);

        spinner = findViewById(R.id.activity_add_spending_members_spinner);

        et_add_spending_name = findViewById(R.id.et_add_spending_name);
        et_add_spending_cost = findViewById(R.id.et_add_spending_cost);

        getSupportLoaderManager().initLoader(ACTIVITY_ADD_SPENDING_LOADER_ID, null, this);
    }

    public void onClickAddSpending(View view) throws MalformedURLException {
        String name = et_add_spending_name.getText().toString();
        String cost = et_add_spending_cost.getText().toString();

        List<Integer> indices = spinner.getSelectedIndicies();
        List<String> listOfNames = spinner.getSelectedStrings();
        List<String> selectedUserIds = new ArrayList<>();

        for(int i = 0; i < indices.size(); i++){
            selectedUserIds.add(listOfIds[indices.get(i)]);
        }
        //ServiceUtils.addSpendingService(mContext, ServiceTasks.ACTION_ADD_SPENDING, name, Double.parseDouble(cost), facebook_id, house_id);

        AddSpendingTask addSpendingTask = new AddSpendingTask(this, name,Double.parseDouble(cost), facebook_id, house_id_server, selectedUserIds);
        addSpendingTask.execute(NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id, house_id_server) + "/spendings");
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
        String[] listOfNames = new String[data.getCount()];
        listOfIds = new String[data.getCount()];

        for(int i = 0; i < data.getCount(); i++){
            data.moveToPosition(i);
            String first_name = data.getString(data.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_FIRST_NAME));
            String user_facebook_id = data.getString(data.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_FACEBOOK_ID));

            listOfNames[i] = first_name;
            listOfIds[i] = user_facebook_id;
            Log.i("Ids", user_facebook_id);
        }

        spinner.setItems(listOfNames);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}
