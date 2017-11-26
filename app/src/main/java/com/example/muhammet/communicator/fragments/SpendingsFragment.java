package com.example.muhammet.communicator.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muhammet.communicator.ListItemClickListener;
import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.adapters.MemberAdapter;
import com.example.muhammet.communicator.adapters.SpendingAdapter;
import com.example.muhammet.communicator.models.Spending;
import com.example.muhammet.communicator.tasks.FetchMembersTask;
import com.example.muhammet.communicator.tasks.FetchSpendingsTask;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class SpendingsFragment extends Fragment implements ListItemClickListener {

    Spending[] spendings = {new Spending("abcde","10.11.2017","Your Share: 2.75$", R.drawable.ic_borrowing_money_black_24dp, "400"),
            new Spending("abcde","08.11.2017","Your Share: 1.25$", R.drawable.ic_food_black_24dp, "400"),
            new Spending("abcde","07.11.2017","Your Share: 1.75$", R.drawable.ic_home_supplies_black_24dp, "400"),
            new Spending("abcde","05.11.2017","Your Share: 2.25$", R.drawable.ic_internet_black_24dp, "400")};
    RecyclerView rv_spendings;
    SpendingAdapter spendingAdapter;
    private DividerItemDecoration mDividerItemDecoration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_spendings, container, false);

        rv_spendings = view.findViewById(R.id.rv_spendings);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        rv_spendings.setLayoutManager(layoutManager);
        mDividerItemDecoration = new DividerItemDecoration(rv_spendings.getContext(), layoutManager.getOrientation());
        rv_spendings.addItemDecoration(mDividerItemDecoration);

        spendingAdapter = new SpendingAdapter(spendings, this);
        rv_spendings.setAdapter(spendingAdapter);

        try {
            SharedPreferences prefs       = PreferenceManager.getDefaultSharedPreferences(getContext());
            String            limit    = prefs.getString("time_period", "All");

            String query = "";
            if(limit.equals("Last Week")){
                query = "7";
            }else if(limit.equals("Last Month")){
                query = "30";
            }else if(limit.equals("Last Year")){
                query = "365";
            }

            FetchSpendingsTask fetchSpendingsTask = new FetchSpendingsTask(getContext(),spendingAdapter);
            fetchSpendingsTask.execute("http://10.0.2.2:3000/api/users/5a19e38c8ad03b25c85b23a3/houses/5a19e3d38ad03b25c85b23a4/spendings?limit=" +query);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }
}
