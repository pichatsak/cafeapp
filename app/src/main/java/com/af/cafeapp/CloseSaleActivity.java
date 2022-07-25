package com.af.cafeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.af.cafeapp.models.DrawerCashData;
import com.af.cafeapp.models.ListDayDrawer;
import com.af.cafeapp.models.MonthSalle;
import com.af.cafeapp.tools.DateTool;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CloseSaleActivity extends AppCompatActivity {
    LinearLayout back;
    private final DateTool dateTool = new DateTool();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView tv_total_all;
    private float totalAll = 0;
    private float totalSaleNormal = 0;
    private float totalSaleGrab = 0;
    private float totalSaleFood = 0;
    private float totalSaleRobin = 0;

    private float totalPayCash = 0;
    private float totalPayOwn = 0;
    private float totalPayGrab = 0;
    private float totalPayFood = 0;
    private float totalPayRobin = 0;

    private float totalDrawStart = 0;
    private float totalChange = 0;
    private float totalAmountCashDraw = 0;
    private float totalInDraw = 0;
    private float totalOutDraw = 0;
    private float totalExpense = 0;

    private float totalSaleCash = 0;
    private float totalSaleOwn = 0;
    String keyDay = "";


    TextView tv_draw_start, tv_draw_pay_cash, tv_total_change, tv_total_amount, tv_total_in_draw, tv_total_out_draw, tv_total_expense;
    TextView tv_pay_cash, tv_pay_own, tv_pay_grab, tv_pay_food, tv_pay_robin;
    TextView tv_total_normal, tv_total_grab, tv_total_food, tv_total_robin;
    DecimalFormat df = new DecimalFormat("#,###.00");
    LinearLayout confirmClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_sale);
        getSupportActionBar().hide();
        back = findViewById(R.id.back);
        back.setOnClickListener(view -> finish());

        tv_total_all = findViewById(R.id.tv_total_all);
        tv_total_normal = findViewById(R.id.tv_total_normal);
        tv_total_grab = findViewById(R.id.tv_total_grab);
        tv_total_food = findViewById(R.id.tv_total_food);
        tv_total_robin = findViewById(R.id.tv_total_robin);

        tv_pay_cash = findViewById(R.id.tv_pay_cash);
        tv_pay_own = findViewById(R.id.tv_pay_own);
        tv_pay_grab = findViewById(R.id.tv_pay_grab);
        tv_pay_food = findViewById(R.id.tv_pay_food);
        tv_pay_robin = findViewById(R.id.tv_pay_robin);

        tv_draw_start = findViewById(R.id.tv_draw_start);
        tv_draw_pay_cash = findViewById(R.id.tv_draw_pay_cash);
        tv_total_change = findViewById(R.id.tv_total_change);
        tv_total_amount = findViewById(R.id.tv_total_amount);
        tv_total_in_draw = findViewById(R.id.tv_total_in_draw);
        tv_total_out_draw = findViewById(R.id.tv_total_out_draw);
        tv_total_expense = findViewById(R.id.tv_total_expense);

        confirmClose = findViewById(R.id.confirmClose);
        Log.d("CHK_KEY", MyApplication.getMonthDataKey());

        getData();
        setClickClose();
    }

    private void setClickClose() {
        String myCur = dateTool.getMonthAndYearCur();
        String dayCur = dateTool.getDayCur();
        confirmClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("CHK_HAVE", myCur);
                Log.d("CHK_HAVE", dayCur);
                DocumentReference docRef = db.collection("month_sale").document(myCur);
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("CHK_HAVE", "have");
                            Map<String, Object> dayMap = (Map<String, Object>) document.getData().get("listDaySale");
                            Map<String, Object> value = (Map<String, Object>) dayMap.get(dayCur);
                            if (value == null) {
                                String uidDay = dayCur;
                                Log.d("CHK_HAVE", "no have day");
                                db.collection("daily_sale")
                                        .whereEqualTo("dateStr", dateTool.getDayCur())
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                                    keyDay = document1.getId();
                                                    HashMap<String, Object> map = (HashMap<String, Object>) document1.getData();
                                                    db.collection("month_sale").document(myCur)
                                                            .update("listDaySale." + uidDay, map)
                                                            .addOnSuccessListener(aVoid -> {
                                                                setEndSale();
                                                            });
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.d("CHK_HAVE", "no have");
                            MonthSalle monthSalle = new MonthSalle();
                            monthSalle.setMonth(dateTool.getMonthCurInt());
                            monthSalle.setYear(dateTool.getYearCurInt());
                            db.collection("daily_sale")
                                    .whereEqualTo("dateStr", dateTool.getDayCur())
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                                keyDay = document1.getId();
                                                HashMap<String, Object> map = (HashMap<String, Object>) document1.getData();
                                                HashMap<String, Object> newList = new HashMap<>();
                                                newList.put(dateTool.getDayCur(), map);
                                                monthSalle.setListDaySale(newList);
                                                db.collection("month_sale").document(myCur)
                                                        .set(monthSalle)
                                                        .addOnSuccessListener(aVoid -> {
                                                            setEndSale();
                                                        });
                                            }
                                        }
                                    });

                        }
                    }
                });
            }
        });
    }

    private void setEndSale() {
        db.collection("data_month").document(MyApplication.getMonthDataKey())
                .update(
                        "listData." + dateTool.getDayCur() + ".status", "end",
                        "listData." + dateTool.getDayCur() + ".dateEnd", FieldValue.serverTimestamp(),
                        "listData." + dateTool.getDayCur() + ".totalChange", totalChange,
                        "listData." + dateTool.getDayCur() + ".totalSale", totalAll,
                        "listData." + dateTool.getDayCur() + ".totalTakeOut", totalOutDraw,
                        "listData." + dateTool.getDayCur() + ".totalPayCash", totalPayCash,
                        "listData." + dateTool.getDayCur() + ".totalSaleCash", totalSaleCash,
                        "listData." + dateTool.getDayCur() + ".totalSaleOwn", totalPayOwn,
                        "listData." + dateTool.getDayCur() + ".totalSaleGrab", totalPayGrab,
                        "listData." + dateTool.getDayCur() + ".totalSaleFood", totalPayFood,
                        "listData." + dateTool.getDayCur() + ".totalSaleRobin", totalPayRobin,
                        "listData." + dateTool.getDayCur() + ".totalDrawerIn", totalInDraw
                )
                .addOnSuccessListener(aVoid -> {
                    db.collection("daily_sale").document(keyDay).delete()
                            .addOnSuccessListener(aVoid2 -> {
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            });
                });
    }

    private void getMonthData() {
        DocumentReference docRef = db.collection("data_month").document(MyApplication.getMonthDataKey());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> listDayMap = (Map<String, Object>) document.getData().get("listData");
                    Map<String, Object> value = (Map<String, Object>) listDayMap.get(dateTool.getDayCur());
                    totalDrawStart += Float.parseFloat(value.get("totalDrawerStart").toString());
                    if (totalDrawStart > 0) {
                        tv_draw_start.setText(" : " + df.format(totalDrawStart));
                    }

                    getDataHisDrawer();

                }
            }
        });
    }

    private void getDataHisDrawer() {

        String myCur = dateTool.getMonthAndYearCur();
        String dateCur = dateTool.getDayCur();

        DocumentReference docRef = db.collection("drawer_cash_his").document(myCur);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("CHK_DRAW", "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> listDayMap = (Map<String, Object>) document.getData().get("listDayDrawer");
                        Map<String, Object> value = (Map<String, Object>) listDayMap.get(dateCur);
                        Map<String, Object> listDataMap = (Map<String, Object>) value.get("listDrawer");
                        for (Map.Entry<String, Object> entry : listDataMap.entrySet()) {
                            Map<String, Object> valueGet = (Map<String, Object>) entry.getValue();
                            String getTypeDraw = valueGet.get("typeData").toString();
                            String getStatus = valueGet.get("statusStart").toString();
                            if (getStatus.equals("no")) {
                                if (getTypeDraw.equals("in")) {
                                    totalInDraw += Float.parseFloat(valueGet.get("total").toString());
                                } else {
                                    totalOutDraw += Float.parseFloat(valueGet.get("total").toString());
                                }
                            }
                        }
                        if (totalInDraw > 0) {
                            tv_total_in_draw.setText(" : " + df.format(totalInDraw));
                        }
                        if (totalOutDraw > 0) {
                            tv_total_out_draw.setText(" : " + df.format(totalOutDraw));
                        }
                        totalAmountCashDraw = (totalDrawStart + totalPayCash + totalInDraw) - (totalChange + totalOutDraw);
                        if (totalAmountCashDraw > 0) {
                            tv_total_amount.setText(" : " + df.format(totalAmountCashDraw));
                        }
                    }
                }
            }
        });


    }

    public void getData() {
        String dateCur = dateTool.getDayCur();
        db.collection("daily_sale")
                .whereEqualTo("dateStr", dateCur)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> billListMap = (Map<String, Object>) document.getData().get("listBill");
                            for (Map.Entry<String, Object> entry : billListMap.entrySet()) {
                                Map<String, Object> value = (Map<String, Object>) entry.getValue();
                                String getTypeBill = value.get("typeBill").toString();
                                totalAll += Float.parseFloat(value.get("total").toString());
                                if (getTypeBill.equals("1")) {
                                    totalSaleNormal += Float.parseFloat(value.get("total").toString());
                                } else if (getTypeBill.equals("2")) {
                                    totalSaleGrab += Float.parseFloat(value.get("total").toString());
                                } else if (getTypeBill.equals("3")) {
                                    totalSaleFood += Float.parseFloat(value.get("total").toString());
                                } else if (getTypeBill.equals("4")) {
                                    totalSaleRobin += Float.parseFloat(value.get("total").toString());
                                }

                                totalChange += Float.parseFloat(value.get("totalChange").toString());

                                Map<String, Object> payMap = (Map<String, Object>) value.get("listPay");
                                for (Map.Entry<String, Object> entryPay : payMap.entrySet()) {
                                    Map<String, Object> valuePay = (Map<String, Object>) entryPay.getValue();
                                    int getTypePay = Integer.parseInt(valuePay.get("typePayPos").toString());
                                    if (getTypePay == 1) {
                                        totalPayCash += Float.parseFloat(valuePay.get("totalPay").toString());
                                        totalSaleCash += Float.parseFloat(valuePay.get("totalPay").toString()) - Float.parseFloat(value.get("totalChange").toString());
                                    } else if (getTypePay == 2) {
                                        totalPayOwn += Float.parseFloat(valuePay.get("totalPay").toString());
                                    } else if (getTypePay == 3) {
                                        totalPayGrab += Float.parseFloat(valuePay.get("totalPay").toString());
                                    } else if (getTypePay == 4) {
                                        totalPayFood += Float.parseFloat(valuePay.get("totalPay").toString());
                                    } else if (getTypePay == 5) {
                                        totalPayRobin += Float.parseFloat(valuePay.get("totalPay").toString());
                                    }

                                }
                            }
                            tv_total_all.setText(df.format(totalAll));
                            if (totalSaleNormal > 0) {
                                tv_total_normal.setText(" : " + df.format(totalSaleNormal));
                            }
                            if (totalSaleGrab > 0) {
                                tv_total_grab.setText(" : " + df.format(totalSaleGrab));
                            }
                            if (totalSaleFood > 0) {
                                tv_total_food.setText(" : " + df.format(totalSaleFood));
                            }
                            if (totalSaleRobin > 0) {
                                tv_total_robin.setText(" : " + df.format(totalSaleRobin));
                            }
                            if (totalPayCash > 0) {
                                tv_pay_cash.setText(" : " + df.format(totalPayCash));
                                tv_draw_pay_cash.setText(" : " + df.format(totalPayCash));
                            }
                            if (totalPayOwn > 0) {
                                tv_pay_own.setText(" : " + df.format(totalPayOwn));
                            }
                            if (totalPayGrab > 0) {
                                tv_pay_grab.setText(" : " + df.format(totalPayGrab));
                            }
                            if (totalPayFood > 0) {
                                tv_pay_food.setText(" : " + df.format(totalPayFood));
                            }
                            if (totalPayRobin > 0) {
                                tv_pay_robin.setText(" : " + df.format(totalPayRobin));
                            }
                            if (totalChange > 0) {
                                tv_total_change.setText(" : " + df.format(totalChange));
                            }
                            getMonthData();

                        }

                    } else {
                        Log.d("CHK_DB", "Error getting documents: ", task.getException());
                    }
                });
    }

}