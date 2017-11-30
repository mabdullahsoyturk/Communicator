package com.example.muhammet.communicator.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.muhammet.communicator.ListItemClickListener;
import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.adapters.BuyMeAdapter;
import com.example.muhammet.communicator.models.BuyMe;
import com.example.muhammet.communicator.tasks.DeleteAllBuyMesTask;
import com.example.muhammet.communicator.tasks.FetchBuyMeTask;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

import java.net.MalformedURLException;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyMeFragment extends Fragment implements ListItemClickListener{

    private Button deleteAllButton;
    private ProgressBar mLoadingIndicator;
    BuyMe[] buyMes = {new BuyMe("aaa", "bbb"), new BuyMe("ccc", "ddd")};
    RecyclerView rv_buy_me;
    BuyMeAdapter toBuyAdapter;
    private DividerItemDecoration mDividerItemDecoration;

    private String user_id = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user_id = getArguments().getString("user_id");
        View view = inflater.inflate(R.layout.fragment_buy_me, container, false);

        deleteAllButton = view.findViewById(R.id.buy_me_delete);
        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteAllBuyMesTask deleteAllBuyMesTask = null;
                try {
                    deleteAllBuyMesTask = new DeleteAllBuyMesTask(getContext());
                    deleteAllBuyMesTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/5a1b12128351e60014b50505/houses/5a19e3d38ad03b25c85b23a4/buy_mes/delete_all");
                    FetchBuyMeTask fetchBuyMeTask = new FetchBuyMeTask(getContext(),toBuyAdapter);
                    fetchBuyMeTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/5a1b12128351e60014b50505/houses/5a19e3d38ad03b25c85b23a4/buy_mes");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        mLoadingIndicator = view.findViewById(R.id.pb_loading_indicator);
        rv_buy_me = view.findViewById(R.id.rv_buy_me);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        rv_buy_me.setLayoutManager(layoutManager);
        mDividerItemDecoration = new DividerItemDecoration(rv_buy_me.getContext(), layoutManager.getOrientation());
        rv_buy_me.addItemDecoration(mDividerItemDecoration);

        toBuyAdapter = new BuyMeAdapter(buyMes,this);
        rv_buy_me.setAdapter(toBuyAdapter);

        try {
            FetchBuyMeTask fetchBuyMeTask = new FetchBuyMeTask(getContext(),toBuyAdapter);
            fetchBuyMeTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/5a1b0d816058c0001439ae35/houses/5a1b12128351e60014b50505/buy_mes");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        FetchBuyMeTask fetchBuyMeTask = null;
        try {
            fetchBuyMeTask = new FetchBuyMeTask(getContext(),toBuyAdapter);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        fetchBuyMeTask.execute(NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/5a1b0d816058c0001439ae35/houses/5a1b12128351e60014b50505/buy_mes");
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }
}
