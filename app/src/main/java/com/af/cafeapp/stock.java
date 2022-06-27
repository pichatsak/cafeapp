package com.af.cafeapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.af.cafeapp.adaptor.StockAdaptor;
import com.af.cafeapp.data_class.data_add_list_stock;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class stock extends AppCompatActivity {
    private LinearLayout AddListStock;
    private ImageView ex_dialog_addstock;
    private LinearLayout AddNumStock;
    private ImageView ex_dialog_addnumstock;
    private LinearLayout AddLobStock;
    private ImageView ex_dialog_addlobstock;
    private ImageView delect_item;
    private LinearLayout cencle_delect;
    private EditText name_stock;  //ชื่อสต็อก
    private LinearLayout add_list_stock; //เพิ่มรายการสต็อก
    private EditText numaddstock; //จำนวนเพิ่มสต็อก
    private EditText unit_add_stock; //หน่วยเพิ่มสต็อก
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private LinearLayout list_stock1; //รายการ 1 ที่เพิ่มสต็อก
    private RecyclerView stockView;
    private ArrayList<data_add_list_stock> data_add_list_stocks_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        list_stock1 = findViewById(R.id.list_stock1);
        stockView = findViewById(R.id.stockView);

        getSupportActionBar().hide();

        getData();

        //กดเพิ่มรายการสต็อกสินค้า
        AddListStock = findViewById(R.id.AddListStock);
        AddListStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(stock.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_add_stock);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                ex_dialog_addstock = dialog.findViewById(R.id.ex_dialog_addstock);
                dialog.show();
                Display display =((WindowManager)getSystemService(stock.this.WINDOW_SERVICE)).getDefaultDisplay();
                int width = display.getWidth();
                int height=display.getHeight();
                Log.v("width", width+"");
                dialog.getWindow().setLayout((6*width)/7,(4*height)/5);



                //stock
                add_list_stock = dialog.findViewById(R.id.add_list_stock);
                name_stock = dialog.findViewById(R.id.name_stock);
                numaddstock = dialog.findViewById(R.id.numaddstock);
                unit_add_stock = dialog.findViewById(R.id.unit_add_stock);
                add_list_stock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (name_stock.getText().toString().isEmpty()) {
                            Toast.makeText(stock.this, "กรุณากรอกชื่อสต็อก", Toast.LENGTH_SHORT).show();
                        } else if (numaddstock.getText().toString().isEmpty()) {
                            Toast.makeText(stock.this, "กรุณากรอกจำนวน", Toast.LENGTH_SHORT).show();
                        } else if (unit_add_stock.getText().toString().isEmpty()) {
                            Toast.makeText(stock.this, "กรุณากรอกหน่วย", Toast.LENGTH_SHORT).show();

                        } else {
                            data_add_stock();
                            dialog.dismiss();
                            Toast.makeText(stock.this, "เพิ่มรายการสำเร็จ", Toast.LENGTH_SHORT).show();
                        }

                    }
                });




                ex_dialog_addstock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        AddNumStock = findViewById(R.id.AddNumStock);
        AddNumStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(stock.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_addnum_stock);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                ex_dialog_addnumstock = dialog.findViewById(R.id.ex_dialog_addnumstock);
                dialog.show();
                Display display =((WindowManager)getSystemService(stock.this.WINDOW_SERVICE)).getDefaultDisplay();
                int width = display.getWidth();
                int height=display.getHeight();
                Log.v("width", width+"");
                dialog.getWindow().setLayout((6*width)/7,(4*height)/5);

                ex_dialog_addnumstock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

        AddLobStock = findViewById(R.id.AddLobStock);
        AddLobStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(stock.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_lobnum_stock);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                ex_dialog_addlobstock = dialog.findViewById(R.id.ex_dialog_addlobstock);
                dialog.show();
                Display display =((WindowManager)getSystemService(stock.this.WINDOW_SERVICE)).getDefaultDisplay();
                int width = display.getWidth();
                int height=display.getHeight();
                Log.v("width", width+"");
                dialog.getWindow().setLayout((6*width)/7,(4*height)/5);

                ex_dialog_addlobstock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

        delect_item = findViewById(R.id.delect_item);
        delect_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(stock.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dailog_delect_list_stock);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                cencle_delect = dialog.findViewById(R.id.cencle_delect);
                dialog.show();
                Display display =((WindowManager)getSystemService(stock.this.WINDOW_SERVICE)).getDefaultDisplay();
                int width = display.getWidth();
                int height=display.getHeight();
                Log.v("width", width+"");
                dialog.getWindow().setLayout((6*width)/7,(4*height)/5);

                cencle_delect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void getData(){
        data_add_list_stocks_list = new ArrayList<>();
        db.collection("data_add_list_stock")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                data_add_list_stock data_add_list_stock = new data_add_list_stock();
                                data_add_list_stock.setAdd_name_stock(document.getData().get("add_name_stock").toString());
                                data_add_list_stock.setId(document.getId());

                                data_add_list_stocks_list.add(data_add_list_stock);
                            }

                            stockView.setLayoutManager(new LinearLayoutManager(stock.this));
                            StockAdaptor stockAdaptor = new StockAdaptor(stock.this,data_add_list_stocks_list);
                            stockView.setAdapter(stockAdaptor);
                        }
                    }
                });
    }

    public void data_add_stock(){

        String Getname_stock = name_stock.getText().toString();
        String Getnumaddstock = numaddstock.getText().toString();
        String Getunit_add_stock = unit_add_stock.getText().toString();



        data_add_list_stock data = new data_add_list_stock();
        data.setAdd_name_stock(Getname_stock);
        data.setAdd_num_stock(Getnumaddstock);
        data.setAdd_unit_stock(Getunit_add_stock);




        db.collection("data_add_list_stock")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                        getData();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(stock.this, "เกิดข้อผิดพลาดลองใหม่อีกครั้ง", Toast.LENGTH_SHORT).show();
                    }
                });

    }





}

