package com.af.cafeapp.ui.drawer_cash;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.af.cafeapp.MyApplication;
import com.af.cafeapp.R;
import com.af.cafeapp.SaveMenuActivity;
import com.af.cafeapp.adaptor.CashDrawerAdaptor;
import com.af.cafeapp.adaptor.StockAdaptor;
import com.af.cafeapp.data_class.data_add_list_stock;
import com.af.cafeapp.databinding.FragmentDrawerCashBinding;
import com.af.cafeapp.models.DrawerCashData;
import com.af.cafeapp.models.DrawerHisData;
import com.af.cafeapp.models.ListDayDrawer;
import com.af.cafeapp.tools.DateTool;
import com.af.cafeapp.ui.gallery.GalleryViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class DrawerCashFragment extends Fragment {

    private FragmentDrawerCashBinding binding;
    private LinearLayout AddDrawerHis;
    private ImageView backs;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView drawerView;
    private ArrayList<data_add_list_stock> data_add_list_stocks_list = new ArrayList<>();
    private DateTool dateTool = new DateTool();
    private String nameUser = "";
    ArrayList<DrawerHisData> drawerHisDataArrayList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentDrawerCashBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        drawerView = root.findViewById(R.id.drawerView);
        backs = root.findViewById(R.id.backs);
        AddDrawerHis = root.findViewById(R.id.AddDrawerHis);


        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        getKeyMonth();
        getData();
        AddDrawerHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_add_drawe);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                ImageView ex_dialog_addstock = dialog.findViewById(R.id.ex_dialog_addstock);
                dialog.show();
                Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                int width = display.getWidth();
                int height = display.getHeight();
                dialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 5);
                AtomicReference<String> typeDataMain = new AtomicReference<>("in");
                EditText total = dialog.findViewById(R.id.total);
                EditText note = dialog.findViewById(R.id.note);
                LinearLayout add_his = dialog.findViewById(R.id.add_his);
                RadioGroup typeData = dialog.findViewById(R.id.typeData);
                typeData.setOnCheckedChangeListener((group, checkedId) -> {
                            if (checkedId == R.id.inDraw) {
                                typeDataMain.set("in");
                            } else {
                                typeDataMain.set("out");
                            }
                        }
                );
                ex_dialog_addstock.setOnClickListener(view12 -> dialog.dismiss());
                String myCur = dateTool.getMonthAndYearCur();
                String dateCur = dateTool.getDayCur();
                add_his.setOnClickListener(view1 -> {
                    if (total.getText().toString().isEmpty() || Float.parseFloat(total.getText().toString()) == 0) {
                        Toast.makeText(getContext(), "กรุณากรอกยอดเงิน", Toast.LENGTH_SHORT).show();
                    } else if (note.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "กรุณากรอกหมายเหตุ", Toast.LENGTH_SHORT).show();
                    } else {
                        Date curDate = Calendar.getInstance().getTime();
                        DrawerHisData data = new DrawerHisData();
                        data.setKey("");
                        data.setDateCreate(curDate);
                        data.setPosCreate(2);
                        data.setTypeData(typeDataMain.get());
                        data.setPosCreateMore(nameUser);
                        data.setTotal(Float.parseFloat(total.getText().toString()));
                        data.setNote(note.getText().toString());
                        data.setStatusStart("no");

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
                                        if (value != null) {
                                            Log.d("CHK_CHK_DRAW", "have");
                                            String uidAdd = UUID.randomUUID().toString();
                                            db.collection("drawer_cash_his")
                                                    .document(myCur)
                                                    .update("listDayDrawer." + dateCur + ".listDrawer." + uidAdd, data).addOnSuccessListener(aVoid -> {
                                                dialog.dismiss();
                                                getData();
                                            });
                                        }
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
                                                    dialog.dismiss();
                                                    getData();
                                                });
                                    }
                                }
                            }
                        });


                    }
                });

            }
        });
        return root;
    }



    private void getData() {
        String myCur = dateTool.getMonthAndYearCur();
        String dateCur = dateTool.getDayCur();
        DocumentReference docRef = db.collection("drawer_cash_his").document(myCur);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> listDayMap = (Map<String, Object>) document.getData().get("listDayDrawer");
                    Map<String, Object> value = (Map<String, Object>) listDayMap.get(dateCur);
                    if (value != null) {
                        Map<String, Object> listDataMap = (Map<String, Object>) value.get("listDrawer");
                        drawerHisDataArrayList = new ArrayList<>();
                        for (Map.Entry<String, Object> entry : listDataMap.entrySet()) {
                            Map<String, Object> valueGet = (Map<String, Object>) entry.getValue();
                            DrawerHisData data = new DrawerHisData();
                            data.setTotal(Float.parseFloat(valueGet.get("total").toString()));
                            data.setNote(valueGet.get("note").toString());
                            data.setTypeData(valueGet.get("typeData").toString());
                            data.setStatusStart(valueGet.get("statusStart").toString());
                            data.setKey(entry.getKey());
                            drawerHisDataArrayList.add(data);
                        }
                        drawerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        CashDrawerAdaptor cashDrawerAdaptor = new CashDrawerAdaptor(getContext(), drawerHisDataArrayList);
                        drawerView.setAdapter(cashDrawerAdaptor);
                    }
                }
            }
        });
    }

    private void getKeyMonth() {
        DocumentReference docRef = db.collection("data_month").document(MyApplication.getMonthDataKey());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> listDayMap = (Map<String, Object>) document.getData().get("listData");
                    Map<String, Object> value = (Map<String, Object>) listDayMap.get(dateTool.getDayCur());
                    nameUser = value.get("nameUser").toString();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}