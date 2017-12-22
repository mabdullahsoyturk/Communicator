package com.example.muhammet.communicator.fragments;

import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.muhammet.communicator.activities.BaseActivity;
import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.listeners.ListItemClickListener;
import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.activities.MemberProfileActivity;
import com.example.muhammet.communicator.adapters.MemberAdapter;
import com.example.muhammet.communicator.sync.CommunicatorSyncTask;
import com.example.muhammet.communicator.sync.CommunicatorSyncUtils;
import com.example.muhammet.communicator.tasks.FetchHouseTask;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

import java.net.MalformedURLException;

public class HomeFragment extends Fragment implements ListItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final int MEMBER_LOADER_ID = 2;

    MemberAdapter memberAdapter;
    RecyclerView rv_members;
    private DividerItemDecoration mDividerItemDecoration;

    Context mContext;
    private TextView house_name;
    private Button btn_add_member;

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
        rv_members.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_members.setLayoutManager(layoutManager);
        mDividerItemDecoration = new DividerItemDecoration(rv_members.getContext(), layoutManager.getOrientation());
        rv_members.addItemDecoration(mDividerItemDecoration);

        memberAdapter = new MemberAdapter(mContext, this);
        rv_members.setAdapter(memberAdapter);

        try {
            FetchHouseTask fetchHouseTask = new FetchHouseTask(mContext, house_name, facebook_id);
            fetchHouseTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/" + facebook_id + "/houses");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

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

        getActivity().getSupportLoaderManager().initLoader(MEMBER_LOADER_ID, null, this);

        Log.i("HouseOnHome", house_id);
        CommunicatorSyncUtils.startImmediateSync(mContext, CommunicatorSyncTask.ACTION_UPDATE_MEMBERS, facebook_id, house_id);

        restartLoader();

        return view;
    }

    @Override
    public void onListItemClick(long clickedItemIndex) {
        Intent intent = new Intent(getActivity(), MemberProfileActivity.class);
        intent.putExtra("id", clickedItemIndex);

        startActivity(intent);
    }

    public void restartLoader(){
        getActivity().getSupportLoaderManager().restartLoader(MEMBER_LOADER_ID, null, this);
        memberAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

        CommunicatorSyncUtils.startImmediateSync(mContext,CommunicatorSyncTask.ACTION_UPDATE_MEMBERS,facebook_id, house_id);
        getActivity().getSupportLoaderManager().restartLoader(MEMBER_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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
                            "house_id=?",
                            new String[]{house_id},
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
        memberAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        memberAdapter.swapCursor(null);
    }
}
