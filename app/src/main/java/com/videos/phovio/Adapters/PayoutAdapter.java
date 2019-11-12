package com.videos.phovio.Adapters;


import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.videos.phovio.R;
import com.videos.phovio.model.Payout;

import java.util.ArrayList;
import java.util.List;

public class PayoutAdapter extends RecyclerView.Adapter<PayoutAdapter.PayoutHolder> {
    private List<Payout> payoutList= new ArrayList<>();
    private Context context;
    public PayoutAdapter(List<Payout> payoutList, Context context){
        this.context=context;
        this.payoutList=payoutList;
    }
    @Override
    public PayoutHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payout, null, false);
        viewHolder.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new PayoutAdapter.PayoutHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(PayoutHolder holder,final int position) {
        holder.text_view_item_payout_account.setText(payoutList.get(position).getAccount());
        holder.text_view_item_payout_name.setText(payoutList.get(position).getName());
        holder.text_view_item_payout_amount.setText(payoutList.get(position).getAmount());
        holder.text_view_item_payout_points.setText(payoutList.get(position).getPoints().toString()+" P");
        holder.text_view_item_payout_date.setText(payoutList.get(position).getDate());
        holder.text_view_item_payout_method.setText(payoutList.get(position).getMethod());
        holder.text_view_item_payout_state.setText(payoutList.get(position).getState());

        if (payoutList.get(position).getState().equals("Pending")){
            holder.text_view_item_payout_state.setBackgroundColor(Color.parseColor("#ffbc12"));
        }else if (payoutList.get(position).getState().equals("Paid")){
            holder.text_view_item_payout_state.setBackgroundColor(Color.parseColor("#48ba21"));
        }else if (payoutList.get(position).getState().equals("Rejected")){
            holder.text_view_item_payout_state.setBackgroundColor(Color.parseColor("#ba2145"));
        }
    }
    @Override
    public int getItemCount() {
        return payoutList.size();
    }

    public static class PayoutHolder extends RecyclerView.ViewHolder {

        private final TextView text_view_item_payout_account;
        private final TextView text_view_item_payout_name;
        private final TextView text_view_item_payout_state;
        private final TextView text_view_item_payout_method;
        private final TextView text_view_item_payout_amount;
        private final TextView text_view_item_payout_points;
        private final TextView text_view_item_payout_date;

        public PayoutHolder(View itemView) {
            super(itemView);
            this.text_view_item_payout_account = (TextView) itemView.findViewById(R.id.text_view_item_payout_account);
            this.text_view_item_payout_name = (TextView) itemView.findViewById(R.id.text_view_item_payout_name);
            this.text_view_item_payout_state = (TextView) itemView.findViewById(R.id.text_view_item_payout_state);
            this.text_view_item_payout_method = (TextView) itemView.findViewById(R.id.text_view_item_payout_method);
            this.text_view_item_payout_amount = (TextView) itemView.findViewById(R.id.text_view_item_payout_amount);
            this.text_view_item_payout_points  = (TextView) itemView.findViewById(R.id.text_view_item_payout_points);
            this.text_view_item_payout_date = (TextView) itemView.findViewById(R.id.text_view_item_payout_date);
        }
    }
}