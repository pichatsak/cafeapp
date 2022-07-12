package com.af.cafeapp.adaptor;

import android.annotation.SuppressLint;
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
import com.af.cafeapp.models.ListPriceModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ListPriceAdaptor extends RecyclerView.Adapter<ListPriceAdaptor.ViewHolder> {

    private List<ListPriceModel> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private OnClicks mOnClicks;
    int posChoose =0;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = storage.getReference();
    // data is passed into the constructor
    public ListPriceAdaptor(Context context, List<ListPriceModel> data, OnClicks listener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mOnClicks = listener;
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_price_list, parent, false);
        return new ViewHolder(view,mOnClicks);


    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ListPriceModel dataGet = mData.get(position);
        holder.namePriceTv.setText(dataGet.getPrice_list_name());

        if(posChoose==position){
            holder.namePriceTv.setBackgroundResource(R.drawable.b_click_menu_path);
            holder.namePriceTv.setTextColor(mInflater.getContext().getResources().getColor(R.color.white));
        }else{
            holder.namePriceTv.setBackgroundResource(R.drawable.b_non_click_menu);
            holder.namePriceTv.setTextColor(mInflater.getContext().getResources().getColor(R.color.textgrey));
        }
        holder.namePriceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posChoose = position;
                mOnClicks.OnClicks(position);
                notifyDataSetChanged();

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
        TextView namePriceTv;
        public OnClicks onClicks;
        ViewHolder(View itemView,OnClicks onClicks) {
            super(itemView);
            namePriceTv = itemView.findViewById(R.id.namePriceTv);
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