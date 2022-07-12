package com.af.cafeapp.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.af.cafeapp.R;
import com.af.cafeapp.adaptor.PayAdaptor;
import com.af.cafeapp.adaptor.ToppingAdaptor;
import com.af.cafeapp.models.PayData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DialogPay {
    private Context context;
    private float total;
    private Dialog dialog;
    private TextView choose_cash,choose_own,tvPrice,tvTotalNeed,tvPayed;
    private int typePay = 1;
    private int posPad =10;
    private float totalPayed=0;
    private String numStr = "";
    private LinearLayout clear,btn_full,conPad,confPay;
    private List<TextView> pad = new ArrayList<TextView>(Arrays.asList(new TextView[posPad]));
    private ArrayList<PayData> payDataArrayList = new ArrayList<>();
    private RecyclerView viewPay;
    private OnClicks mOnClicks;

    public interface OnClicks{
        void OnClicks(ArrayList<PayData> payDataArrayListGet);
    }

    public DialogPay(Context context, float total, OnClicks onClicks) {
        this.context = context;
        this.total = total;
        this.dialog = new Dialog(context);
        this.mOnClicks = onClicks;
        initDialog();
    }

    private void initDialog(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_check_money);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ImageView ex_dialog = dialog.findViewById(R.id.ex_dialog);
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int height=display.getHeight();
        dialog.getWindow().setLayout((6*width)/7,(4*height)/5);
        dialog.show();
        choose_cash = dialog.findViewById(R.id.choose_cash);
        choose_own = dialog.findViewById(R.id.choose_own);
        tvPrice = dialog.findViewById(R.id.tvPrice);
        clear = dialog.findViewById(R.id.clear);
        btn_full = dialog.findViewById(R.id.btn_full);
        tvTotalNeed = dialog.findViewById(R.id.tvTotalNeed);
        tvPayed = dialog.findViewById(R.id.tvPayed);
        conPad = dialog.findViewById(R.id.conPad);
        viewPay = dialog.findViewById(R.id.viewPay);
        confPay = dialog.findViewById(R.id.confPay);

        tvPayed.setText(String.format(Locale.ENGLISH,"%.2f", totalPayed));
        String totalNeedGet = String.format(Locale.ENGLISH,"%.2f", total);
        tvTotalNeed.setText(totalNeedGet);
        initNumPad();
        initSetClick();

        ex_dialog.setOnClickListener(view1 -> dialog.dismiss());
    }

    private void initNumPad() {
        for (int i = 0; i < posPad; i++) {
            String getId = "pad" +i ;
            pad.set(i, dialog.findViewById(context.getResources().getIdentifier(getId, "id", context.getPackageName())));
            int finalI = i;
            pad.get(i).setOnClickListener(v -> setShowPad(finalI));
        }
    }

    private void setShowPad(int num) {
        if(num==0&&numStr.isEmpty()){
            numStr = "";
        }else{
            numStr += String.valueOf(num);
        }
        tvPrice.setText(numStr);

    }

    private void initSetClick() {
        choose_cash.setOnClickListener(view -> {
            typePay =1;
            choose_cash.setBackgroundResource(R.drawable.check_fram_bar3);
            choose_own.setBackground(null);
            numStr="";
            tvPrice.setText(numStr);
        });
        choose_own.setOnClickListener(view -> {
            typePay =2;
            choose_own.setBackgroundResource(R.drawable.check_fram_bar3);
            choose_cash.setBackground(null);
            numStr="";
            tvPrice.setText(numStr);
        });
        clear.setOnClickListener(view -> {
            numStr = "";
            tvPrice.setText(numStr);
        });
        btn_full.setOnClickListener(view -> {
            numStr = String.valueOf(Math.round(total));
            tvPrice.setText(numStr);
            PayData payData = new PayData();
            if(typePay==1){
                payData.setTypePay("เงินสด");
                payData.setTypePayPos(1);
            }else{
                payData.setTypePay("เงินโอน");
                payData.setTypePayPos(2);
            }
            payData.setTotalPay(Float.parseFloat(numStr));
            payDataArrayList.add(payData);
            setShowPay();
            calTotal();
        });
        conPad.setOnClickListener(view -> {
            if(tvPrice.getText().toString().isEmpty()){
                Toast.makeText(context, "กรุณากรอกตัวเลขที่ถูกต้อง!", Toast.LENGTH_SHORT).show();
            }else{
                setPayData();
            }
        });
        confPay.setOnClickListener(view -> setConfirmPay());
    }

    private void setConfirmPay() {
        if(totalPayed<total){
            Toast.makeText(context, "ยอดเงินที่ชำระไม่ถูกต้อง!", Toast.LENGTH_SHORT).show();
        }else {
            mOnClicks.OnClicks(payDataArrayList);
            dialog.dismiss();
        }
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
        payData.setTotalPay(Float.parseFloat(numStr));
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
