package com.af.cafeapp.ui.slideshow;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.af.cafeapp.MyApplication;
import com.af.cafeapp.R;
import com.af.cafeapp.adaptor.Shop_Expenses_Adaptor;
import com.af.cafeapp.data_class.data_add_list_shop_expenses;
import com.af.cafeapp.databinding.FragmentSlideshowBinding;
import com.af.cafeapp.models.ExpenseData;
import com.af.cafeapp.models.ExpenseHisData;
import com.af.cafeapp.models.ListDayExpense;
import com.af.cafeapp.tools.DateTool;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SlideshowFragmentOld extends Fragment {

    private FragmentSlideshowBinding binding;
    private TextView dateCurShow,tv_expense;
    private ImageView backs;
    private LinearLayout add_list,add_list_shop_expenses;
    private ImageView ex_dialog_addshop_expenses;
    private EditText name_shop_expenses,money_shop_expenses;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView shop_expenses_view;
    private ArrayList<data_add_list_shop_expenses> data_add_list_shop_expenses_list = new ArrayList<>();
    private ArrayList<ExpenseHisData> expenseHisDataArrayList = new ArrayList<>();
    DateTool dateTool = new DateTool();
    String nameUser = "";
    private float totalExpense=0;
    DecimalFormat df = new DecimalFormat("#,###.00");
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        shop_expenses_view = root.findViewById(R.id.shop_expenses_view);
        dateCurShow = root.findViewById(R.id.dateCurShow);
        dateCurShow.setText(dateTool.getDayCur());
        tv_expense = root.findViewById(R.id.tv_expense);
        backs = root.findViewById(R.id.backs);

        getKeyMonth();

        getdata_shop_expenses();
        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        add_list = root.findViewById(R.id.add_list);
        add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_add_shop_expenses);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                ex_dialog_addshop_expenses = dialog.findViewById(R.id.ex_dialog_addshop_expenses);
                dialog.show();
                Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                int width = display.getWidth();
                int height=display.getHeight();
                Log.v("width", width+"");
                dialog.getWindow().setLayout((6*width)/7,(4*height)/5);


                add_list_shop_expenses = dialog.findViewById(R.id.add_list_shop_expenses);
                name_shop_expenses = dialog.findViewById(R.id.name_shop_expenses);
                money_shop_expenses = dialog.findViewById(R.id.money_shop_expenses);
                add_list_shop_expenses.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (name_shop_expenses.getText().toString().isEmpty()) {
                            Toast.makeText(getContext(), "กรุณากรอกชื่อรายการ", Toast.LENGTH_SHORT).show();
                        } else if (money_shop_expenses.getText().toString().isEmpty()) {
                            Toast.makeText(getContext(), "กรุณากรอกจำนวนเงิน", Toast.LENGTH_SHORT).show();
                        }   else {

                            String myCur = dateTool.getMonthAndYearCur();
                            String dateCur = dateTool.getDayCur();

                            Date curDate = Calendar.getInstance().getTime();

                            ExpenseHisData data = new ExpenseHisData();
                            data.setKey("");
                            data.setDateCreate(curDate);
                            data.setPosCreate(2);
                            data.setPosCreateMore(nameUser);
                            data.setNameExpense(name_shop_expenses.getText().toString());
                            data.setTotal(Float.parseFloat(money_shop_expenses.getText().toString()));

                            DocumentReference docRef = db.collection("expense_his").document(myCur);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d("CHK_DRAW", "DocumentSnapshot data: " + document.getData());
                                            Map<String, Object> listDayMap = (Map<String, Object>) document.getData().get("listDayExpense");
                                            Map<String, Object> value = (Map<String, Object>) listDayMap.get(dateCur);
                                            if (value != null) {
                                                Log.d("CHK_CHK_DRAW", "have");
                                                String uidAdd = UUID.randomUUID().toString();
                                                db.collection("expense_his")
                                                        .document(myCur)
                                                        .update("listDayExpense." + dateCur + ".listExpense." + uidAdd, data).addOnSuccessListener(aVoid -> {
                                                    dialog.dismiss();
                                                    getdata_shop_expenses();
                                                });
                                            }
                                        } else {
                                            Log.d("CHK_DRAW", "No such document");
                                            ExpenseData expenseData = new ExpenseData();
                                            expenseData.setMonth(dateTool.getMonthCurInt());
                                            expenseData.setYear(dateTool.getYearCurInt());

                                            String uid = UUID.randomUUID().toString();
                                            final HashMap<String, Object> newList = new HashMap<>();
                                            newList.put(uid, data);

                                            ListDayExpense listDayExpense = new ListDayExpense();
                                            listDayExpense.setDate(dateCur);
                                            listDayExpense.setId("");
                                            listDayExpense.setListExpense(newList);

                                            final HashMap<String, Object> newDayList = new HashMap<>();
                                            newDayList.put(dateCur, listDayExpense);

                                            expenseData.setListDayExpense(newDayList);

                                            db.collection("expense_his").document(myCur)
                                                    .set(expenseData)
                                                    .addOnSuccessListener(aVoid -> {
                                                        dialog.dismiss();
                                                        getdata_shop_expenses();
                                                    });
                                        }
                                    }
                                }
                            });

