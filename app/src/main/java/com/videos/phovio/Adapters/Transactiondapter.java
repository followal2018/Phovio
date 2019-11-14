package com.videos.phovio.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.videos.phovio.R;
import com.videos.phovio.model.Transaction;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class Transactiondapter extends RecyclerView.Adapter<Transactiondapter.TransactionHolder> {
    private List<Transaction> transactionList= new ArrayList<>();
    private Context context;
    public Transactiondapter(List<Transaction> transactionList, Context context){
        this.context=context;
        this.transactionList=transactionList;
    }
    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_earning, null, false);
        viewHolder.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new Transactiondapter.TransactionHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(TransactionHolder holder,final int position) {
            holder.text_view_points_transaction_item.setText(transactionList.get(position).getPoints().replace("P","")+" Points");

//        holder.text_view_amount_transaction_item.setText(String.format("%.6f", transactionList.get(position).getAmount()));
        holder.text_view_amount_transaction_item.setText(String.format("%.6f$", new Double(transactionList.get(position).getAmount().substring(0, transactionList.get(position).getAmount().length() - 1))));

        holder.text_view_created_transaction_item.setText(transactionList.get(position).getCreated());
            holder.text_view_label_transaction_item.setText(transactionList.get(position).getLabel());
        Picasso.with(context).load(transactionList.get(position).getImage()).placeholder(context.getResources().getDrawable(R.drawable.placeholder)).error(context.getResources().getDrawable(R.drawable.image_quote)).into(holder.image_view_item_earning);
    }
    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class TransactionHolder extends RecyclerView.ViewHolder {

        private final TextView text_view_amount_transaction_item;
        private final TextView text_view_points_transaction_item;
        private final TextView text_view_label_transaction_item;
        private final TextView text_view_created_transaction_item;
        private final CircleImageView image_view_item_earning;

        public TransactionHolder(View itemView) {
            super(itemView);
            this.text_view_amount_transaction_item = (TextView) itemView.findViewById(R.id.text_view_amount_transaction_item);
            this.text_view_points_transaction_item = (TextView) itemView.findViewById(R.id.text_view_points_transaction_item);
            this.text_view_label_transaction_item = (TextView) itemView.findViewById(R.id.text_view_label_transaction_item);
            this.text_view_created_transaction_item = (TextView) itemView.findViewById(R.id.text_view_created_transaction_item);
            this.image_view_item_earning = (CircleImageView) itemView.findViewById(R.id.image_view_item_earning);
        }
    }
}