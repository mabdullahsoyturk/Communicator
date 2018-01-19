package com.example.muhammet.communicator.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.muhammet.communicator.listeners.AsyncTaskFinishedObserver;
import com.example.muhammet.communicator.listeners.BuyMeSpendingItemClickListener;
import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.activities.BaseActivity;
import com.example.muhammet.communicator.adapters.SpendingAdapter;
import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.sync.CommunicatorSyncTask;
import com.example.muhammet.communicator.sync.CommunicatorSyncUtils;
import com.example.muhammet.communicator.tasks.DeleteSpendingTask;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

public class SpendingsFragment extends Fragment implements
        BuyMeSpendingItemClickListener
        ,LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final int SPENDING_LOADER_ID = 1;
    Context mContext;
    ProgressBar progressBar;

    RecyclerView mRecyclerView;
    SpendingAdapter spendingAdapter;
    private DividerItemDecoration mDividerItemDecoration;

    private String facebook_id;
    private String house_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_spendings, container, false);

        mContext = getContext();

        facebook_id = getArguments().getString("facebook_id");
        house_id = getArguments().getString("house_id");

        mRecyclerView = view.findViewById(R.id.rv_spendings);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        spendingAdapter = new SpendingAdapter(getContext(),this);
        mRecyclerView.setAdapter(spendingAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                long id = (long)viewHolder.itemView.getTag();

                String stringId = Long.toString(id);

                DeleteSpendingTask deleteSpendingTask = new DeleteSpendingTask(getContext(), facebook_id, house_id, stringId, new AsyncTaskFinishedObserver() {
                    @Override
                    public void isFinished(String s) {
                        restartLoader();
                    }
                });
                deleteSpendingTask.execute(NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id, house_id) + "/spendings/" + stringId);
            }
        }).attachToRecyclerView(mRecyclerView);

        getActivity().getSupportLoaderManager().initLoader(SPENDING_LOADER_ID, null, this);

        return view;
    }

    public void restartLoader(){
        getActivity().getSupportLoaderManager().restartLoader(SPENDING_LOADER_ID, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().getSupportLoaderManager().restartLoader(SPENDING_LOADER_ID, null, this);

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
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public Cursor loadInBackground() {
                
                try {
                    return getActivity().getContentResolver().query(CommunicatorContract.SpendingEntry.CONTENT_URI,
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
        spendingAdapter.swapCursor(data);
        if(data.moveToFirst()){
            house_id = data.getString(data.getColumnIndex(CommunicatorContract.SpendingEntry.COLUMN_HOUSE_ID));
            CommunicatorSyncUtils.startImmediateSync(mContext, CommunicatorSyncTask.ACTION_UPDATE_SPENDINGS,facebook_id, house_id);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        spendingAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(long id) {

    }

    @Override
    public void onDeleteClicked(long id) {
        //ServiceUtils.deleteSpendingService(mContext, ServiceTasks.ACTION_DELETE_SPENDING, facebook_id, house_id, String.valueOf(id));
        DeleteSpendingTask deleteBuyMeTask = new DeleteSpendingTask(getContext(), facebook_id, house_id, String.valueOf(id),new AsyncTaskFinishedObserver() {
            @Override
            public void isFinished(String s) {
                restartLoader();
            }
        });
        deleteBuyMeTask.execute(NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id,house_id) + "/spendings/" + String.valueOf(id));
    }

}
