package com.example.muhammet.communicator.fragments;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.muhammet.communicator.ListItemClickListener;
import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.adapters.BuyMeAdapter;
import com.example.muhammet.communicator.data.CommunicatorContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyMeFragment extends Fragment implements ListItemClickListener,
                                                        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int BUY_ME_LOADER_ID = 0;

    private Button deleteAllButton;
    private ProgressBar mLoadingIndicator;
    RecyclerView rv_buy_me;
    BuyMeAdapter toBuyAdapter;
    private DividerItemDecoration mDividerItemDecoration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_buy_me, container, false);

        deleteAllButton = view.findViewById(R.id.buy_me_delete);
        mLoadingIndicator = view.findViewById(R.id.pb_loading_indicator);
        rv_buy_me = view.findViewById(R.id.rv_buy_me);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        rv_buy_me.setLayoutManager(layoutManager);
        mDividerItemDecoration = new DividerItemDecoration(rv_buy_me.getContext(), layoutManager.getOrientation());
        rv_buy_me.addItemDecoration(mDividerItemDecoration);

        toBuyAdapter = new BuyMeAdapter(getContext(),this);
        rv_buy_me.setAdapter(toBuyAdapter);

        setSwipeHelper();
        setDeleteButton();

        return view;
    }

    public void restartLoader(){
        getActivity().getSupportLoaderManager().restartLoader(BUY_ME_LOADER_ID, null, this);
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

            Cursor mBuyMeData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mBuyMeData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mBuyMeData);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    return getActivity().getContentResolver().query(CommunicatorContract.BuyMeEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mBuyMeData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        toBuyAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        toBuyAdapter.swapCursor(null);
    }

    public void setSwipeHelper(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

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
        }).attachToRecyclerView(rv_buy_me);
    }

    public void setDeleteButton(){
        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Uri uri = CommunicatorContract.BuyMeEntry.CONTENT_URI;
                    getActivity().getContentResolver().delete(uri, null, null);
                    restartLoader();
            }
        });
    }
}
