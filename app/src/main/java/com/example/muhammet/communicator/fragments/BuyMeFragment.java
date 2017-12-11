package com.example.muhammet.communicator.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ProgressBar;

import com.example.muhammet.communicator.ListItemClickListener;
import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.activities.BaseActivity;
import com.example.muhammet.communicator.adapters.BuyMeAdapter;
import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.models.BuyMe;
import com.example.muhammet.communicator.sync.CommunicatorSyncUtils;
import com.example.muhammet.communicator.tasks.DeleteAllBuyMesTask;
import com.example.muhammet.communicator.tasks.FetchBuyMeTask;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

import java.net.MalformedURLException;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyMeFragment extends Fragment implements ListItemClickListener, LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final int BUY_ME_LOADER_ID = 0;
    Context mContext;
    BroadcastReceiver broadcastReceiver;

    private Button deleteAllButton;
    RecyclerView mRecyclerView;
    BuyMeAdapter buyMeAdapter;
    private DividerItemDecoration mDividerItemDecoration;

    private String user_id;
    private String house_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user_id = getArguments().getString("user_id");
        house_id = getArguments().getString("house_id");
        View view = inflater.inflate(R.layout.fragment_buy_me, container, false);

        mContext = getContext();

        deleteAllButton = view.findViewById(R.id.buy_me_delete);
        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DeleteAllBuyMesTask deleteAllBuyMesTask = new DeleteAllBuyMesTask(getContext(), buyMeAdapter, user_id,house_id);
                    deleteAllBuyMesTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/" + user_id + "/houses/" + house_id + "/buy_mes");
                    buyMeAdapter.notifyDataSetChanged();
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

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete

                // COMPLETED (1) Construct the URI for the item to delete
                //[Hint] Use getTag (from the adapter code) to get the id of the swiped item
                // Retrieve the id of the task to delete
                int id = (int) viewHolder.itemView.getTag();

                // Build appropriate uri with String row id appended
                String stringId = Integer.toString(id);
                Uri uri = CommunicatorContract.BuyMeEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                // COMPLETED (2) Delete a single row of data using a ContentResolver
                getActivity().getContentResolver().delete(uri, null, null);

                // COMPLETED (3) Restart the loader to re-query for all tasks after a deletion
                restartLoader();
            }
        }).attachToRecyclerView(mRecyclerView);

//        try {
//            FetchBuyMeTask fetchBuyMeTask = new FetchBuyMeTask(getContext(), buyMeAdapter);
//            fetchBuyMeTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/" + user_id + "/houses/" + house_id + "/buy_mes");
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

        getActivity().getSupportLoaderManager().initLoader(BUY_ME_LOADER_ID, null, this);

        CommunicatorSyncUtils.startImmediateSync(mContext,user_id, house_id);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent ıntent) {
                CommunicatorSyncUtils.startImmediateSync(mContext, user_id, house_id);
            }
        };

        return view;
    }

    public void restartLoader(){
        getActivity().getSupportLoaderManager().restartLoader(BUY_ME_LOADER_ID, null, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mContext.registerReceiver(broadcastReceiver, new IntentFilter(CommunicatorContract.UI_UPDATE_BROADCAST));
    }

    @Override
    public void onPause() {
        super.onPause();
        mContext.unregisterReceiver(broadcastReceiver);
    }

    @Override
   public void onResume() {
        super.onResume();

        getActivity().getSupportLoaderManager().restartLoader(BUY_ME_LOADER_ID, null, this);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        
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
                    return getActivity().getContentResolver().query(CommunicatorContract.BuyMeEntry.CONTENT_URI,
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
        buyMeAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        buyMeAdapter.swapCursor(null);
    }
}
