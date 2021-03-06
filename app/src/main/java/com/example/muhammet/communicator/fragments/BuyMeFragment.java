package com.example.muhammet.communicator.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Button;

import com.example.muhammet.communicator.activities.AddBuyMeActivity;
import com.example.muhammet.communicator.listeners.AsyncTaskFinishedObserver;
import com.example.muhammet.communicator.listeners.BuyMeSpendingItemClickListener;
import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.activities.BaseActivity;
import com.example.muhammet.communicator.adapters.BuyMeAdapter;
import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.sync.CommunicatorSyncTask;
import com.example.muhammet.communicator.sync.CommunicatorSyncUtils;
import com.example.muhammet.communicator.tasks.DeleteAllBuyMesTask;
import com.example.muhammet.communicator.tasks.DeleteBuyMeTask;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

import java.net.MalformedURLException;

public class BuyMeFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        BuyMeSpendingItemClickListener{

    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final int BUY_ME_LOADER_ID = 0;
    Context mContext;

    private Button deleteAllButton;
    private FloatingActionButton addBuyMeButton;
    RecyclerView mRecyclerView;
    BuyMeAdapter buyMeAdapter;
    private DividerItemDecoration mDividerItemDecoration;

    private String facebook_id;
    //private String house_id;
    private String house_id_server;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_buy_me, container, false);

        facebook_id = getArguments().getString("facebook_id");
        //house_id = getArguments().getString("house_id");
        house_id_server = getArguments().getString("house_id_server");

        mContext = getContext();

        addBuyMeButton = view.findViewById(R.id.buy_me_button_add);
        addBuyMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startAddBuyMeActivity = new Intent(mContext,AddBuyMeActivity.class);
                startAddBuyMeActivity.putExtra("facebook_id", facebook_id);
                //startAddBuyMeActivity.putExtra("house_id", house_id);
                startAddBuyMeActivity.putExtra("house_id_server", house_id_server);
                startActivity(startAddBuyMeActivity);
            }
        });

        deleteAllButton = view.findViewById(R.id.buy_me_delete);
        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ServiceUtils.deleteAllBuyMesService(mContext, ServiceTasks.ACTION_DELETE_ALL_BUY_MES, facebook_id, house_id);
                try {
                    DeleteAllBuyMesTask deleteAllBuyMesTask = new DeleteAllBuyMesTask(getContext(), buyMeAdapter, facebook_id, house_id_server, new AsyncTaskFinishedObserver() {
                        @Override
                        public void isFinished(String s) {
                            restartLoader();
                        }
                    });
                    deleteAllBuyMesTask.execute(NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id,house_id_server) + "/buy_mes");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        mRecyclerView = view.findViewById(R.id.rv_buy_me);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        buyMeAdapter = new BuyMeAdapter(mContext, this);
        mRecyclerView.setAdapter(buyMeAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                long id = (long)viewHolder.itemView.getTag();

                String stringId = Long.toString(id);

                DeleteBuyMeTask deleteBuyMeTask = new DeleteBuyMeTask(getContext(), facebook_id, house_id_server, stringId,new AsyncTaskFinishedObserver() {
                    @Override
                    public void isFinished(String s) {
                        restartLoader();
                    }
                });
                deleteBuyMeTask.execute(NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id,house_id_server) + "/buy_mes/" + stringId);
            }
        }).attachToRecyclerView(mRecyclerView);

        getActivity().getSupportLoaderManager().initLoader(BUY_ME_LOADER_ID, null, this);

        return view;
    }

    public void restartLoader(){
        getActivity().getSupportLoaderManager().restartLoader(BUY_ME_LOADER_ID, null, this);
        buyMeAdapter.notifyDataSetChanged();
    }

    @Override
   public void onResume() {
        super.onResume();

        restartLoader();
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
                    return getActivity().getContentResolver().query(CommunicatorContract.BuyMeEntry.CONTENT_URI,
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
        buyMeAdapter.swapCursor(data);
        if(data.moveToFirst()){
            house_id_server = data.getString(data.getColumnIndex(CommunicatorContract.BuyMeEntry.COLUMN_HOUSE_ID_SERVER));
            CommunicatorSyncUtils.startImmediateSync(mContext, CommunicatorSyncTask.ACTION_UPDATE_BUY_MES, facebook_id, house_id_server);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        buyMeAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(long id) {

    }

    @Override
    public void onDeleteClicked(long id) {
        //ServiceUtils.deleteBuyMeService(mContext, ServiceTasks.ACTION_DELETE_BUY_ME, facebook_id, house_id, String.valueOf(id));
        DeleteBuyMeTask deleteBuyMeTask = new DeleteBuyMeTask(getContext(), facebook_id, house_id_server, String.valueOf(id),new AsyncTaskFinishedObserver() {
            @Override
            public void isFinished(String s) {
                restartLoader();
            }
        });
        deleteBuyMeTask.execute(NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id,house_id_server) + "/buy_mes/" + String.valueOf(id));
    }

}
