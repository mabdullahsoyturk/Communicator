package com.example.muhammet.communicator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muhammet.communicator.ListItemClickListener;
import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.activities.MemberProfileActivity;
import com.example.muhammet.communicator.adapters.MemberAdapter;
import com.example.muhammet.communicator.models.Member;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ListItemClickListener {

    private List<Member> members;
    private static final int itemNumber = 4;
    MemberAdapter memberAdapter;
    RecyclerView rv_members;
    private DividerItemDecoration mDividerItemDecoration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        members = new ArrayList<Member>();

        members.add(new Member(R.drawable.icon_profile_empty, "Muhammet", "0.00 $"));
        members.add(new Member(R.drawable.icon_profile_empty, "Yakup", "0.00 $"));
        members.add(new Member(R.drawable.icon_profile_empty, "Burhan", "0.00 $"));
        members.add(new Member(R.drawable.icon_profile_empty, "Mertcan", "0.00 $"));

        rv_members = view.findViewById(R.id.recycler_view);
        rv_members.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_members.setLayoutManager(layoutManager);
        mDividerItemDecoration = new DividerItemDecoration(rv_members.getContext(), layoutManager.getOrientation());
        rv_members.addItemDecoration(mDividerItemDecoration);

        memberAdapter = new MemberAdapter(itemNumber, members, this);
        rv_members.setAdapter(memberAdapter);

        return view;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(getActivity(), MemberProfileActivity.class);

        String member_name = members.get(clickedItemIndex).getMember_name();
        String member_debt = members.get(clickedItemIndex).getMember_debt();

        intent.putExtra("member_name", member_name);
        intent.putExtra("member_debt", member_debt);

        startActivity(intent);
    }
}
