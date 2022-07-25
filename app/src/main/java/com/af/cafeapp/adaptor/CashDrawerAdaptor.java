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
import com.af.cafeapp.models.DrawerHisData;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.List;

public class CashDrawerAdaptor extends RecyclerView.Adapter<CashDrawerAdaptor.ViewHolder> {

    private List<DrawerHisData> mData;
    private LayoutInflater mInflater;
    DecimalFormat df = new DecimalFormat("#,###.00");
    private ItemClickListener mClickListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    // data is passed into the constructor
    public CashDrawerAdaptor(Context context, List<DrawerHisData> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_drawer, parent, false);
        return new ViewHolder(view);


    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DrawerHisData dataGet = mData.get(position);

        holder.total.setText(df.format(dataGet.getTotal()));
        holder.note.setText(dataGet.getNote());
        if(dataGet.getTypeData().equals("in")){
            holder.status_show.setText("นำเข้า");
        }else{
            holder.status_show.setText("นำออก");
        }

        if(dataGet.getStatusStart().equals("yes")){
            holder.delete_item.setVisibility(View.GONE);
        }

//        holder.nameStock.setText(dataGet.getAdd_name_stock());
//        holder.numStock.setText(String.valueOf(dataGet.getAdd_num_stock()));
//        holder.unitStock.setText(dataGet.getAdd_unit_stock());
//        dataGet.getId();
//
//
//
//        if(dataGet.getAdd_num_stock()<=5 && dataGet.getAdd_num_stock()>=1){
//
//            holder.low_stock.setVisibility(View.VISIBLE);
//            holder.use_stock.setVisibility(View.GONE);
//            holder.out_of_stock.setVisibility(View.GONE);
//
//        }
//        else if(dataGet.getAdd_num_stock()>5){
//            holder.low_stock.setVisibility(View.GONE);
//            holder.use_stock.setVisibility(View.VISIBLE);
//            holder.out_of_stock.setVisibility(View.GONE);
//
//        }
//
//        else if(dataGet.getAdd_num_stock()==0){
//            holder.low_stock.setVisibility(View.GONE);
//            holder.use_stock.setVisibility(View.GONE);
//            holder.out_of_stock.setVisibility(View.VISIBLE);
//
//        }
//
//
//        holder.AddNumStock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ImageView ex_dialog_addnumstock;
//                TextView namestocklist;
//
//                final Dialog dialog = new Dialog(mInflater.getContext());
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setCancelable(false);
//                dialog.setContentView(R.layout.dialog_addnum_stock);
//                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                ex_dialog_addnumstock = dialog.findViewById(R.id.ex_dialog_addnumstock);
//                dialog.show();
//                Display display = ((WindowManager) mInflater.getContext().getSystemService(mInflater.getContext().WINDOW_SERVICE)).getDefaultDisplay();
//                int width = display.getWidth();
//                int height = display.getHeight();
//                Log.v("width", width + "");
//                dialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 5);
//
//                namestocklist = dialog.findViewById(R.id.namestocklist);
//                namestocklist.setText(dataGet.getAdd_name_stock());
//                EditText num;
//                LinearLayout addnum;
//                addnum = dialog.findViewById(R.id.addnum);
//                num = dialog.findViewById(R.id.num);
//                addnum.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (num.getText().toString().isEmpty()) {
//                            Toast.makeText(mInflater.getContext(), "กรุณากรอกจำนวน", Toast.LENGTH_SHORT).show();
//                        } else {
//
//                            String getNum = num.getText().toString();
//                            int in = Integer.valueOf(getNum);
//
//                            db.collection("DrawerHisData").document(dataGet.getId()).update("add_num_stock", FieldValue.increment(in));
////                            db.collection("DrawerHisData").document(dataGet.getId()).delete();
//                            dialog.dismiss();
//
//                        }
//
//                    }
//                });
//
//                ex_dialog_addnumstock.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//
//            }
//        });
//
//
//        holder.AddLobStock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ImageView ex_dialog_addlobstock;
//                TextView namestocklist_lob;
//                EditText num_lob;
//                LinearLayout lobnum;
//
//                final Dialog dialog = new Dialog(mInflater.getContext());
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setCancelable(false);
//                dialog.setContentView(R.layout.dialog_lobnum_stock);
//                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                ex_dialog_addlobstock = dialog.findViewById(R.id.ex_dialog_addlobstock);
//                dialog.show();
//                Display display = ((WindowManager) mInflater.getContext().getSystemService(mInflater.getContext().WINDOW_SERVICE)).getDefaultDisplay();
//                int width = display.getWidth();
//                int height = display.getHeight();
//                Log.v("width", width + "");
//                dialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 5);
//
//                namestocklist_lob = dialog.findViewById(R.id.namestocklist_lob);
//                namestocklist_lob.setText(dataGet.getAdd_name_stock());
//                lobnum = dialog.findViewById(R.id.lobnum);
//                num_lob = dialog.findViewById(R.id.num_lob);
//                lobnum.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (num_lob.getText().toString().isEmpty()) {
//                            Toast.makeText(mInflater.getContext(), "กรุณากรอกจำนวน", Toast.LENGTH_SHORT).show();
//                        }
//                        else if (dataGet.getAdd_num_stock()==0){
//                            Toast.makeText(mInflater.getContext(), "จำนวนสต็อกหมดแล้ว", Toast.LENGTH_SHORT).show();
//                        }
//                        else if (Integer.valueOf(num_lob.getText().toString())>dataGet.getAdd_num_stock()){
//                            Toast.makeText(mInflater.getContext(), "จำนวนสต็อกมีน้อยกว่าจำนวนนำออก", Toast.LENGTH_SHORT).show();
//                        }
//
//
//                        else {
//
//                            String getNum = num_lob.getText().toString();
//                            int in = Integer.valueOf(getNum);
//
//                            db.collection("DrawerHisData").document(dataGet.getId()).update("add_num_stock", FieldValue.increment(-in));
//
//                            dialog.dismiss();
//
//                        }
//
//                    }
//                });
//
//                ex_dialog_addlobstock.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//
//            }
//        });
//
//        holder.delect_item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LinearLayout cencle_delect,ok_delect;
//
//
//
//
//                        final Dialog dialog = new Dialog(mInflater.getContext());
//                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        dialog.setCancelable(false);
//                        dialog.setContentView(R.layout.dailog_delect_list_stock);
//                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                        cencle_delect = dialog.findViewById(R.id.cencle_delect);
//                        dialog.show();
//                        Display display =((WindowManager)mInflater.getContext().getSystemService(mInflater.getContext().WINDOW_SERVICE)).getDefaultDisplay();
//                        int width = display.getWidth();
//                        int height=display.getHeight();
//                        Log.v("width", width+"");
//                        dialog.getWindow().setLayout((6*width)/7,(4*height)/5);
//
//                ok_delect = dialog.findViewById(R.id.ok_delect);
//                ok_delect.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                        db.collection("DrawerHisData").document(dataGet.getId()).delete();
//                    }
//                });
//
//                        cencle_delect.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog.dismiss();
//                            }
//                        });
//                    }
//                });


    }




    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView total,note,status_show;
        ImageView delete_item;

        ViewHolder(View itemView) {
            super(itemView);
            total = itemView.findViewById(R.id.total);
            note = itemView.findViewById(R.id.note);
            status_show = itemView.findViewById(R.id.status_show);
            delete_item = itemView.findViewById(R.id.delete_item);
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