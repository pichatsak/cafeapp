package com.af.cafeapp.adaptor;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.af.cafeapp.R;
import com.af.cafeapp.models.MenuDataModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MenuAdaptor extends RecyclerView.Adapter<MenuAdaptor.ViewHolder> {

    private List<MenuDataModel> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private OnClicks mOnClicks;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = storage.getReference();
    // data is passed into the constructor
    public MenuAdaptor(Context context, List<MenuDataModel> data, OnClicks listener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mOnClicks = listener;
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_menu, parent, false);
        return new ViewHolder(view,mOnClicks);


    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MenuDataModel dataGet = mData.get(position);
        holder.nameMenu.setText(dataGet.getMenuName());
        Log.d("CHK_IMG",dataGet.getKey());
        storageReference.child("picMenu/"+dataGet.getKey()).getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(mInflater.getContext())
                    .load(uri)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(14)))
                    .into(holder.imgMenu);
        });

    }




    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameMenu;
        ImageView imgMenu;
        LinearLayout menuCont;
        public OnClicks onClicks;
        ViewHolder(View itemView,OnClicks onClicks) {
            super(itemView);
            nameMenu = itemView.findViewById(R.id.nameMenu);
            imgMenu = itemView.findViewById(R.id.imgMenu);
            menuCont = itemView.findViewById(R.id.menuCont);

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