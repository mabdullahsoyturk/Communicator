package com.example.muhammet.communicator.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.adapters.MemberProfileAdapter;
import com.example.muhammet.communicator.data.CommunicatorContract;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class MemberProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MemberProfileActivity.class.getSimpleName();
    private static final int MEMBER_SPENDING_LOADER_ID = 10;
    private static final int MEMBER_INFO_LOADER_ID = 30;

    Context mContext;
    RecyclerView mRecyclerView;
    MemberProfileAdapter memberProfileAdapter;
    private DividerItemDecoration mDividerItemDecoration;

    private TextView memberProfileName;
    private TextView memberProfileDebt;
    private ImageView memberProfilePhoto;

    private String member_id;
    private String facebook_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;

        memberProfileName = findViewById(R.id.tv_member_name);
        memberProfileDebt = findViewById(R.id.tv_member_debt);
        memberProfilePhoto = findViewById(R.id.iv_member_photo);

        mRecyclerView = findViewById(R.id.rv_member_profile);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        memberProfileAdapter = new MemberProfileAdapter(mContext);
        mRecyclerView.setAdapter(memberProfileAdapter);

        Intent intent = getIntent();

        member_id = intent.getStringExtra("id");
        facebook_id = intent.getStringExtra("facebook_id");
        Log.i("MemberId", member_id);

        getSupportLoaderManager().initLoader(MEMBER_INFO_LOADER_ID, null, this);
        getSupportLoaderManager().initLoader(MEMBER_SPENDING_LOADER_ID, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == MEMBER_SPENDING_LOADER_ID){
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
                                "facebook_id=?",
                                new String[]{facebook_id},
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
        if(id == MEMBER_INFO_LOADER_ID){
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
                        return getContentResolver().query(CommunicatorContract.UserEntry.CONTENT_URI.buildUpon().appendPath(member_id).build(),
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

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == MEMBER_SPENDING_LOADER_ID){
            memberProfileAdapter.swapCursor(data);
        }
        if(loader.getId() == MEMBER_INFO_LOADER_ID){
            if(data.moveToFirst()){
                String first_name = data.getString(data.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_FIRST_NAME));
                String last_name  = data.getString(data.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_LAST_NAME));
                String full_name = first_name + " " + last_name;

                String photo = data.getString(data.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_PHOTO_URL));
                double balance = data.getDouble(data.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_BALANCE));

                memberProfileDebt.setText(String.valueOf(balance));
                memberProfileName.setText(full_name);
                Uri uri = Uri.parse(photo);

                Transformation transformation = new RoundedTransformationBuilder()
                        .cornerRadiusDp(30)
                        .oval(false)
                        .build();
                Picasso.with(mContext)
                        .load(uri)
                        .transform(transformation)
                        .into(memberProfilePhoto);

            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        memberProfileAdapter.swapCursor(null);
    }
}
