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
import com.example.muhammet.communicator.utilities.NetworkUtilities;

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

        spendingAdapter = new SpendingAdapter(spendings, this);
        rv_spendings.setAdapter(spendingAdapter);

        try {
            FetchSpendingsTask fetchSpendingsTask = new FetchSpendingsTask(getContext(), spendingAdapter);
            fetchSpendingsTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/" + user_id + "/houses/" + house_id + "/spendings");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

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

        return view;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }
}
