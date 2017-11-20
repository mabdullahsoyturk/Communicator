package com.example.muhammet.communicator.fragments;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class SpendingsFragment extends Fragment implements ListItemClickListener {

    List<Spending> spendings;
    RecyclerView rv_spendings;
    SpendingAdapter spendingAdapter;
    private DividerItemDecoration mDividerItemDecoration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_spendings, container, false);

        spendings = new ArrayList<Spending>();

        spendings.add(new Spending("abcde","10.11.2017","Your Share: 2.75$", R.drawable.ic_borrowing_money_black_24dp, "400"));
        spendings.add(new Spending("abcde","08.11.2017","Your Share: 1.25$", R.drawable.ic_food_black_24dp, "400"));
        spendings.add(new Spending("abcde","07.11.2017","Your Share: 1.75$", R.drawable.ic_home_supplies_black_24dp, "400"));
        spendings.add(new Spending("abcde","05.11.2017","Your Share: 2.25$", R.drawable.ic_internet_black_24dp, "400"));
        spendings.add(new Spending("abcde","04.11.2017","Your Share: 3.35$", R.drawable.ic_maintenance_black_24dp, "400"));
        spendings.add(new Spending("abcde","03.11.2017","Your Share: 7.15$", R.drawable.ic_others_black_24dp, "400"));
        spendings.add(new Spending("abcde","01.11.2017","Your Share: 9.30$", R.drawable.ic_utilities_black_24dp, "400"));


        rv_spendings = view.findViewById(R.id.rv_spendings);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        rv_spendings.setLayoutManager(layoutManager);
        mDividerItemDecoration = new DividerItemDecoration(rv_spendings.getContext(), layoutManager.getOrientation());
        rv_spendings.addItemDecoration(mDividerItemDecoration);

        spendingAdapter = new SpendingAdapter(spendings, this);
        rv_spendings.setAdapter(spendingAdapter);

        return view;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }
}
