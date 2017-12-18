package com.example.muhammet.communicator.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.example.muhammet.communicator.listeners.ListItemClickListener;
import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.activities.MemberProfileActivity;
import com.example.muhammet.communicator.adapters.MemberAdapter;
import com.example.muhammet.communicator.models.Member;
import com.example.muhammet.communicator.tasks.FetchHouseTask;
import com.example.muhammet.communicator.utilities.NetworkUtilities;

import java.net.MalformedURLException;

public class HomeFragment extends Fragment implements ListItemClickListener {

    Member[] members = {new Member(R.drawable.icon_profile_empty, "Muhammet", 0.00),
            new Member(R.drawable.icon_profile_empty, "Yakup", 0.00),
            new Member(R.drawable.icon_profile_empty, "Burhan", 0.00),
            new Member(R.drawable.icon_profile_empty, "Mertcan", 0.00 )
    };
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

        memberAdapter = new MemberAdapter(members, this);
        rv_members.setAdapter(memberAdapter);

        try {
            Log.i("home", "" + facebook_id);
            FetchHouseTask fetchHouseTask = new FetchHouseTask(mContext, house_name, memberAdapter, facebook_id);
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

        return view;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(getActivity(), MemberProfileActivity.class);

        String member_name = members[clickedItemIndex].getFirstName();
        double member_debt = members[clickedItemIndex].getBalance();

        intent.putExtra("member_name", member_name);
        intent.putExtra("member_debt", member_debt);

        startActivity(intent);
    }
}
