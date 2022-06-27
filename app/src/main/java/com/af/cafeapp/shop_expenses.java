package com.af.cafeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class shop_expenses extends AppCompatActivity {

    private LinearLayout add_list;
    private ImageView ex_dialog_addshop_expenses;
    private ImageView delect_item;
    private LinearLayout cencle_delect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_expenses);
        getSupportActionBar().hide();

        add_list = findViewById(R.id.add_list);
        add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(shop_expenses.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_add_shop_expenses);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                ex_dialog_addshop_expenses = dialog.findViewById(R.id.ex_dialog_addshop_expenses);
                dialog.show();
                Display display =((WindowManager)getSystemService(shop_expenses.this.WINDOW_SERVICE)).getDefaultDisplay();
                int width = display.getWidth();
                int height=display.getHeight();
                Log.v("width", width+"");
                dialog.getWindow().setLayout((6*width)/7,(4*height)/5);

                ex_dialog_addshop_expenses.setOnClickListener(new View.OnClickListener() {
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
                final Dialog dialog = new Dialog(shop_expenses.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_delect_list_shop_expenses);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                cencle_delect = dialog.findViewById(R.id.cencle_delect);
                dialog.show();
                Display display =((WindowManager)getSystemService(shop_expenses.this.WINDOW_SERVICE)).getDefaultDisplay();
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
}