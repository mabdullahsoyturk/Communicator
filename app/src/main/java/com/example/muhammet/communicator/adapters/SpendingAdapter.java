package com.example.muhammet.communicator.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muhammet.communicator.ListItemClickListener;
import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.models.Member;
import com.example.muhammet.communicator.models.Spending;

import java.util.List;

public class SpendingAdapter extends RecyclerView.Adapter<SpendingAdapter.SpendingAdapterViewHolder> {

    List<Spending> spendings;
    private ListItemClickListener mOnClickListener;

    public SpendingAdapter(List<Spending> spendings, ListItemClickListener listener){
        this.spendings = spendings;
        mOnClickListener = listener;
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

        public void bind(Spending spending){
            spendingName.setText(spending.getName());
            spendingDate.setText(spending.getDate());
            spendingShare.setText(spending.getShare());
            spendingIcon.setImageResource(spending.getIcon_id());
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
        Spending spending = spendings.get(position);
        holder.bind(spending);
    }

    @Override
    public int getItemCount() {
        return spendings.size();
    }

    public void setSpendingData(List<Spending> spendingData) {
        spendings = spendingData;
        notifyDataSetChanged();
    }

}
