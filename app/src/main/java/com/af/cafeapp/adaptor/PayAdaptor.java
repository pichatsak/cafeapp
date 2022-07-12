package com.af.cafeapp.adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.af.cafeapp.R;
import com.af.cafeapp.models.PayData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Locale;

public class PayAdaptor extends RecyclerView.Adapter<PayAdaptor.ViewHolder> {

    private List<PayData> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private OnClicks mOnClicks;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = storage.getReference();
    // data is passed into the constructor
    public PayAdaptor(Context context, List<PayData> data, OnClicks listener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mOnClicks = listener;
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_pay, parent, false);
        return new ViewHolder(view,mOnClicks);


    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PayData dataGet = mData.get(position);
        holder.tvTypePay.setText(dataGet.getTypePay());
        holder.tvPay.setText(String.format(Locale.ENGLISH,"%.2f", dataGet.getTotalPay()));
        holder.del.setOnClickListener(view -> mOnClicks.OnClicks(position));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTypePay,tvPay;
        LinearLayout del;
        public OnClicks onClicks;
        ViewHolder(View itemView,OnClicks onClicks) {
            super(itemView);
            tvTypePay = itemView.findViewById(R.id.tvTypePay);
            tvPay = itemView.findViewById(R.id.tvPay);
            del  = itemView.findViewById(R.id.del);

            itemView.setOnClickListener(this);
            this.onClicks = onClicks;
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

    public interface OnClicks{
        void OnClicks(int position);
    }
}