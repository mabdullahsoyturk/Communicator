package com.example.muhammet.communicator.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muhammet.communicator.ListItemClickListener;
import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.models.Member;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberAdapterViewHolder> {

    Member[] members;
    public ListItemClickListener mOnClickListener;

    public MemberAdapter(Member[] members, ListItemClickListener listener){
        this.members = members;
        mOnClickListener = listener;
    }

    class MemberAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ListItemClickListener mListener;
        private ImageView member_img;
        private TextView member_name;
        private TextView member_debt;

        public MemberAdapterViewHolder(View itemView, ListItemClickListener listener) {
            super(itemView);

            member_img = itemView.findViewById(R.id.member_img);
            member_name = itemView.findViewById(R.id.member_name);
            member_debt = itemView.findViewById(R.id.member_debt);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        public void bind(Member member){
            member_img.setImageResource(member.getIcon_id());
            member_name.setText(member.getFirstName());
            member_debt.setText(String.valueOf(member.getBalance()));
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListener.onListItemClick(clickedPosition);
        }
    }

    @Override
    public MemberAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.member_row;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MemberAdapterViewHolder viewHolder = new MemberAdapterViewHolder(view, mOnClickListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MemberAdapterViewHolder holder, int position) {
        holder.bind(members[position]);
    }

    @Override
    public int getItemCount() {
        if(members == null){
            return 0;
        }

        return members.length;
    }

    public void setMemberData(Member[] memberData) {
        members = memberData;
        notifyDataSetChanged();
    }

}
