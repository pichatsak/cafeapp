package com.af.cafeapp.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.af.cafeapp.R;
import com.af.cafeapp.models.ToppingMenuData;

import java.util.List;

public class ToppingAdaptor extends RecyclerView.Adapter<ToppingAdaptor.ViewHolder> {

    private List<ToppingMenuData> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private OnClicks mOnClicks;
    private int posPrice=0;

    // data is passed into the constructor
    public ToppingAdaptor(Context context, List<ToppingMenuData> data,int posPrice,OnClicks listener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mOnClicks = listener;
        this.posPrice = posPrice;
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_topping, parent, false);
        return new ViewHolder(view,mOnClicks);


    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ToppingMenuData dataGet = mData.get(position);
        holder.nameTopping.setText(dataGet.getNameTopping());
        if(posPrice==1){
            holder.price.setText(String.valueOf(dataGet.getPriceTopping()));
        }else if(posPrice==2){
            holder.price.setText(String.valueOf(dataGet.getPriceToppingGrab()));
        }else if(posPrice==3){
            holder.price.setText(String.valueOf(dataGet.getPriceToppingFoodPanda()));
        }else if(posPrice==4){
            holder.price.setText(String.valueOf(dataGet.getPriceToppingRobinHood()));
        }
        holder.delRow.setOnClickListener(view -> {
            mOnClicks.OnClicks(position);
        });
    }




    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTopping,price;
        LinearLayout delRow;
        public OnClicks onClicks;
        ViewHolder(View itemView,OnClicks onClicks) {
            super(itemView);
            nameTopping = itemView.findViewById(R.id.nameTopping);
            price = itemView.findViewById(R.id.price);
            delRow = itemView.findViewById(R.id.delRow);

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