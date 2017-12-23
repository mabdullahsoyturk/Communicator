package com.example.muhammet.communicator.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.data.CommunicatorContract;

public class MemberProfileAdapter extends RecyclerView.Adapter<MemberProfileAdapter.MemberProfileAdapterViewHolder> {

    Context mContext;
    Cursor mCursor;

    public MemberProfileAdapter(Context context){
        mContext         = context;
    }

    class MemberProfileAdapterViewHolder extends RecyclerView.ViewHolder{

        private TextView spendingName;
        private TextView spendingDate;
        private TextView spendingShare;
        private long id;

        public MemberProfileAdapterViewHolder(View itemView) {
            super(itemView);

            spendingName = itemView.findViewById(R.id.member_spending_name);
            spendingDate = itemView.findViewById(R.id.member_spending_date);
            spendingShare = itemView.findViewById(R.id.member_spending_share);
        }

        public void bind(long id){
            this.id = id;
        }
    }

    @Override
    public MemberProfileAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.member_spendings_row;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MemberProfileAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MemberProfileAdapterViewHolder holder, int position) {
        int idIndex = mCursor.getColumnIndex(CommunicatorContract.SpendingEntry._ID);
        int nameIndex = mCursor.getColumnIndex(CommunicatorContract.SpendingEntry.COLUMN_NAME);
        int costIndex = mCursor.getColumnIndex(CommunicatorContract.SpendingEntry.COLUMN_COST);
        int facebookIdIndex = mCursor.getColumnIndex(CommunicatorContract.SpendingEntry.COLUMN_FACEBOOK_ID);
        int houseIdIndex = mCursor.getColumnIndex(CommunicatorContract.SpendingEntry.COLUMN_HOUSE_ID);
        int createdTimeIndex = mCursor.getColumnIndex(CommunicatorContract.SpendingEntry.COLUMN_CREATED_TIME);

        mCursor.moveToPosition(position);

        final long id = mCursor.getLong(idIndex);
        String name = mCursor.getString(nameIndex);
        double cost = mCursor.getDouble(costIndex);
        int facebookId = mCursor.getInt(facebookIdIndex);
        int houseId = mCursor.getInt(houseIdIndex);
        String createdTime = mCursor.getString(createdTimeIndex);

        holder.bind(id);
        holder.itemView.setTag(id);
        holder.spendingName.setText(name);
        holder.spendingShare.setText(String.valueOf(cost));
        holder.spendingDate.setText(createdTime);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

}
