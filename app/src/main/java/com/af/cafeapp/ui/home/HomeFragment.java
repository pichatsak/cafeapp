package com.af.cafeapp.ui.home;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.af.cafeapp.OpenDailyActivity;
import com.af.cafeapp.R;
import com.af.cafeapp.SaveMenuActivity;
import com.af.cafeapp.adaptor.MenuAdaptor;
import com.af.cafeapp.databinding.FragmentHomeBinding;
import com.af.cafeapp.models.MenuDataModel;
import com.af.cafeapp.models.MonthData;
import com.af.cafeapp.tools.DateTool;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
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
    final static int LAUNCH_SAVE_MENU = 2;
    LinearLayout add_menu_home,openSale,closeSale;
    RecyclerView viewMenu,viewCart;
    TextView chooseTypeBill;
    int typeBill = 1;
    ArrayList<MenuDataModel> menuDataModelArrayList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        add_menu_home = root.findViewById(R.id.add_menu_home);
        menuBar = root.findViewById(R.id.menuBar);
        viewMenu = root.findViewById(R.id.viewMenu);
        openSale = root.findViewById(R.id.openSale);
        closeSale = root.findViewById(R.id.closeSale);
        chooseTypeBill = root.findViewById(R.id.chooseTypeBill);
        menuBar.setOnClickListener(view -> {
            DrawerLayout drawer = container.getRootView().findViewById(R.id.drawer_layout);
            drawer.open();
        });

        checkNewMonthData();
        goToAddMenu();
        getShowMenu();
        setClickChooseType();
        setShowChooseType();
        return root;
    }

    @SuppressLint("SetTextI18n")
    private void setShowChooseType() {
        if(typeBill==1){
            chooseTypeBill.setText("ขายหน้าร้าน");
            chooseTypeBill.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_shop_svgrepo_com, 0);
        }else if(typeBill==2){
            chooseTypeBill.setText("Grab");
            chooseTypeBill.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_grab_logo_svgrepo_com, 0);
        }else if(typeBill==3){
            chooseTypeBill.setText("Food Panda");
            chooseTypeBill.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_foodpanda_svgrepo_com, 0);
        }else if(typeBill==4){
            chooseTypeBill.setText("Robin Hood");
            chooseTypeBill.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_rbh_logo_01_e1635235514424, 0);
        }
    }

    private void setClickChooseType() {
        chooseTypeBill.setOnClickListener(view -> {
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_change_type);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            ImageView ex_dialog_add_topping = dialog.findViewById(R.id.ex_dialog_add_topping);
            TextView chooseSale = dialog.findViewById(R.id.chooseSale);
            TextView chooseGrab = dialog.findViewById(R.id.chooseGrab);
            TextView chooseFood = dialog.findViewById(R.id.chooseFood);
            TextView chooseRobin = dialog.findViewById(R.id.chooseRobin);
            dialog.show();
            ex_dialog_add_topping.setOnClickListener(view1 -> dialog.dismiss());
            chooseSale.setOnClickListener(view12 -> {
                typeBill = 1;
                dialog.dismiss();
                setShowChooseType();
            });
            chooseGrab.setOnClickListener(view12 -> {
                typeBill = 2;
                dialog.dismiss();
                setShowChooseType();
            });
            chooseFood.setOnClickListener(view12 -> {
                typeBill = 3;
                dialog.dismiss();
                setShowChooseType();
            });
            chooseRobin.setOnClickListener(view12 -> {
                typeBill = 4;
                dialog.dismiss();
                setShowChooseType();
            });


        });
    }

    private void setShowBtnSale(){
        if(isOpenSale){
            openSale.setVisibility(View.GONE);
            closeSale.setVisibility(View.VISIBLE);
        }else{
            openSale.setVisibility(View.VISIBLE);
            closeSale.setVisibility(View.GONE);
        }
    }

    private void getShowMenu(){
        menuDataModelArrayList = new ArrayList<>();
        DocumentReference docRef = db.collection("menu").document("menu-all");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> menuMap = (Map<String, Object>) document.getData().get("listMenu");
                    for (Map.Entry<String, Object> entry : menuMap.entrySet()) {
                        MenuDataModel menuData = new MenuDataModel();
                        Map<String, Object> value = (Map<String, Object>) entry.getValue();
                        menuData.setKey(entry.getKey());
                        menuData.setMenuName(value.get("menuName").toString());
                        menuData.setPriceNm(Float.parseFloat(value.get("priceNm").toString()));
                        menuData.setPriceSp(Float.parseFloat(value.get("priceSp").toString()));
                        menuData.setPriceNmGrab(Float.parseFloat(value.get("priceNmGrab").toString()));
                        menuData.setPriceSpGrab(Float.parseFloat(value.get("priceSpGrab").toString()));
                        menuData.setPriceNmFood(Float.parseFloat(value.get("priceNmFood").toString()));
                        menuData.setPriceSpFood(Float.parseFloat(value.get("priceSpFood").toString()));
                        menuData.setPriceNmRobin(Float.parseFloat(value.get("priceNmRobin").toString()));
                        menuData.setPriceSpRobin(Float.parseFloat(value.get("priceSpRobin").toString()));
                        menuDataModelArrayList.add(menuData);
                    }
                    viewMenu.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
                    viewMenu.setLayoutManager(mLayoutManager);
                    MenuAdaptor menuAdaptor = new MenuAdaptor(getContext(), menuDataModelArrayList, position -> {

                    });
                    viewMenu.setAdapter(menuAdaptor);

                }
            } else {
                Log.d("CHK_DB", "get failed with ", task.getException());
            }
        });

    }

    private void goToAddMenu() {
        add_menu_home.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), SaveMenuActivity.class);
            startActivityForResult(intent,LAUNCH_SAVE_MENU);
        });
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
                        if(value == null){
                            openDialogOpenSale();
                        }else{
                            isOpenSale = true;
                            setShowBtnSale();
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
                setShowBtnSale();
            }else if(requestCode==LAUNCH_SAVE_MENU){
                getShowMenu();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}