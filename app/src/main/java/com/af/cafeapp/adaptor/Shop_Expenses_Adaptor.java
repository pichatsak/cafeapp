package com.af.cafeapp.adaptor;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.af.cafeapp.R;
import com.af.cafeapp.models.ExpenseHisData;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.List;

public class Shop_Expenses_Adaptor extends RecyclerView.Adapter<Shop_Expenses_Adaptor.ViewHolder> {

    private List<ExpenseHisData> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    DecimalFormat df = new DecimalFormat("#,###.00");

    // data is passed into the constructor
    public Shop_Expenses_Adaptor(Context context, List<ExpenseHisData> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_shop_expenses, parent, false);
        return new ViewHolder(view);


    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ExpenseHisData dataGet = mData.get(position);
        holder.name_list.setText(dataGet.getNameExpense());
        holder.money.setText(df.format(dataGet.getTotal()));

//
//
//        holder.delect_item_shop_expenses.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LinearLayout cencle_delect_shop_expenses,ok_delect_shop_expenses;
//
//
//
//
//                final Dialog dialog = new Dialog(mInflater.getContext());
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setCancelable(false);
//                dialog.setContentView(R.layout.dialog_delect_list_shop_expenses);
//                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                cencle_delect_shop_expenses = dialog.findViewById(R.id.cencle_delect_shop_expenses);
//                dialog.show();
//                Display display =((WindowManager)mInflater.getContext().getSystemService(mInflater.getContext().WINDOW_SERVICE)).getDefaultDisplay();
//                int width = display.getWidth();
//                int height=display.getHeight();
//                Log.v("width", width+"");
//                dialog.getWindow().setLayout((6*width)/7,(4*height)/5);
//
//                ok_delect_shop_expenses = dialog.findViewById(R.id.ok_delect_shop_expenses);
//                ok_delect_shop_expenses.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                        db.collection("ExpenseHisData").document(dataGet.getId()).delete();
//                    }
//                });
//
//                cencle_delect_shop_expenses.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//            }
//        });
    }




    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_list,money;
        ImageView delect_item_shop_expenses;

        ViewHolder(View itemView) {
            super(itemView);
            name_list = itemView.findViewById(R.id.name_list);
            money = itemView.findViewById(R.id.money);
            delect_item_shop_expenses = itemView.findViewById(R.id.delect_item_shop_expenses);


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
