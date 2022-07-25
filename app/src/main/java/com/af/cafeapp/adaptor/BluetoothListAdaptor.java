package com.af.cafeapp.adaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.af.cafeapp.R;
import com.af.cafeapp.models.ModelDevice;
import com.af.cafeapp.models.ToppingChooseModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BluetoothListAdaptor extends RecyclerView.Adapter<BluetoothListAdaptor.ViewHolder> {

    private List<ModelDevice> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private OnClicks mOnClicks;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = storage.getReference();
    // data is passed into the constructor
    public BluetoothListAdaptor(Context context, List<ModelDevice> data, OnClicks listener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mOnClicks = listener;
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_device, parent, false);
        return new ViewHolder(view,mOnClicks);


    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ModelDevice dataGet = mData.get(position);
        holder.txtDv.setText(dataGet.getNameDevice());
        holder.txtArd.setText(dataGet.getAdrDevice());
        holder.contMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClicks.OnClicks(position,"");
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
        LinearLayout contMain;
        TextView txtDv,txtArd;
        public OnClicks onClicks;
        ViewHolder(View itemView,OnClicks onClicks) {
            super(itemView);
            contMain = itemView.findViewById(R.id.contMain);
            txtDv = itemView.findViewById(R.id.txtDv);
            txtArd = itemView.findViewById(R.id.txtArd);
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
        void OnClicks(int position,String status);

    }
}