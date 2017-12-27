package com.example.muhammet.communicator.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.muhammet.communicator.activities.AddSpendingActivity;
import com.example.muhammet.communicator.activities.BaseActivity;
import com.example.muhammet.communicator.activities.PayActivity;
import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.listeners.ListItemClickListener;
import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.activities.MemberProfileActivity;
import com.example.muhammet.communicator.adapters.MemberAdapter;
import com.example.muhammet.communicator.sync.CommunicatorSyncTask;
import com.example.muhammet.communicator.sync.CommunicatorSyncUtils;
import com.example.muhammet.communicator.utilities.PreferenceUtilities;

import java.util.Locale;

public class HomeFragment extends Fragment implements ListItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final int MEMBER_LOADER_ID = 2;
    private static final int HOUSE_LOADER_ID = 20;

    MemberAdapter memberAdapter;
    RecyclerView rv_members;
    private DividerItemDecoration mDividerItemDecoration;

    Context mContext;
    private TextView house_name;
    private Button btn_add_member;
    private Button add_spending;
    private Button pay;

    private String facebook_id;
    private String house_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        facebook_id = getArguments().getString("facebook_id");
        house_id = getArguments().getString("house_id");

        mContext = getContext();

        house_name = view.findViewById(R.id.tv_house_name);
        rv_members = view.findViewById(R.id.recycler_view);

        add_spending = view.findViewById(R.id.fragment_home_add_spending);
        add_spending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startAddSpendingActivity = new Intent(mContext,AddSpendingActivity.class);
                startAddSpendingActivity.putExtra("facebook_id", facebook_id);
                startAddSpendingActivity.putExtra("house_id", house_id);
                startActivity(startAddSpendingActivity);
            }
        });

        pay = view.findViewById(R.id.fragment_home_pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startPayActivity = new Intent(mContext,PayActivity.class);
                startPayActivity.putExtra("facebook_id", facebook_id);
                startPayActivity.putExtra("house_id", house_id);
                startActivity(startPayActivity);
            }
        });

        rv_members.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_members.setLayoutManager(layoutManager);
        mDividerItemDecoration = new DividerItemDecoration(rv_members.getContext(), layoutManager.getOrientation());
        rv_members.addItemDecoration(mDividerItemDecoration);

        memberAdapter = new MemberAdapter(mContext, this);
        rv_members.setAdapter(memberAdapter);

        btn_add_member = view.findViewById(R.id.btn_add_member);
        btn_add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        mContext);

                alertDialogBuilder.setTitle("Send the following code to the person you want to add.");

                alertDialogBuilder
                        .setMessage(house_id)
                        .setCancelable(true);

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();
            }
        });

        getActivity().getSupportLoaderManager().initLoader(HOUSE_LOADER_ID, null, this);

        getActivity().getSupportLoaderManager().initLoader(MEMBER_LOADER_ID, null, this);

        return view;
    }

    @Override
    public void onListItemClick(long clickedItemIndex, String member_facebook_id) {
        Intent intent = new Intent(getActivity(), MemberProfileActivity.class);
        Log.i("clickedItemIndex", "" + clickedItemIndex);
        Log.i("facebook_id", member_facebook_id);
        intent.putExtra("id", String.valueOf(clickedItemIndex));
        intent.putExtra("facebook_id", member_facebook_id);

        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().getSupportLoaderManager().restartLoader(MEMBER_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if(id == MEMBER_LOADER_ID){
            return new AsyncTaskLoader<Cursor>(getContext()) {

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
                        return getActivity().getContentResolver().query(CommunicatorContract.UserEntry.CONTENT_URI,
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

        if(id == HOUSE_LOADER_ID){
            return new AsyncTaskLoader<Cursor>(getContext()) {

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
                        return getActivity().getContentResolver().query(CommunicatorContract.HouseEntry.CONTENT_URI,
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
        if(loader.getId() == MEMBER_LOADER_ID){
            memberAdapter.swapCursor(data);
        }
        if(loader.getId() == HOUSE_LOADER_ID){
            if(data.moveToFirst()){
                String nameOfHouse = data.getString(data.getColumnIndex(CommunicatorContract.HouseEntry.COLUMN_NAME));
                house_name.setText(nameOfHouse);
                house_id = data.getString(data.getColumnIndex(CommunicatorContract.HouseEntry._ID));
                Log.i("house_id", house_id);
                CommunicatorSyncUtils.startImmediateSync(mContext, CommunicatorSyncTask.ACTION_UPDATE_MEMBERS, facebook_id, house_id);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(loader.getId() == MEMBER_LOADER_ID){
            memberAdapter.swapCursor(null);
        }
    }
}
