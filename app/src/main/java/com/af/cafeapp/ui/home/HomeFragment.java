package com.af.cafeapp.ui.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.af.cafeapp.OpenDailyActivity;
import com.af.cafeapp.R;
import com.af.cafeapp.databinding.FragmentHomeBinding;
import com.af.cafeapp.models.MonthData;
import com.af.cafeapp.tools.DateTool;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"FieldCanBeLocal", "ConstantConditions", "unused", "unchecked"})
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TextView menuBar;
    boolean isOpenSale = false;
    private final DateTool dateTool = new DateTool();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String keyMonthData = "";
    final static int LAUNCH_OPEN_SALE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        menuBar = root.findViewById(R.id.menuBar);
        menuBar.setOnClickListener(view -> {
            DrawerLayout drawer = container.getRootView().findViewById(R.id.drawer_layout);
            drawer.open();
        });


        checkNewMonthData();
        return root;
    }

    private void checkNewMonthData() {
        int getMonthCur = dateTool.getMonthCurInt();
        int getYearCur = dateTool.getYearCurInt();
        db.collection("data_month")
                .whereEqualTo("month", getMonthCur)
                .whereEqualTo("year", getYearCur)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() == 0) {
                            final HashMap<String, Object> newList = new HashMap<>();
                            MonthData monthData = new MonthData();
                            monthData.setMonth(getMonthCur);
                            monthData.setYear(getYearCur);
                            monthData.setListData(newList);
                            db.collection("data_month")
                                    .add(monthData)
                                    .addOnSuccessListener(documentReference -> {
                                        keyMonthData = documentReference.getId();
                                        checkOpenDaily();
                                    })
                                    .addOnFailureListener(e -> Log.w("CHK_DB", "Error adding document", e));
                        } else {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                keyMonthData = document.getId();
                                checkOpenDaily();
                            }

                        }
                    } else {
                        Log.d("CHK_DB", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void checkOpenDaily() {
        String dateCur = dateTool.getDayCur();
        DocumentReference docRef = db.collection("data_month").document(keyMonthData);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {

                    Log.d("CHK_DB", "DocumentSnapshot data: " + document.getData());
                    Map<String, Object> ListDayMap = (Map<String, Object>) document.getData().get("listData");
                    if (ListDayMap.size() == 0) {
                        openDialogOpenSale();
                    }else{
                        Map<String, Object> value = (Map<String, Object>) ListDayMap.get(dateCur);
                        Log.d("CHK_DATA",value.toString());
                        if(value.size()==0){
                            openDialogOpenSale();
                        }else{
                            isOpenSale = true;
                        }
                    }

                } else {
                    Log.d("CHK_DB", "No such document");
                }
            } else {
                Log.d("CHK_DB", "get failed with ", task.getException());
            }
        });


    }

    private void openDialogOpenSale(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_open_daily);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        dialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 5);
        LinearLayout confirm = dialog.findViewById(R.id.confirm);
        LinearLayout cancel = dialog.findViewById(R.id.cancel);
        ImageView ex_dialog_add_topping = dialog.findViewById(R.id.ex_dialog_add_topping);
        cancel.setOnClickListener(view -> dialog.dismiss());
        confirm.setOnClickListener(view -> {
            dialog.dismiss();
            Intent intent = new Intent(getContext(), OpenDailyActivity.class);
            intent.putExtra("keyMonthData",keyMonthData);
            startActivityForResult(intent,LAUNCH_OPEN_SALE);
        });
        ex_dialog_add_topping.setOnClickListener(view -> dialog.dismiss());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("CHK_RES", "requestCode : " + requestCode + " resultCode: " + resultCode);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == LAUNCH_OPEN_SALE){
                isOpenSale = true;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}