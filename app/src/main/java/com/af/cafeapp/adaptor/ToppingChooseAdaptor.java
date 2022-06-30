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
import com.af.cafeapp.models.ToppingChooseModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ToppingChooseAdaptor extends RecyclerView.Adapter<ToppingChooseAdaptor.ViewHolder> {

    private List<ToppingChooseModel> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private OnClicks mOnClicks;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = storage.getReference();
    private ArrayList<String> checkList = new ArrayList<>();
    // data is passed into the constructor
    public ToppingChooseAdaptor(Context context, List<ToppingChooseModel> data, OnClicks listener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mOnClicks = listener;
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_topp_choose, parent, false);
        return new ViewHolder(view,mOnClicks);


    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ToppingChooseModel dataGet = mData.get(position);
        holder.nameToppingShow.setText(dataGet.getToppingName());
        AtomicInteger numTp = new AtomicInteger();
        final boolean[] isAdd = {false};
        holder.plusTp.setOnClickListener(view -> {
            numTp.getAndIncrement();
            mData.get(position).setNum(numTp.get());
            holder.tvNum.setText(String.valueOf(numTp));
            holder.contChoose.setBackgroundResource(R.drawable.active_tp);
            isAdd[0] = true;
            checkList.add(dataGet.getKey());
        });
        holder.delTp.setOnClickListener(view -> {
            if(numTp.get()==1){
                numTp.set(0);
                mData.get(position).setNum(numTp.get());
                isAdd[0] = false;
                checkList.remove(dataGet.getKey());
                holder.contChoose.setBackgroundResource(R.drawable.b_non_click_menu);
            }else if(numTp.get()==0){
                numTp.set(0);
                mData.get(position).setNum(numTp.get());
                isAdd[0] = false;
            }else{
                checkList.remove(dataGet.getKey());
                numTp.getAndDecrement();
                mData.get(position).setNum(numTp.get());
                isAdd[0] = true;
            }
            holder.tvNum.setText(String.valueOf(numTp));
        });
        holder.contChoose.setOnClickListener(view -> {
            if(isAdd[0]){
                while(checkList.remove(dataGet.getKey())) {}
                isAdd[0] = false;
                numTp.set(0);
                mData.get(position).setNum(numTp.get());
                holder.contChoose.setBackgroundResource(R.drawable.b_non_click_menu);
            }else{
                checkList.add(dataGet.getKey());
                isAdd[0] = true;
                numTp.set(1);
                mData.get(position).setNum(numTp.get());
                holder.contChoose.setBackgroundResource(R.drawable.active_tp);
            }
            holder.tvNum.setText(String.valueOf(numTp));
        });
    }

    public ArrayList<String> getCheckList(){
        return this.checkList;
    }

    public List<ToppingChooseModel> getTppList(){
        return this.mData;
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameToppingShow,tvNum;
        LinearLayout contChoose;
        ImageView delTp,plusTp;
        public OnClicks onClicks;
        ViewHolder(View itemView,OnClicks onClicks) {
            super(itemView);
            nameToppingShow = itemView.findViewById(R.id.nameToppingShow);
            delTp = itemView.findViewById(R.id.delTp);
            plusTp = itemView.findViewById(R.id.plusTp);
            contChoose = itemView.findViewById(R.id.contChoose);
            tvNum = itemView.findViewById(R.id.tvNum);

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