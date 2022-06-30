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
import com.af.cafeapp.models.CartData;
import com.af.cafeapp.models.ToppingChooseModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartAdaptor extends RecyclerView.Adapter<CartAdaptor.ViewHolder> {

    private List<CartData> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private OnClicks mOnClicks;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = storage.getReference();
    // data is passed into the constructor
    public CartAdaptor(Context context, List<CartData> data, OnClicks listener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mOnClicks = listener;
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_cart, parent, false);
        return new ViewHolder(view,mOnClicks);


    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CartData dataGet = mData.get(position);
        holder.nameMenuCart.setText(dataGet.getNameMenu());
        Log.d("CHK_PRICE",""+dataGet.getPrice());
        String priceGet = String.format(Locale.ENGLISH,"%.2f", dataGet.getPrice());
        holder.priceMenuCart.setText(priceGet);
        holder.numMenuShow.setText(String.valueOf(dataGet.getNum()));
        if(dataGet.getTypePrice()==1){
            holder.showTypePrice.setText("ธรรมดา");
        }else{
            holder.showTypePrice.setText("พิเศษ");
        }
        if(dataGet.getNote().isEmpty()){
            holder.noteShow.setText("");
        }else{
            holder.noteShow.setText(dataGet.getNote());
        }
        holder.delCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClicks.OnClicks(position,"del");
            }
        });
        String txtTop = "";
        Map<String, Object> topMap = (Map<String, Object>) dataGet.getTopping();
        int i=0;
        if(topMap.size()==0){
            holder.showTopAll.setVisibility(View.GONE);
        }else{
            for (Map.Entry<String, Object> entry : topMap.entrySet()) {
                Log.d("CHK_TPP_CHOOSE",""+entry.getValue().toString());
                ToppingChooseModel toppingChooseModelGet = (ToppingChooseModel) entry.getValue();
                if(i==0){
                    txtTop += toppingChooseModelGet.getToppingName()+"@"+toppingChooseModelGet.getNum();
                }else{
                    txtTop += " , "+toppingChooseModelGet.getToppingName()+"@"+toppingChooseModelGet.getNum();
                }
                i++;
            }
            holder.showTopAll.setText(txtTop);
        }

        if(mData.size()==(position+1)){
            Log.d("CHKMAX","YES");
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(40, 20, 40, 450);
            holder.contMain.setLayoutParams(layoutParams);
        }else{
            Log.d("CHKMAX","NO");
        }

    }



    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameMenuCart,priceMenuCart,numMenuShow,showTypePrice,noteShow,showTopAll;
        LinearLayout delCart,contMain;
        public OnClicks onClicks;
        ViewHolder(View itemView,OnClicks onClicks) {
            super(itemView);
            nameMenuCart = itemView.findViewById(R.id.nameMenuCart);
            priceMenuCart = itemView.findViewById(R.id.priceMenuCart);
            numMenuShow = itemView.findViewById(R.id.numMenuShow);
            showTypePrice = itemView.findViewById(R.id.showTypePrice);
            showTopAll = itemView.findViewById(R.id.showTopAll);
            noteShow = itemView.findViewById(R.id.noteShow);
            delCart = itemView.findViewById(R.id.delCart);
            contMain = itemView.findViewById(R.id.contMain);
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