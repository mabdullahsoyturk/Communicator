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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.adapters.MemberProfileAdapter;
import com.example.muhammet.communicator.data.CommunicatorContract;

public class MemberProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MemberProfileActivity.class.getSimpleName();
    private static final int MEMBER_SPENDING_LOADER_ID = 10;
    Context mContext;
    RecyclerView mRecyclerView;
    MemberProfileAdapter memberProfileAdapter;
    private DividerItemDecoration mDividerItemDecoration;

    TextView memberProfileName;
    TextView memberProfileDebt;

    private String member_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;

        memberProfileName = findViewById(R.id.tv_member_name);
        memberProfileDebt = findViewById(R.id.tv_member_debt);

        mRecyclerView = findViewById(R.id.rv_member_profile);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        memberProfileAdapter = new MemberProfileAdapter(mContext);
        mRecyclerView.setAdapter(memberProfileAdapter);

        Intent intent = getIntent();

        member_id = intent.getStringExtra("id");

        getSupportLoaderManager().initLoader(MEMBER_SPENDING_LOADER_ID, null, this);

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
                    return getContentResolver().query(CommunicatorContract.SpendingEntry.CONTENT_URI,
                            null,
                            "house_id",
                             new String[]{String.valueOf(member_id)},
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
        memberProfileAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        memberProfileAdapter.swapCursor(null);
    }
}
