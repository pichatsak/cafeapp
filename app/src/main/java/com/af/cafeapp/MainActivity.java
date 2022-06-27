package com.af.cafeapp;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.af.cafeapp.ui.Tool.CloseBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.af.cafeapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private LinearLayout checkBill;  // ปุ่มเช็คบิล
  //  private ImageView ex_dialog ; //ออกไดอาร็อคชำระเงิน
    private TextView checklish;
    private ImageView ex_dialog_g_and_f ;
    private LinearLayout add_menu_home; //เพิ่มเมนู
    private ImageView ex_dialog_addmenu_home;
    private LinearLayout add_topping;
    private ImageView ex_dialog_add_topping;
    private LinearLayout menu1;
    private ImageView ex_dialog_click_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
//        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        getSupportActionBar().hide();
        //  CloseBar closeBar = new CloseBar(this);




//ไดอาร็อคเช็คบิล
        checkBill = findViewById(R.id.checkBill);

        checkBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_check_money_g_and_f);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                ex_dialog_g_and_f = dialog.findViewById(R.id.ex_dialog_g_and_f);
                dialog.show();
                Display display =((WindowManager)getSystemService(MainActivity.this.WINDOW_SERVICE)).getDefaultDisplay();
                int width = display.getWidth();
                int height=display.getHeight();
                Log.v("width", width+"");
                dialog.getWindow().setLayout((6*width)/7,(4*height)/5);

                ex_dialog_g_and_f.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
   //ห้ามลบ
//        checkBill.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final Dialog dialog = new Dialog(MainActivity.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setCancelable(false);
//                dialog.setContentView(R.layout.dialog_check_money);
//                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                    ex_dialog = dialog.findViewById(R.id.ex_dialog);
//                dialog.show();
//                Display display =((WindowManager)getSystemService(MainActivity.this.WINDOW_SERVICE)).getDefaultDisplay();
//                int width = display.getWidth();
//                int height=display.getHeight();
//                Log.v("width", width+"");
//                dialog.getWindow().setLayout((6*width)/7,(4*height)/5);
//
//                ex_dialog.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//            }
//        });

//ไดอาร็อคเช็ครายการที่สั่ง
        checklish = findViewById(R.id.checklish);
        checklish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_check_lish);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                dialog.show();
                Display display =((WindowManager)getSystemService(MainActivity.this.WINDOW_SERVICE)).getDefaultDisplay();
                int width = display.getWidth();
                int height=display.getHeight();
                Log.v("width", width+"");
                dialog.getWindow().setLayout((6*width)/7,(4*height)/5);




            }
        });


        add_menu_home = findViewById(R.id.add_menu_home);

        add_menu_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_add_menu_home);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                ex_dialog_addmenu_home = dialog.findViewById(R.id.ex_dialog_addmenu_home);
                dialog.show();
                Display display =((WindowManager)getSystemService(MainActivity.this.WINDOW_SERVICE)).getDefaultDisplay();
                int width = display.getWidth();
                int height=display.getHeight();
                Log.v("width", width+"");
                dialog.getWindow().setLayout((6*width)/7,(4*height)/5);

                ex_dialog_addmenu_home.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                add_topping = dialog.findViewById(R.id.add_topping);
                add_topping.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.dialog_add_topping);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        ex_dialog_add_topping = dialog.findViewById(R.id.ex_dialog_add_topping);
                        dialog.show();
                        Display display =((WindowManager)getSystemService(MainActivity.this.WINDOW_SERVICE)).getDefaultDisplay();
                        int width = display.getWidth();
                        int height=display.getHeight();
                        Log.v("width", width+"");
                        dialog.getWindow().setLayout((6*width)/7,(4*height)/5);

                        ex_dialog_add_topping.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });


                    }
                });

            }
        });


        menu1 = findViewById(R.id.menu1);
        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_click_menu);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                ex_dialog_click_menu = dialog.findViewById(R.id.ex_dialog_click_menu);
                dialog.show();
                Display display =((WindowManager)getSystemService(MainActivity.this.WINDOW_SERVICE)).getDefaultDisplay();
                int width = display.getWidth();
                int height=display.getHeight();
                Log.v("width", width+"");
                dialog.getWindow().setLayout((6*width)/7,(4*height)/5);

                ex_dialog_click_menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }


}