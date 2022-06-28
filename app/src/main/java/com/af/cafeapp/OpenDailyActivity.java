package com.af.cafeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.af.cafeapp.models.DailyData;
import com.af.cafeapp.tools.DateTool;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class OpenDailyActivity extends AppCompatActivity {
    LinearLayout back, confirmOpen;
    EditText totalChange;
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
        Bundle bundle = getIntent().getExtras();
        keyMonthDataGet = bundle.getString("keyMonthData");
        Log.d("CHK_KEY", keyMonthDataGet);
        back.setOnClickListener(view -> finish());
        confirmOpen.setOnClickListener(view -> setOpen());
    }

    private void setOpen() {

        if (totalChange.getText().toString().isEmpty()) {
            Toast.makeText(this, "กรุณากรอกยอดเงิน", Toast.LENGTH_SHORT).show();
        } else {
            float totalChangeGet = Float.valueOf(totalChange.getText().toString());
            Date curDate = Calendar.getInstance().getTime();
            String strDate = dateTool.getDayCur();
            DailyData dailyData = new DailyData();
            dailyData.setDateCreate(curDate);
            dailyData.setDateDaily(curDate);
            dailyData.setDateStr(strDate);
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

                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

                    });

        }

    }
}