package com.af.cafeapp.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.af.cafeapp.R;
import com.af.cafeapp.adaptor.PayAdaptor;
import com.af.cafeapp.models.PayData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DialogPayDeli {
    private Context context;
    private float total;
    private Dialog dialog;
    private TextView choose_cash,choose_own,tvPrice,tvTotalNeed,tvPayed;
    private int typePay = 0;
    private int typeBills = 0;
    private int posPad =10;
    private float totalPayed=0;
    private ImageView imgShows;
    private String numStr = "";
    private LinearLayout clear,btn_full,conPad,confPay;
    private List<TextView> pad = new ArrayList<TextView>(Arrays.asList(new TextView[posPad]));
    private ArrayList<PayData> payDataArrayList = new ArrayList<>();
    private RecyclerView viewPay;

    private OnClicks mOnClicks;

    public interface OnClicks{
        void OnClicks(ArrayList<PayData> payDataArrayListGet);
    }

    public DialogPayDeli(Context context, float total,int typeBills, OnClicks onClicks) {
        this.context = context;
        this.total = total;
        this.dialog = new Dialog(context);
        this.mOnClicks = onClicks;
        this.typeBills = typeBills;
        initDialog();
    }

    private void initDialog(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_check_money_g_and_f);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ImageView ex_dialog = dialog.findViewById(R.id.ex_dialog_g_and_f);
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int height=display.getHeight();
        dialog.getWindow().setLayout((6*width)/7,(4*height)/5);
        dialog.show();

        tvPrice = dialog.findViewById(R.id.tvPrice);
        clear = dialog.findViewById(R.id.clear);
        btn_full = dialog.findViewById(R.id.btn_full);
        conPad = dialog.findViewById(R.id.conPad);
        viewPay = dialog.findViewById(R.id.viewPay);
        imgShows = dialog.findViewById(R.id.imgShows);

        tvPrice.setText(String.valueOf(Math.round(total)));

        if(typeBills==2){
            imgShows.setBackgroundResource(R.drawable.ic_grab_logo_svgrepo_com);
        }else if(typeBills==3){
            imgShows.setBackgroundResource(R.drawable.ic_foodpanda_svgrepo_com);
        }else if(typeBills==4){
            imgShows.setBackgroundResource(R.drawable.ic_rbh_logo_01_e1635235514424);
        }

        initSetClick();

        ex_dialog.setOnClickListener(view1 -> dialog.dismiss());
    }


    private void initSetClick() {


        conPad.setOnClickListener(view -> {

                setConfirmPay();

        });
    }

    private void setConfirmPay() {
        PayData payData = new PayData();
        if(typeBills==2){
            payData.setTypePay("Grab");
            payData.setTypePayPos(3);
        }else if(typeBills==3){
            payData.setTypePay("Food Panda");
            payData.setTypePayPos(4);
        }else if(typeBills==4){
            payData.setTypePay("Robin Hood");
            payData.setTypePayPos(5);
        }
        payData.setTotalPay(total);
        payDataArrayList.add(payData);
        mOnClicks.OnClicks(payDataArrayList);
        dialog.dismiss();
    }

    private void setPayData() {
        PayData payData = new PayData();
        if(typePay==1){
            payData.setTypePay("เงินสด");
            payData.setTypePayPos(1);
        }else{
            payData.setTypePay("เงินโอน");
            payData.setTypePayPos(2);
        }
        payData.setTotalPay(total);
        payDataArrayList.add(payData);
        setShowPay();
        calTotal();
    }

    private void setShowPay() {
        viewPay.setLayoutManager(new LinearLayoutManager(context));
        PayAdaptor payAdaptor = new PayAdaptor(context, payDataArrayList, position -> {
            payDataArrayList.remove(position);
            setShowPay();
            calTotal();
        });
        viewPay.setAdapter(payAdaptor);
    }

    private void calTotal(){
        totalPayed = 0;
        for(PayData payDataGet : payDataArrayList){
            totalPayed += payDataGet.getTotalPay();
        }
        tvPayed.setText(String.format(Locale.ENGLISH,"%.2f", totalPayed));
    }


}
