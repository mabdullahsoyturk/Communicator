package com.example.muhammet.communicator.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.listeners.ListItemClickListener;
import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.models.Member;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberAdapterViewHolder> {

    Context mContext;
    Cursor mCursor;
    public ListItemClickListener mOnClickListener;

    public MemberAdapter(Context context, ListItemClickListener listener){
        mContext = context;
        mOnClickListener = listener;
    }

    class MemberAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ListItemClickListener mListener;
        private ImageView member_img;
        private TextView member_name;
        private TextView member_debt;
        private long id;

        public MemberAdapterViewHolder(View itemView, ListItemClickListener listener) {
            super(itemView);

            member_img = itemView.findViewById(R.id.member_img);
            member_name = itemView.findViewById(R.id.member_name);
            member_debt = itemView.findViewById(R.id.member_debt);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        public void bind(long id){
            this.id = id;
        }

        @Override
        public void onClick(View view) {
            mListener.onListItemClick(id);
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
        int idIndex          = mCursor.getColumnIndex(CommunicatorContract.UserEntry._ID);
        int firstNameIndex   = mCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_FIRST_NAME);
        int lastNameIndex    = mCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_LAST_NAME);
        int emailIndex       = mCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_EMAIL);
        int balanceIndex     = mCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_BALANCE);
        int photoIndex       = mCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_PHOTO_URL);
        int statusIndex      = mCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_STATUS);
        int createdTımeIndex = mCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_CREATED_TIME);
        int facebookIdIndex  = mCursor.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_FACEBOOK_ID);

        mCursor.moveToPosition(position);

        long id = mCursor.getLong(idIndex);
        String firstName = mCursor.getString(firstNameIndex);
        String lastName = mCursor.getString(lastNameIndex);
        String emailName = mCursor.getString(emailIndex);
        double balance = mCursor.getDouble(balanceIndex);
        String photo   = mCursor.getString(photoIndex);
        int status     = mCursor.getInt(statusIndex);
        String createdTime = mCursor.getString(createdTımeIndex);
        String facebookId = mCursor.getString(facebookIdIndex);

        Uri uri = Uri.parse(photo);

        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(mContext)
                .load(uri)
                .transform(transformation)
                .into(holder.member_img);


        holder.bind(id);
        holder.member_name.setText(firstName);
        holder.member_debt.setText(String.valueOf(balance));
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
