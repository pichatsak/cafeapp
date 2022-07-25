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

import com.af.cafeapp.RealmDB.PrintSetRm;
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

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
//    private LinearLayout checkBill;  // ปุ่มเช็คบิล
  //  private ImageView ex_dialog ; //ออกไดอาร็อคชำระเงิน
    private TextView checklish;
    private ImageView ex_dialog_g_and_f ;
//    private LinearLayout add_menu_home; //เพิ่มเมนู
    private ImageView ex_dialog_addmenu_home;
    private LinearLayout add_topping;
    private ImageView ex_dialog_add_topping;
    private LinearLayout menu1;
    private ImageView ex_dialog_click_menu;
    Realm realm = Realm.getDefaultInstance();
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
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_drawer_cash)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        getSupportActionBar().hide();
        //  CloseBar closeBar = new CloseBar(this);



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


        checkPrintSet();
    }


    public void checkPrintSet(){
        RealmResults<PrintSetRm> printSetRms = realm.where(PrintSetRm.class).findAll();
        if(printSetRms.isEmpty()){
            realm.beginTransaction();
            PrintSetRm printSetRm = realm.createObject(PrintSetRm.class, UUID.randomUUID().toString());
            printSetRm.setSizePaper(58f);
            printSetRm.setPerLine(48);
            printSetRm.setDpiPrinter(203);
            realm.commitTransaction();
        }
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