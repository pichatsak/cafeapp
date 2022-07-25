package com.af.cafeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.af.cafeapp.models.DailyData;
import com.af.cafeapp.models.DailySale;
import com.af.cafeapp.models.DrawerCashData;
import com.af.cafeapp.models.DrawerHisData;
import com.af.cafeapp.models.ListDayDrawer;
import com.af.cafeapp.tools.DateTool;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OpenDailyActivity extends AppCompatActivity {
    LinearLayout back, confirmOpen;
    EditText totalChange, nameUser;
    String keyMonthDataGet = "";
    private final DateTool dateTool = new DateTool();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_daily);
        back = findViewById(R.id.back);
        confirmOpen = findViewById(R.id.confirmOpen);
        totalChange = findViewById(R.id.totalChange);
        nameUser = findViewById(R.id.nameUser);
        Bundle bundle = getIntent().getExtras();
        keyMonthDataGet = bundle.getString("keyMonthData");
        Log.d("CHK_KEY", keyMonthDataGet);
        back.setOnClickListener(view -> finish());
        confirmOpen.setOnClickListener(view -> setOpen());
    }

    private void setOpen() {

        if (totalChange.getText().toString().isEmpty()) {
            Toast.makeText(this, "กรุณากรอกยอดเงิน", Toast.LENGTH_SHORT).show();
        } else if (nameUser.getText().toString().isEmpty()) {
            Toast.makeText(this, "กรุณากรอกชื่อพนักงาน", Toast.LENGTH_SHORT).show();
        } else {
            float totalChangeGet = Float.parseFloat(totalChange.getText().toString());
            Date curDate = Calendar.getInstance().getTime();
            String strDate = dateTool.getDayCur();
            DailyData dailyData = new DailyData();
            dailyData.setDateCreate(curDate);
            dailyData.setDateDaily(curDate);
            dailyData.setDateStr(strDate);
            dailyData.setNameUser(nameUser.getText().toString());
            dailyData.setTotalDrawerStart(totalChangeGet);
            dailyData.setTotalSale(0.0F);
            dailyData.setTotalChange(0.0F);
            dailyData.setTotalAmount(0.0F);
            dailyData.setTotalTakeOut(0.0F);
            dailyData.setTotalExpends(0.0F);
            dailyData.setStatus("running");

            db.collection("data_month").document(keyMonthDataGet)
                    .update("listData." + strDate, dailyData)
                    .addOnSuccessListener(aVoid -> {
                        final HashMap<String, Object> newList = new HashMap<>();
                        DailySale dailySale = new DailySale();
                        dailySale.setBillRun(1);
                        dailySale.setDateCreate(curDate);
                        dailySale.setDateStr(strDate);
                        dailySale.setListBill(newList);
                        db.collection("daily_sale")
                                .add(dailySale)
                                .addOnSuccessListener(documentReference -> {
                                    DrawerHisData data = new DrawerHisData();
                                    data.setKey("");
                                    data.setDateCreate(curDate);
                                    data.setPosCreate(2);
                                    data.setTypeData("in");
                                    data.setPosCreateMore(nameUser.getText().toString());
                                    data.setTotal(totalChangeGet);
                                    data.setNote("เงินตั้งต้น");
                                    data.setStatusStart("yes");

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

                                                    String uid = UUID.randomUUID().toString();
                                                    final HashMap<String, Object> newList = new HashMap<>();
                                                    newList.put(uid, data);

                                                    ListDayDrawer listDayDrawer = new ListDayDrawer();
                                                    listDayDrawer.setDate(dateCur);
                                                    listDayDrawer.setId("");
                                                    listDayDrawer.setListDrawer(newList);

                                                    db.collection("drawer_cash_his")
                                                            .document(myCur)
                                                            .update("listDayDrawer."+ dateCur, listDayDrawer).addOnSuccessListener(aVoid -> {
                                                        Intent returnIntent = new Intent();
                                                        setResult(Activity.RESULT_OK, returnIntent);
                                                        finish();
                                                    });
                                                } else {
                                                    Log.d("CHK_DRAW", "No such document");
                                                    DrawerCashData drawerCashData = new DrawerCashData();
                                                    drawerCashData.setMonth(dateTool.getMonthCurInt());
                                                    drawerCashData.setYear(dateTool.getYearCurInt());

                                                    String uid = UUID.randomUUID().toString();
                                                    final HashMap<String, Object> newList = new HashMap<>();
                                                    newList.put(uid, data);

                                                    ListDayDrawer listDayDrawer = new ListDayDrawer();
                                                    listDayDrawer.setDate(dateCur);
                                                    listDayDrawer.setId("");
                                                    listDayDrawer.setListDrawer(newList);

                                                    final HashMap<String, Object> newDayList = new HashMap<>();
                                                    newDayList.put(dateCur, listDayDrawer);

                                                    drawerCashData.setListDayDrawer(newDayList);

                                                    db.collection("drawer_cash_his").document(myCur)
                                                            .set(drawerCashData)
                                                            .addOnSuccessListener(aVoid -> {
                                                                Intent returnIntent = new Intent();
                                                                setResult(Activity.RESULT_OK, returnIntent);
                                                                finish();
                                                            });
                                                }
                                            }
                                        }
                                    });
//
//                                    Intent returnIntent = new Intent();
//                                    setResult(Activity.RESULT_OK, returnIntent);
//                                    finish();

                                });
                    });

        }

    }
}