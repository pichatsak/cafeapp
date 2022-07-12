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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.af.cafeapp.R;
import com.af.cafeapp.adaptor.Shop_Expenses_Adaptor;
import com.af.cafeapp.data_class.data_add_list_shop_expenses;
import com.af.cafeapp.databinding.FragmentSlideshowBinding;
import com.af.cafeapp.shop_expenses;
import com.af.cafeapp.tools.DateTool;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private TextView dateCurShow;
    private ImageView backs;
    private LinearLayout add_list,add_list_shop_expenses;
    private ImageView ex_dialog_addshop_expenses;
    private EditText name_shop_expenses,money_shop_expenses;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView shop_expenses_view;
    private ArrayList<data_add_list_shop_expenses> data_add_list_shop_expenses_list = new ArrayList<>();
    DateTool dateTool = new DateTool();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        shop_expenses_view = root.findViewById(R.id.shop_expenses_view);
        dateCurShow = root.findViewById(R.id.dateCurShow);
        dateCurShow.setText(dateTool.getDayCur());
        backs = root.findViewById(R.id.backs);
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
                            data_add_shop_expenses();
                            dialog.dismiss();
                            Toast.makeText(getContext(), "เพิ่มรายการสำเร็จ", Toast.LENGTH_SHORT).show();
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

    public void getdata_shop_expenses(){
        // แบบเรียลไทม์
        db.collection("data_add_list_shop_expenses")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        data_add_list_shop_expenses_list = new ArrayList<>();

                        for (QueryDocumentSnapshot document : value) {
                            data_add_list_shop_expenses data_add_list_shop_expenses = new data_add_list_shop_expenses();
                            data_add_list_shop_expenses.setAdd_name_shop_expenses(document.getData().get("add_name_shop_expenses").toString());
                            data_add_list_shop_expenses.setAdd_money_shop_expenses(Integer.parseInt(document.getData().get("add_money_shop_expenses").toString()));
                            data_add_list_shop_expenses.setId(document.getId());

                            data_add_list_shop_expenses_list.add(data_add_list_shop_expenses);
                        }

                        shop_expenses_view.setLayoutManager(new LinearLayoutManager(getContext()));
                        Shop_Expenses_Adaptor Shop_Expenses_Adaptor = new Shop_Expenses_Adaptor(getContext(),data_add_list_shop_expenses_list);
                        shop_expenses_view.setAdapter(Shop_Expenses_Adaptor);

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