//                            data_add_shop_expenses();
//                            dialog.dismiss();
//                            Toast.makeText(getContext(), "เพิ่มรายการสำเร็จ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



                ex_dialog_addshop_expenses.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


            }
        });

        return root;
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

    public void getdata_shop_expenses(){
        // แบบเรียลไทม์

        String myCur = dateTool.getMonthAndYearCur();
        String dateCur = dateTool.getDayCur();
        DocumentReference docRef = db.collection("expense_his").document(myCur);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> listDayMap = (Map<String, Object>) document.getData().get("listDayExpense");
                    Map<String, Object> value = (Map<String, Object>) listDayMap.get(dateCur);
                    if (value != null) {
                        Map<String, Object> listDataMap = (Map<String, Object>) value.get("listExpense");
                        expenseHisDataArrayList = new ArrayList<>();
                        totalExpense = 0;
                        for (Map.Entry<String, Object> entry : listDataMap.entrySet()) {
                            Map<String, Object> valueGet = (Map<String, Object>) entry.getValue();
                            ExpenseHisData data = new ExpenseHisData();
                            data.setTotal(Float.parseFloat(valueGet.get("total").toString()));
                            data.setNameExpense(valueGet.get("nameExpense").toString());
                            data.setKey(entry.getKey());
                            expenseHisDataArrayList.add(data);
                            totalExpense += Float.parseFloat(valueGet.get("total").toString());
                        }
                        shop_expenses_view.setLayoutManager(new LinearLayoutManager(getContext()));
                        Shop_Expenses_Adaptor shop_expenses_adaptor = new Shop_Expenses_Adaptor(getContext(), expenseHisDataArrayList);
                        shop_expenses_view.setAdapter(shop_expenses_adaptor);
                        tv_expense.setText(df.format(totalExpense));
                    }
                }
            }
        });

    }
    public void data_add_shop_expenses(){

        String Getname_shop_expenses = name_shop_expenses.getText().toString();
        String Getmoney_shop_expenses = money_shop_expenses.getText().toString();

        data_add_list_shop_expenses data_add_list_shop_expenses = new data_add_list_shop_expenses();
        data_add_list_shop_expenses.setAdd_name_shop_expenses(Getname_shop_expenses);
        data_add_list_shop_expenses.setAdd_money_shop_expenses(Integer.parseInt(Getmoney_shop_expenses));

        //  getData();

        db.collection("data_add_list_shop_expenses")
                .add(data_add_list_shop_expenses)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(getContext(), "เกิดข้อผิดพลาดลองใหม่อีกครั้ง", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}