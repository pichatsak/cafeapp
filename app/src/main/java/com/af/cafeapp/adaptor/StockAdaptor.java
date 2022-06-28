package com.af.cafeapp.adaptor;

import static android.content.ContentValues.TAG;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.af.cafeapp.R;
import com.af.cafeapp.data_class.data_add_list_stock;
import com.af.cafeapp.stock;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class StockAdaptor extends RecyclerView.Adapter<StockAdaptor.ViewHolder> {

    private List<data_add_list_stock> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();





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
        holder.numStock.setText(String.valueOf(dataGet.getAdd_num_stock()));
        holder.unitStock.setText(dataGet.getAdd_unit_stock());
        dataGet.getId();



        if(dataGet.getAdd_num_stock()<=5 && dataGet.getAdd_num_stock()>=1){

            holder.low_stock.setVisibility(View.VISIBLE);
            holder.use_stock.setVisibility(View.GONE);
            holder.out_of_stock.setVisibility(View.GONE);

        }
        else if(dataGet.getAdd_num_stock()>5){
            holder.low_stock.setVisibility(View.GONE);
            holder.use_stock.setVisibility(View.VISIBLE);
            holder.out_of_stock.setVisibility(View.GONE);

        }

        else if(dataGet.getAdd_num_stock()==0){
            holder.low_stock.setVisibility(View.GONE);
            holder.use_stock.setVisibility(View.GONE);
            holder.out_of_stock.setVisibility(View.VISIBLE);

        }


        holder.AddNumStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView ex_dialog_addnumstock;
                TextView namestocklist;

                final Dialog dialog = new Dialog(mInflater.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_addnum_stock);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                ex_dialog_addnumstock = dialog.findViewById(R.id.ex_dialog_addnumstock);
                dialog.show();
                Display display = ((WindowManager) mInflater.getContext().getSystemService(mInflater.getContext().WINDOW_SERVICE)).getDefaultDisplay();
                int width = display.getWidth();
                int height = display.getHeight();
                Log.v("width", width + "");
                dialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 5);

                namestocklist = dialog.findViewById(R.id.namestocklist);
                namestocklist.setText(dataGet.getAdd_name_stock());
                EditText num;
                LinearLayout addnum;
                addnum = dialog.findViewById(R.id.addnum);
                num = dialog.findViewById(R.id.num);
                addnum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (num.getText().toString().isEmpty()) {
                            Toast.makeText(mInflater.getContext(), "กรุณากรอกจำนวน", Toast.LENGTH_SHORT).show();
                        } else {

                            String getNum = num.getText().toString();
                            int in = Integer.valueOf(getNum);

                            db.collection("data_add_list_stock").document(dataGet.getId()).update("add_num_stock", FieldValue.increment(in));
//                            db.collection("data_add_list_stock").document(dataGet.getId()).delete();
                            dialog.dismiss();

                        }

                    }
                });

                ex_dialog_addnumstock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });


        holder.AddLobStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView ex_dialog_addlobstock;
                TextView namestocklist_lob;
                EditText num_lob;
                LinearLayout lobnum;

                final Dialog dialog = new Dialog(mInflater.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_lobnum_stock);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                ex_dialog_addlobstock = dialog.findViewById(R.id.ex_dialog_addlobstock);
                dialog.show();
                Display display = ((WindowManager) mInflater.getContext().getSystemService(mInflater.getContext().WINDOW_SERVICE)).getDefaultDisplay();
                int width = display.getWidth();
                int height = display.getHeight();
                Log.v("width", width + "");
                dialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 5);

                namestocklist_lob = dialog.findViewById(R.id.namestocklist_lob);
                namestocklist_lob.setText(dataGet.getAdd_name_stock());
                lobnum = dialog.findViewById(R.id.lobnum);
                num_lob = dialog.findViewById(R.id.num_lob);
                lobnum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (num_lob.getText().toString().isEmpty()) {
                            Toast.makeText(mInflater.getContext(), "กรุณากรอกจำนวน", Toast.LENGTH_SHORT).show();
                        }
                        else if (dataGet.getAdd_num_stock()==0){
                            Toast.makeText(mInflater.getContext(), "จำนวนสต็อกหมดแล้ว", Toast.LENGTH_SHORT).show();
                        }
                        else if (Integer.valueOf(num_lob.getText().toString())>dataGet.getAdd_num_stock()){
                            Toast.makeText(mInflater.getContext(), "จำนวนสต็อกมีน้อยกว่าจำนวนนำออก", Toast.LENGTH_SHORT).show();
                        }


                        else {

                            String getNum = num_lob.getText().toString();
                            int in = Integer.valueOf(getNum);

                            db.collection("data_add_list_stock").document(dataGet.getId()).update("add_num_stock", FieldValue.increment(-in));

                            dialog.dismiss();

                        }

                    }
                });

                ex_dialog_addlobstock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

        holder.delect_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout cencle_delect,ok_delect;




                        final Dialog dialog = new Dialog(mInflater.getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.dailog_delect_list_stock);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        cencle_delect = dialog.findViewById(R.id.cencle_delect);
                        dialog.show();
                        Display display =((WindowManager)mInflater.getContext().getSystemService(mInflater.getContext().WINDOW_SERVICE)).getDefaultDisplay();
                        int width = display.getWidth();
                        int height=display.getHeight();
                        Log.v("width", width+"");
                        dialog.getWindow().setLayout((6*width)/7,(4*height)/5);

                ok_delect = dialog.findViewById(R.id.ok_delect);
                ok_delect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        db.collection("data_add_list_stock").document(dataGet.getId()).delete();
                    }
                });

                        cencle_delect.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
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
        TextView nameStock, numStock, unitStock;
        LinearLayout AddNumStock,AddLobStock,use_stock,low_stock,out_of_stock;
        ImageView delect_item;

        ViewHolder(View itemView) {
            super(itemView);
            nameStock = itemView.findViewById(R.id.nameStock);
            numStock = itemView.findViewById(R.id.numStock);
            AddNumStock = itemView.findViewById(R.id.AddNumStock);
            unitStock = itemView.findViewById(R.id.unitStock);
            AddLobStock = itemView.findViewById(R.id.AddLobStock);
            delect_item = itemView.findViewById(R.id.delect_item);
            use_stock = itemView.findViewById(R.id.use_stock);
            low_stock = itemView.findViewById(R.id.low_stock);
            out_of_stock = itemView.findViewById(R.id.out_of_stock);

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