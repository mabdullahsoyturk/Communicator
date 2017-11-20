package com.example.muhammet.communicator.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muhammet.communicator.ListItemClickListener;
import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.models.BuyMe;
import com.example.muhammet.communicator.models.Member;

import java.util.List;

/**
 * Created by Muhammet on 12.11.2017.
 */

public class BuyMeAdapter extends RecyclerView.Adapter<BuyMeAdapter.BuyMeAdapterViewHolder>{

    private Cursor mCursor;
    private Context mContext;
    private ListItemClickListener mOnClickListener;
    
    public BuyMeAdapter(Context context, ListItemClickListener listener){
        mContext = context;
        mOnClickListener = listener;
    }

    class BuyMeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ListItemClickListener mListener;
        private TextView buy_me_name;
        private TextView buy_me_description;

        public BuyMeAdapterViewHolder(View itemView, ListItemClickListener listener) {
            super(itemView);
            mListener = listener;

            buy_me_name = itemView.findViewById(R.id.buy_me_name);
            buy_me_description = itemView.findViewById(R.id.buy_me_description);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListener.onListItemClick(clickedPosition);
        }
    }


    @Override
    public BuyMeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.buy_me_row;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new BuyMeAdapter.BuyMeAdapterViewHolder(view, mOnClickListener);
    }

    @Override
    public void onBindViewHolder(BuyMeAdapterViewHolder holder, int position) {
        int idIndex = mCursor.getColumnIndex(CommunicatorContract.BuyMeEntry._ID);
        int nameIndex = mCursor.getColumnIndex(CommunicatorContract.BuyMeEntry.COLUMN_NAME);
        int descriptionIndex = mCursor.getColumnIndex(CommunicatorContract.BuyMeEntry.COLUMN_DESCRIPTION);

        mCursor.moveToPosition(position);

        final int id = mCursor.getInt(idIndex);
        String name = mCursor.getString(nameIndex);
        String description = mCursor.getString(descriptionIndex);

        holder.itemView.setTag(id);
        holder.buy_me_name.setText(name);
        holder.buy_me_description.setText(description);

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
