package com.example.muhammet.communicator.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muhammet.communicator.ListItemClickListener;
import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.models.Member;
import com.example.muhammet.communicator.models.Spending;

import java.util.List;

public class SpendingAdapter extends RecyclerView.Adapter<SpendingAdapter.SpendingAdapterViewHolder> {

    Context mContext;
    Cursor mCursor;
    
    private ListItemClickListener mOnClickListener;

    public SpendingAdapter(Context context, ListItemClickListener listener){
        mOnClickListener = listener;
        mContext = context;
    }

    class SpendingAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ListItemClickListener mListener;
        private TextView spendingName;
        private TextView spendingDate;
        private TextView spendingShare;
        private ImageView spendingIcon;
        private Button button;

        public SpendingAdapterViewHolder(View itemView, ListItemClickListener listener) {
            super(itemView);
            mListener = listener;

            spendingName = itemView.findViewById(R.id.spending_name);
            spendingDate = itemView.findViewById(R.id.spending_date);
            spendingShare = itemView.findViewById(R.id.spending_share);
            spendingIcon = itemView.findViewById(R.id.spending_icon);
            button = itemView.findViewById(R.id.spending_button);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListener.onListItemClick(clickedPosition);
        }
    }

    @Override
    public SpendingAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.spending_row;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new SpendingAdapterViewHolder(view, mOnClickListener);
    }

    @Override
    public void onBindViewHolder(SpendingAdapterViewHolder holder, int position) {
        int idIndex = mCursor.getColumnIndex(CommunicatorContract.SpendingEntry._ID);
        int nameIndex = mCursor.getColumnIndex(CommunicatorContract.SpendingEntry.COLUMN_NAME);
        int costIndex = mCursor.getColumnIndex(CommunicatorContract.SpendingEntry.COLUMN_COST);
        int userIdIndex = mCursor.getColumnIndex(CommunicatorContract.SpendingEntry.COLUMN_USER_ID);
        int houseIdIndex = mCursor.getColumnIndex(CommunicatorContract.SpendingEntry.COLUMN_HOUSE_ID);
        int createdTimeIndex = mCursor.getColumnIndex(CommunicatorContract.SpendingEntry.COLUMN_CREATED_TIME);

        mCursor.moveToPosition(position);

        final int id = mCursor.getInt(idIndex);
        String name = mCursor.getString(nameIndex);
        double cost = mCursor.getDouble(costIndex);
        int userId = mCursor.getInt(userIdIndex);
        int houseId = mCursor.getInt(houseIdIndex);
        String createdTime = mCursor.getString(createdTimeIndex);

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

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

}
