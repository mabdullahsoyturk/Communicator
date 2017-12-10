package com.example.muhammet.communicator.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.muhammet.communicator.ListItemClickListener;
import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.activities.BaseActivity;
import com.example.muhammet.communicator.adapters.MemberAdapter;
import com.example.muhammet.communicator.adapters.SpendingAdapter;
import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.models.Spending;
import com.example.muhammet.communicator.tasks.FetchMembersTask;
import com.example.muhammet.communicator.tasks.FetchSpendingsTask;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class SpendingsFragment extends Fragment implements ListItemClickListener,LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final int SPENDING_LOADER_ID = 1;
    Context mContext = getContext();
    ProgressBar progressBar;

    RecyclerView rv_spendings;
    SpendingAdapter spendingAdapter;
    private DividerItemDecoration mDividerItemDecoration;

    private String user_id;
    private String house_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user_id = getArguments().getString("user_id");
        house_id = getArguments().getString("house_id");
        View view = inflater.inflate(R.layout.fragment_spendings, container, false);

        rv_spendings = view.findViewById(R.id.rv_spendings);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        rv_spendings.setLayoutManager(layoutManager);
        mDividerItemDecoration = new DividerItemDecoration(rv_spendings.getContext(), layoutManager.getOrientation());
        rv_spendings.addItemDecoration(mDividerItemDecoration);

        spendingAdapter = new SpendingAdapter(getContext(),this);
        rv_spendings.setAdapter(spendingAdapter);

//        try {
//            FetchSpendingsTask fetchSpendingsTask = new FetchSpendingsTask(getContext(), spendingAdapter);
//            fetchSpendingsTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/" + user_id + "/houses/" + house_id + "/spendings");
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

//        try {
//            SharedPreferences prefs       = PreferenceManager.getDefaultSharedPreferences(getContext());
//            String            limit    = prefs.getString("time_period", "All");
//
//            String query = "";
//            if(limit.equals("Last Week")){
//                query = "7";
//            }else if(limit.equals("Last Month")){
//                query = "30";
//            }else if(limit.equals("Last Year")){
//                query = "365";
//            }
//
//            FetchSpendingsTask fetchSpendingsTask = new FetchSpendingsTask(getContext(),spendingAdapter);
//            fetchSpendingsTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/5a1b0d816058c0001439ae35/houses/5a1b12128351e60014b50505/spendings?limit=" +query);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

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
                    return getActivity().getContentResolver().query(CommunicatorContract.SpendingEntry.CONTENT_URI,
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
        spendingAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        spendingAdapter.swapCursor(null);
    }
}
