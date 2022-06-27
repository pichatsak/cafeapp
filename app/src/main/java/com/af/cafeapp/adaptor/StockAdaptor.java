package com.af.cafeapp.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.af.cafeapp.R;
import com.af.cafeapp.data_class.data_add_list_stock;

import java.util.List;

public class StockAdaptor extends RecyclerView.Adapter<StockAdaptor.ViewHolder> {

    private List<data_add_list_stock> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public StockAdaptor(Context context, List<data_add_list_stock> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_stock, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        data_add_list_stock dataGet = mData.get(position);
        holder.nameStock.setText(dataGet.getAdd_name_stock());
        holder.AddNumStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }



    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameStock,numStock,unitStock;
        LinearLayout AddNumStock;

        ViewHolder(View itemView) {
            super(itemView);
            nameStock = itemView.findViewById(R.id.nameStock);
            numStock = itemView.findViewById(R.id.numStock);
            AddNumStock = itemView.findViewById(R.id.AddNumStock);
            unitStock = itemView.findViewById(R.id.unitStock);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position


    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}