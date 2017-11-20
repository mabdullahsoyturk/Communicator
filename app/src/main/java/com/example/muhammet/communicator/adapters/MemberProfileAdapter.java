package com.example.muhammet.communicator.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muhammet.communicator.ListItemClickListener;
import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.models.Member;

import java.util.List;

/**
 * Created by Muhammet on 13.11.2017.
 */

public class MemberProfileAdapter extends RecyclerView.Adapter<MemberProfileAdapter.MemberProfileAdapterViewHolder> {

    List<Member> memberProfiles;
    private ListItemClickListener mOnClickListener;

    public MemberProfileAdapter(List<Member> memberProfiles, ListItemClickListener listener){
        this.memberProfiles = memberProfiles;
        mOnClickListener = listener;
    }

    class MemberProfileAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ListItemClickListener mListener;
        private ImageView member_photo;
        private TextView member_name;
        private TextView member_debt;

        public MemberProfileAdapterViewHolder(View itemView, ListItemClickListener listener) {
            super(itemView);

            member_photo = itemView.findViewById(R.id.iv_member_photo);
            member_name = itemView.findViewById(R.id.tv_member_name);
            member_debt = itemView.findViewById(R.id.tv_member_debt);
            mListener = listener;
        }

        public void bind(Member member){
            member_photo.setImageResource(member.getMember_icon_id());
            member_name.setText(member.getMember_name());
            member_debt.setText(member.getMember_debt());
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListener.onListItemClick(clickedPosition);
        }
    }

    @Override
    public MemberProfileAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.member_row;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MemberProfileAdapterViewHolder(view, mOnClickListener);
    }

    @Override
    public void onBindViewHolder(MemberProfileAdapterViewHolder holder, int position) {
        Member member = memberProfiles.get(position);
        holder.bind(member);
    }

    @Override
    public int getItemCount() {
        return memberProfiles.size();
    }

    public void setMemberData(List<Member> memberData) {
        memberProfiles = memberData;
        notifyDataSetChanged();
    }

}
