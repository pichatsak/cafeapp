package com.af.cafeapp.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.af.cafeapp.OpenDailyActivity;
import com.af.cafeapp.R;
import com.af.cafeapp.SaveMenuActivity;
import com.af.cafeapp.adaptor.CartAdaptor;
import com.af.cafeapp.adaptor.MenuAdaptor;
import com.af.cafeapp.adaptor.ToppingChooseAdaptor;
import com.af.cafeapp.databinding.FragmentHomeBinding;
import com.af.cafeapp.models.CartData;
import com.af.cafeapp.models.MenuDataModel;
import com.af.cafeapp.models.MonthData;
import com.af.cafeapp.models.ToppingChooseModel;
import com.af.cafeapp.tools.DateTool;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings({"FieldCanBeLocal", "ConstantConditions", "unused", "unchecked"})
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TextView menuBar,showNumBill;
    boolean isOpenSale = false;
    private final DateTool dateTool = new DateTool();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String keyMonthData = "";
    final static int LAUNCH_OPEN_SALE = 1;
    final static int LAUNCH_SAVE_MENU = 2;
    LinearLayout add_menu_home, openSale, closeSale,checkBill;
    RecyclerView viewMenu, viewCart;
    TextView chooseTypeBill;
    int typeBill = 1;
    String keyDailySale = "";
    TextView totalShowBill;
    int billNo=0;
    float totalBill=0;
    float totalPay=0;
    float totalPayed=0;
    float totalChange=0;
    ArrayList<MenuDataModel> menuDataModelArrayList = new ArrayList<>();
    ArrayList<CartData> cartDataArrayList = new ArrayList<>();

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
        showNumBill = root.findViewById(R.id.showNumBill);
        totalShowBill = root.findViewById(R.id.totalShowBill);
        checkBill = root.findViewById(R.id.checkBill);
        viewCart = root.findViewById(R.id.viewCart);
        menuBar.setOnClickListener(view -> {
            DrawerLayout drawer = container.getRootView().findViewById(R.id.drawer_layout);
            drawer.open();
        });

        checkNewMonthData();
        goToAddMenu();
        getShowMenu();
        setClickChooseType();
        setShowChooseType();
        getBillRunCur();
        setClickPay();
        return root;
    }

    private void openPaySale(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_check_money);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ImageView ex_dialog = dialog.findViewById(R.id.ex_dialog);
        dialog.show();
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int height=display.getHeight();
        dialog.getWindow().setLayout((6*width)/7,(4*height)/5);

        ex_dialog.setOnClickListener(view1 -> dialog.dismiss());
    }

    private void setClickPay() {
        checkBill.setOnClickListener(view -> {
            if(totalBill==0){
                Toast.makeText(getContext(), "กรุณาเลือกเมนู", Toast.LENGTH_SHORT).show();
            }else{
                if(typeBill==1){
                    openPaySale();
                }else{
                    openPayDelivery();
                }
            }
        });
    }

    private void openPayDelivery(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_check_money_g_and_f);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ImageView ex_dialog_g_and_f = dialog.findViewById(R.id.ex_dialog_g_and_f);
        dialog.show();
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int height=display.getHeight();
        dialog.getWindow().setLayout((6*width)/7,(4*height)/5);

        ex_dialog_g_and_f.setOnClickListener(view1 -> dialog.dismiss());
    }

    private void setShowOrder() {
        viewCart.setLayoutManager(new LinearLayoutManager(getContext()));
        CartAdaptor cartAdaptor = new CartAdaptor(getContext(), cartDataArrayList, (position, status) -> {
            if(status.equals("del")){
                cartDataArrayList.remove(position);
                setShowOrder();
            }
        });
        viewCart.setAdapter(cartAdaptor);
        totalBill =0;
        if(cartDataArrayList.size()==0){
            totalBill =0;
            String totalStr = String.format(Locale.ENGLISH,"%.2f", totalBill);
            totalShowBill.setText(totalStr);
        }else{
            for(CartData cartData:cartDataArrayList){
                totalBill += cartData.getTotal();
                String totalStr = String.format(Locale.ENGLISH,"%.2f", totalBill);
                totalShowBill.setText(totalStr);
            }
        }


    }

    private void getBillRunCur() {
        String dateCur = dateTool.getDayCur();
        db.collection("daily_sale")
                .whereEqualTo("dateStr", dateCur)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        if(task.getResult().size()==0){
                            showNumBill.setText("ยังไม่ได้เปิดรอบการขาย");
                        }else{
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                keyDailySale = document.getId();
                                int billCur = Integer.parseInt(document.getData().get("billRun").toString());
                                showNumBill.setText(String.valueOf(billCur));
                                billNo = billCur;
                            }
                        }

                    } else {
                        Log.d("CHK_DB", "Error getting documents: ", task.getException());
                        showNumBill.setText("ยังไม่ได้เปิดรอบการขาย");
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void setShowChooseType() {
        if (typeBill == 1) {
            chooseTypeBill.setText("ขายหน้าร้าน");
            chooseTypeBill.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_shop_svgrepo_com, 0);
        } else if (typeBill == 2) {
            chooseTypeBill.setText("Grab");
            chooseTypeBill.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_grab_logo_svgrepo_com, 0);
        } else if (typeBill == 3) {
            chooseTypeBill.setText("Food Panda");
            chooseTypeBill.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_foodpanda_svgrepo_com, 0);
        } else if (typeBill == 4) {
            chooseTypeBill.setText("Robin Hood");
            chooseTypeBill.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_rbh_logo_01_e1635235514424, 0);
        }
        setChangePriceType();
    }

    private void setChangePriceType() {
        int pos = 0;
        for(CartData cartDataGet : cartDataArrayList){

            for (MenuDataModel menuDataModel:menuDataModelArrayList){
                if(menuDataModel.getKey().equals(cartDataGet.getKeyMenu())){
                    float newPrice=0;
                    float newPriceTotalTops=0;
                    if (typeBill == 1) {
                        if(cartDataGet.getTypePrice()==1){
                            newPrice = menuDataModel.getPriceNm();
                        }else{
                            newPrice = menuDataModel.getPriceSp();
                        }
                    } else if (typeBill == 2) {
                        if(cartDataGet.getTypePrice()==1){
                            newPrice = menuDataModel.getPriceNmGrab();
                        }else{
                            newPrice = menuDataModel.getPriceSpGrab();
                        }
                    } else if (typeBill == 3) {
                        if(cartDataGet.getTypePrice()==1){
                            newPrice = menuDataModel.getPriceNmFood();
                        }else{
                            newPrice = menuDataModel.getPriceSpFood();
                        }
                    } else if (typeBill == 4) {
                        if(cartDataGet.getTypePrice()==1){
                            newPrice = menuDataModel.getPriceNmRobin();
                        }else{
                            newPrice = menuDataModel.getPriceSpRobin();
                        }

                    }
                    Map<String, Object> topMapGetMenu = (Map<String, Object>) menuDataModel.getListTopping();
                    Map<String, Object> topMap = (Map<String, Object>) cartDataGet.getTopping();
                    Map<String, Object> newTops = new HashMap<>();
                    if(topMap.size()>0){
                        for (Map.Entry<String, Object> entry : topMap.entrySet()) {
                            ToppingChooseModel toppingChooseModelGet = (ToppingChooseModel) entry.getValue();
                            for (Map.Entry<String, Object> entryHave : topMapGetMenu.entrySet()) {
                                Map<String, Object> value = (Map<String, Object>) entryHave.getValue();
                                if(entryHave.getKey().equals(toppingChooseModelGet.getKey())){
                                    float totalNewTop = 0;
                                    float priceNewTop = 0;
                                    if (typeBill == 1) {
                                        priceNewTop = Float.parseFloat(value.get("priceTopping").toString());
                                        totalNewTop = Float.parseFloat(value.get("priceTopping").toString())*toppingChooseModelGet.getNum();
                                        newPrice += Float.parseFloat(value.get("priceTopping").toString())*toppingChooseModelGet.getNum();
                                    } else if (typeBill == 2) {
                                        priceNewTop = Float.parseFloat(value.get("priceToppingGrab").toString());
                                        totalNewTop = Float.parseFloat(value.get("priceToppingGrab").toString())*toppingChooseModelGet.getNum();
                                        newPrice += Float.parseFloat(value.get("priceToppingGrab").toString())*toppingChooseModelGet.getNum();
                                    } else if (typeBill == 3) {
                                        priceNewTop = Float.parseFloat(value.get("priceToppingFoodPanda").toString());
                                        totalNewTop = Float.parseFloat(value.get("priceToppingFoodPanda").toString())*toppingChooseModelGet.getNum();
                                        newPrice += Float.parseFloat(value.get("priceToppingFoodPanda").toString())*toppingChooseModelGet.getNum();
                                    } else if (typeBill == 4) {
                                        priceNewTop = Float.parseFloat(value.get("priceToppingRobinHood").toString());
                                        totalNewTop = Float.parseFloat(value.get("priceToppingRobinHood").toString())*toppingChooseModelGet.getNum();
                                        newPrice += Float.parseFloat(value.get("priceToppingRobinHood").toString())*toppingChooseModelGet.getNum();
                                    }
                                    newPriceTotalTops += priceNewTop;
                                    String uidNew = UUID.randomUUID().toString();
                                    ToppingChooseModel toppingChooseModelNew = new ToppingChooseModel();
                                    toppingChooseModelNew.setKey(entryHave.getKey());
                                    toppingChooseModelNew.setToppingName(toppingChooseModelGet.getToppingName());
                                    toppingChooseModelNew.setNum(toppingChooseModelGet.getNum());
                                    toppingChooseModelNew.setToppingTotal(totalNewTop);
                                    toppingChooseModelNew.setToppingPrice(priceNewTop);
                                    newTops.put(uidNew,toppingChooseModelNew);
                                }
                            }
                        }
                    }
                    cartDataArrayList.get(pos).setTopping(newTops);
                    cartDataArrayList.get(pos).setPrice(newPrice);
                    cartDataArrayList.get(pos).setTotal(newPrice);
                    cartDataArrayList.get(pos).setToppingTotal(newPriceTotalTops);
                }
            }

            pos++;
        }
        setShowOrder();

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

    private void setShowBtnSale() {
        if (isOpenSale) {
            openSale.setVisibility(View.GONE);
            closeSale.setVisibility(View.VISIBLE);
        } else {
            openSale.setVisibility(View.VISIBLE);
            closeSale.setVisibility(View.GONE);
        }
    }

    private void getShowMenu() {
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
                        HashMap<String, Object> getTp = (HashMap<String, Object>) value.get("listTopping");
                        menuData.setListTopping(getTp);
                        menuDataModelArrayList.add(menuData);
                    }
                    viewMenu.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
                    viewMenu.setLayoutManager(mLayoutManager);
                    MenuAdaptor menuAdaptor = new MenuAdaptor(getContext(), menuDataModelArrayList, this::openDialogConfirmMenu);
                    viewMenu.setAdapter(menuAdaptor);

                }
            } else {
                Log.d("CHK_DB", "get failed with ", task.getException());
            }
        });

    }

    private void openDialogConfirmMenu(int position) {
        if(isOpenSale){
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_click_menu);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            ImageView ex_dialog_click_menu = dialog.findViewById(R.id.ex_dialog_click_menu);
            dialog.show();
            getContext();
            Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            dialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 5);

            MenuDataModel menuDataModel = menuDataModelArrayList.get(position);

            TextView showNameMenu = dialog.findViewById(R.id.showNameMenu);
            showNameMenu.setText(menuDataModel.getMenuName());

            ex_dialog_click_menu.setOnClickListener(view -> dialog.dismiss());
            AtomicInteger numMenu = new AtomicInteger(1);
            AtomicInteger posPrice = new AtomicInteger(1);
            TextView chooseSp = dialog.findViewById(R.id.chooseSp);
            TextView chooseNm = dialog.findViewById(R.id.chooseNm);
            AtomicReference<Float> priceMenu = new AtomicReference<>((float) 0);

            if(typeBill==1){
                priceMenu.set(menuDataModel.getPriceNm());
            }else if(typeBill==2){
                priceMenu.set(menuDataModel.getPriceNmGrab());
            }else if(typeBill==3){
                priceMenu.set(menuDataModel.getPriceNmFood());
            }else if(typeBill==4){
                priceMenu.set(menuDataModel.getPriceNmRobin());
            }

            chooseNm.setOnClickListener(view -> {
                posPrice.set(1);
                chooseSp.setBackgroundResource(R.drawable.b_non_click_menu);
                chooseSp.setTextColor(getResources().getColor(R.color.grey));
                chooseNm.setBackgroundResource(R.drawable.b_click_menu_path);
                chooseNm.setTextColor(getResources().getColor(R.color.white));
                if(typeBill==1){
                    priceMenu.set(menuDataModel.getPriceNm());
                }else if(typeBill==2){
                    priceMenu.set(menuDataModel.getPriceNmGrab());
                }else if(typeBill==3){
                    priceMenu.set(menuDataModel.getPriceNmFood());
                }else if(typeBill==4){
                    priceMenu.set(menuDataModel.getPriceNmRobin());
                }

            });
            chooseSp.setOnClickListener(view -> {
                posPrice.set(2);
                chooseNm.setBackgroundResource(R.drawable.b_non_click_menu);
                chooseNm.setTextColor(getResources().getColor(R.color.grey));
                chooseSp.setBackgroundResource(R.drawable.b_click_menu_path);
                chooseSp.setTextColor(getResources().getColor(R.color.white));
                if(typeBill==1){
                    priceMenu.set(menuDataModel.getPriceSp());
                }else if(typeBill==2){
                    priceMenu.set(menuDataModel.getPriceSpGrab());
                }else if(typeBill==3){
                    priceMenu.set(menuDataModel.getPriceSpFood());
                }else if(typeBill==4){
                    priceMenu.set(menuDataModel.getPriceSpRobin());
                }
            });

            ImageView plusNum = dialog.findViewById(R.id.plusNum);
            ImageView delNum = dialog.findViewById(R.id.delNum);
            EditText numShow = dialog.findViewById(R.id.numShow);
            EditText note = dialog.findViewById(R.id.note);
            LinearLayout confirmAdd = dialog.findViewById(R.id.confirmAdd);
            numShow.setSelection(numShow.getText().length());

            plusNum.setOnClickListener(view -> {
                numMenu.getAndIncrement();
                numShow.setText(String.valueOf(numMenu.get()));
                numShow.setSelection(numShow.getText().length());
            });

            delNum.setOnClickListener(view -> {
                if(numMenu.get()==0){
                    Toast.makeText(dialog.getContext(), "ไม่สามารถลดจำนวนเกินกว่านี้ได้", Toast.LENGTH_SHORT).show();
                }else{
                    numMenu.getAndDecrement();
                    numShow.setText(String.valueOf(numMenu.get()));
                    numShow.setSelection(numShow.getText().length());
                }
            });

            numShow.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().isEmpty()) {
                        numMenu.set(0);
                    }else{
                        int newVal = Integer.parseInt(charSequence.toString());
                        if(newVal==0){
                            numMenu.set(1);
                            numShow.setText(String.valueOf(numMenu.get()));
                            numShow.setSelection(numShow.getText().length());
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            ArrayList<ToppingChooseModel> toppingChooseModelArrayList = new ArrayList<>();

            Map<String, Object> topMap = (Map<String, Object>) menuDataModel.getListTopping();
            for (Map.Entry<String, Object> entry : topMap.entrySet()) {
                Map<String, Object> value = (Map<String, Object>) entry.getValue();
                ToppingChooseModel toppingChooseModel = new ToppingChooseModel();
                toppingChooseModel.setKey(entry.getKey());
                toppingChooseModel.setToppingName(value.get("nameTopping").toString());
                if(typeBill==1){
                    toppingChooseModel.setToppingPrice(Float.parseFloat(value.get("priceTopping").toString()));
                }else if(typeBill==2){
                    toppingChooseModel.setToppingPrice(Float.parseFloat(value.get("priceToppingGrab").toString()));
                }else if(typeBill==3){
                    toppingChooseModel.setToppingPrice(Float.parseFloat(value.get("priceToppingFoodPanda").toString()));
                }else if(typeBill==4){
                    toppingChooseModel.setToppingPrice(Float.parseFloat(value.get("priceToppingRobinHood").toString()));
                }
                toppingChooseModel.setNum(0);
                toppingChooseModel.setToppingTotal(0);
                toppingChooseModelArrayList.add(toppingChooseModel);
            }

            RecyclerView viewTopping = dialog.findViewById(R.id.viewTopping);
            viewTopping.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
            viewTopping.setLayoutManager(mLayoutManager);
            ToppingChooseAdaptor toppingChooseAdaptor = new ToppingChooseAdaptor(getContext(), toppingChooseModelArrayList, position1 -> {

            });
            viewTopping.setAdapter(toppingChooseAdaptor);

            confirmAdd.setOnClickListener(view -> {
                if(numShow.getText().toString().isEmpty()||numShow.getText().toString().equals("0")){
                    Toast.makeText(dialog.getContext(), "กรุณากรอกจำนวนให้ถูกต้อง", Toast.LENGTH_SHORT).show();
                }else{
                    // GET TOPPING //
                    ArrayList<String> getArr = toppingChooseAdaptor.getCheckList();
                    List<ToppingChooseModel> getTpp = toppingChooseAdaptor.getTppList();
                    Set<String> sortsTp = new LinkedHashSet<>(getArr);
                    List<ToppingChooseModel> toppingNew = new ArrayList<>();
                    float topPrice = 0;

                    for (ToppingChooseModel tpp : getTpp) {
                        for(String chooseTp : sortsTp){
                            if(tpp.getKey().equals(chooseTp)){
                                toppingNew.add(tpp);
                                topPrice += tpp.getToppingPrice()*tpp.getNum();
                                break;
                            }
                        }
                    }

                    Log.d("CHK_CHOOSE_TPP",toppingNew.toString());
                    // END GET TOPPING //
                    float total = Integer.parseInt(numShow.getText().toString())* priceMenu.get();

                    final Map<String, Object> newListTpp = new HashMap<>();

                    for (ToppingChooseModel tppChoose : toppingNew) {
                        String uid = UUID.randomUUID().toString();
                        Log.d("CHK_CHK_TPP",tppChoose.getToppingName());
                        newListTpp.put(uid, tppChoose);
                    }

                    float totalMenu = (priceMenu.get()*Integer.parseInt(numShow.getText().toString()))+topPrice;
                    float priceMenuOne = priceMenu.get()+topPrice;
                    Date curDate = Calendar.getInstance().getTime();

                    CartData cartData = new CartData();
                    cartData.setNameMenu(menuDataModel.getMenuName());
                    cartData.setNum(Integer.parseInt(numShow.getText().toString()));
                    cartData.setPrice(priceMenuOne);
                    cartData.setTotal(totalMenu);
                    cartData.setTypePrice(typeBill);
                    cartData.setToppingTotal(topPrice);
                    cartData.setNote(note.getText().toString());
                    cartData.setTopping(newListTpp);
                    cartData.setDateCreate(curDate);
                    cartData.setTypePrice(posPrice.get());
                    cartData.setKeyMenu(menuDataModel.getKey());
                    cartDataArrayList.add(cartData);
                    setShowOrder();
                    dialog.dismiss();

                }
            });
        }else{
            Toast.makeText(getContext(), "กรุณาเปิดรอบการขายวันนี้ก่อน!", Toast.LENGTH_SHORT).show();
        }
    }



    private void goToAddMenu() {
        add_menu_home.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), SaveMenuActivity.class);
            startActivityForResult(intent, LAUNCH_SAVE_MENU);
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
                    } else {
                        Map<String, Object> value = (Map<String, Object>) ListDayMap.get(dateCur);
                        if (value == null) {
                            openDialogOpenSale();
                        } else {
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

    private void openDialogOpenSale() {
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
            intent.putExtra("keyMonthData", keyMonthData);
            startActivityForResult(intent, LAUNCH_OPEN_SALE);
        });
        ex_dialog_add_topping.setOnClickListener(view -> dialog.dismiss());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("CHK_RES", "requestCode : " + requestCode + " resultCode: " + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LAUNCH_OPEN_SALE) {
                isOpenSale = true;
                setShowBtnSale();
                getBillRunCur();
            } else if (requestCode == LAUNCH_SAVE_MENU) {
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