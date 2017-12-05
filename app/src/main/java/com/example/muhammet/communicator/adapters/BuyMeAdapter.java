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
import com.example.muhammet.communicator.models.BuyMe;
import com.example.muhammet.communicator.models.Member;
import com.example.muhammet.communicator.models.Spending;

import java.util.List;

/**
 * Created by Muhammet on 12.11.2017.
 */

public class BuyMeAdapter extends RecyclerView.Adapter<BuyMeAdapter.BuyMeAdapterViewHolder>{

    BuyMe[] buyMes;
    private ListItemClickListener mOnClickListener;
    
    public BuyMeAdapter(BuyMe[] buyMes, ListItemClickListener listener){
        this.buyMes = buyMes;
        mOnClickListener = listener;
    }

    class BuyMeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ListItemClickListener mListener;
        private TextView buyMeName;
        private TextView buyMeDescription;
        private Button editButton;
        private Button deleteButton;

        public BuyMeAdapterViewHolder(View itemView, ListItemClickListener listener) {
            super(itemView);
            mListener = listener;

            buyMeName = itemView.findViewById(R.id.buy_me_name);
            buyMeDescription = itemView.findViewById(R.id.buy_me_description);
            editButton = itemView.findViewById(R.id.buy_me_edit);
            deleteButton = itemView.findViewById(R.id.buy_me_delete);
            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
        }

        public void bind(BuyMe buyMe){
            buyMeName.setText(buyMe.getName());
            buyMeDescription.setText(buyMe.getDesc());
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
        BuyMe buyMe = buyMes[position];
        holder.bind(buyMe);

    }

    @Override
    public int getItemCount() {
        if(buyMes == null){
            return 0;
        }

        return buyMes.length;
    }

    public void setBuyMeData(BuyMe[] buyMeData) {
        buyMes = buyMeData;
        notifyDataSetChanged();
    }
}
