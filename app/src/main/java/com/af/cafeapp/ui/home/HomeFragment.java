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
import com.af.cafeapp.adaptor.ListPriceAdaptor;
import com.af.cafeapp.adaptor.MenuAdaptor;
import com.af.cafeapp.adaptor.ToppingChooseAdaptor;
import com.af.cafeapp.databinding.FragmentHomeBinding;
import com.af.cafeapp.models.BillsData;
import com.af.cafeapp.models.CartData;
import com.af.cafeapp.models.ListPriceModel;
import com.af.cafeapp.models.MenuDataModel;
import com.af.cafeapp.models.MonthData;
import com.af.cafeapp.models.PayData;
import com.af.cafeapp.models.ToppingChooseModel;
import com.af.cafeapp.tools.DateTool;
import com.af.cafeapp.widget.DialogPay;
import com.af.cafeapp.widget.DialogPayDeli;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    private TextView menuBar, showNumBill;
    boolean isOpenSale = false;
    private final DateTool dateTool = new DateTool();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String keyMonthData = "";
    final static int LAUNCH_OPEN_SALE = 1;
    final static int LAUNCH_SAVE_MENU = 2;
    LinearLayout add_menu_home, openSale, closeSale, checkBill, openBill;
    RecyclerView viewMenu, viewCart;
    TextView chooseTypeBill;
    int typeBill = 1;
    String keyDailySale = "";
    TextView totalShowBill;
    int billNo = 0;
    boolean isOpenBill = false;
    float totalBill = 0;
    float totalPay = 0;
    float totalPayed = 0;
    float totalChange = 0;
    ArrayList<MenuDataModel> menuDataModelArrayList = new ArrayList<>();
    ArrayList<CartData> cartDataArrayList = new ArrayList<>();
    ArrayList<PayData> payDataArrayList = new ArrayList<>();
    DialogPay dialogPay;
    DialogPayDeli dialogPayDeli;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        add_menu_home = root.findViewById(R.id.add_menu_home);
        menuBar = root.findViewById(R.id.menuBar);
        openBill = root.findViewById(R.id.openBill);
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
        setClickOpenBill();
        checkNewMonthData();
        goToAddMenu();
        getShowMenu();
        setClickChooseType();
        setShowChooseType();
        getBillRunCur();
        setClickPay();
        setShowBtn();
        setBtnCloseSale();
        return root;
    }

    private void setBtnCloseSale() {
        closeSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new
            }
        });
    }

    private void setClickOpenBill() {
        openBill.setOnClickListener(view -> {
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
                isOpenBill = true;
                dialog.dismiss();
                setShowChooseType();
            });
            chooseGrab.setOnClickListener(view12 -> {
                typeBill = 2;
                isOpenBill = true;
                dialog.dismiss();
                setShowChooseType();
            });
            chooseFood.setOnClickListener(view12 -> {
                typeBill = 3;
                isOpenBill = true;
                dialog.dismiss();
                setShowChooseType();
            });
            chooseRobin.setOnClickListener(view12 -> {
                typeBill = 4;
                isOpenBill = true;
                dialog.dismiss();
                setShowChooseType();
            });
        });
    }

    private void setShowBtn() {
        if (isOpenBill) {
            openBill.setVisibility(View.GONE);
            checkBill.setVisibility(View.VISIBLE);
        } else {
            openBill.setVisibility(View.VISIBLE);
            checkBill.setVisibility(View.GONE);
        }
    }

    private void setNewBill() {
        cartDataArrayList = new ArrayList<>();
        typeBill = 1;
        billNo = 0;
        totalBill = 0;
        totalPay = 0;
        totalPayed = 0;
        totalChange = 0;
        getBillRunCur();
        setShowChooseType();
    }

    private void openPaySale() {
        dialogPay = new DialogPay(getContext(), totalBill, payDataArrayListGet -> {
            payDataArrayList = payDataArrayListGet;
            setCompleteBill();
        });
    }

    private void setCompleteBill() {

        Date curDate = Calendar.getInstance().getTime();
        BillsData billsData = new BillsData();
        billsData.setNoBill(billNo);
        billsData.setDateCreate(curDate);
        billsData.setTotal(totalBill);
        billsData.setTotalAmount(totalBill);
        billsData.setTotalTopping(0);

        HashMap<String, Object> mapAddMenu = new HashMap<>();
        for (CartData cartDataAdd : cartDataArrayList) {
            String uidAdd = UUID.randomUUID().toString();
            mapAddMenu.put(uidAdd, cartDataAdd);
        }
        billsData.setListMenu(mapAddMenu);

        HashMap<String, Object> mapAddPay = new HashMap<>();
        float totalPayIn = 0;
        for (PayData payDataAdd : payDataArrayList) {
            String uidAdd = UUID.randomUUID().toString();
            mapAddPay.put(uidAdd, payDataAdd);
            totalPayIn += payDataAdd.getTotalPay();
        }
        billsData.setTotalPay(totalPayIn);
        float totalChangeIn = totalPayIn - totalBill;
        billsData.setTotalChange(totalChangeIn);
        billsData.setListPay(mapAddPay);
        billsData.setTypeBill(String.valueOf(typeBill));
        db.collection("daily_sale").document(keyDailySale)
                .update("listBill." + billNo, billsData, "billRun", billNo + 1)
                .addOnSuccessListener(aVoid -> {
                    billNo++;
                    openDialogSuccess(billsData);
                    isOpenBill = false;
                    setShowBtn();
                });
    }

    private void openDialogSuccess(BillsData billsDataOld) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_success_sale);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ImageView ex_dialog_g_and_f = dialog.findViewById(R.id.ex_dialog_add_topping);
        dialog.show();
        LinearLayout okBtn = dialog.findViewById(R.id.okBtn);
        TextView tvTotalSc = dialog.findViewById(R.id.tvTotalSc);
        TextView tvTotalPay = dialog.findViewById(R.id.tvTotalPay);
        TextView tvTotalChange = dialog.findViewById(R.id.tvTotalChange);

        tvTotalSc.setText(String.format(Locale.ENGLISH, "%.2f", billsDataOld.getTotal()));
        tvTotalPay.setText(String.format(Locale.ENGLISH, "%.2f", billsDataOld.getTotalPay()));
        tvTotalChange.setText(String.format(Locale.ENGLISH, "%.2f", billsDataOld.getTotalChange()));

        okBtn.setOnClickListener(view -> dialog.dismiss());

        ex_dialog_g_and_f.setOnClickListener(view1 -> dialog.dismiss());

        setNewBill();
    }


    private void setClickPay() {
        checkBill.setOnClickListener(view -> {
            if (totalBill == 0) {
                Toast.makeText(getContext(), "กรุณาเลือกเมนู", Toast.LENGTH_SHORT).show();
            } else {
                if (typeBill == 1) {
                    openPaySale();
                } else {
                    openPayDelivery();
                }
            }
        });
    }

    private void openPayDelivery() {
        dialogPayDeli = new DialogPayDeli(getContext(), totalBill, typeBill, payDataArrayListGet -> {
            payDataArrayList = payDataArrayListGet;
            setCompleteBill();
        });
    }

    private void setShowOrder() {
        viewCart.setLayoutManager(new LinearLayoutManager(getContext()));
        CartAdaptor cartAdaptor = new CartAdaptor(getContext(), cartDataArrayList, (position, status) -> {
            if (status.equals("del")) {
                cartDataArrayList.remove(position);
                setShowOrder();
            }
        });
        viewCart.setAdapter(cartAdaptor);
        totalBill = 0;
        if (cartDataArrayList.size() == 0) {
            totalBill = 0;
            String totalStr = String.format(Locale.ENGLISH, "%.2f", totalBill);
            totalShowBill.setText(totalStr);
        } else {
            for (CartData cartData : cartDataArrayList) {
                totalBill += cartData.getTotal();
                String totalStr = String.format(Locale.ENGLISH, "%.2f", totalBill);
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
                        if (task.getResult().size() == 0) {
                            showNumBill.setText("ยังไม่ได้เปิดรอบการขาย");
                        } else {
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

        setShowBtn();
        setChangePriceType();
    }

    private void setChangePriceType() {
        int pos = 0;
        for (CartData cartDataGet : cartDataArrayList) {

            for (MenuDataModel menuDataModel : menuDataModelArrayList) {
                if (menuDataModel.getKey().equals(cartDataGet.getKeyMenu())) {
                    float newPrice = 0;
                    float newPriceTotalTops = 0;
//                    if (typeBill == 1) {
//                        if(cartDataGet.getTypePrice()==1){
//                            newPrice = menuDataModel.getPriceNm();
//                        }else{
//                            newPrice = menuDataModel.getPriceSp();
//                        }
//                    } else if (typeBill == 2) {
//                        if(cartDataGet.getTypePrice()==1){
//                            newPrice = menuDataModel.getPriceNmGrab();
//                        }else{
//                            newPrice = menuDataModel.getPriceSpGrab();
//                        }
//                    } else if (typeBill == 3) {
//                        if(cartDataGet.getTypePrice()==1){
//                            newPrice = menuDataModel.getPriceNmFood();
//                        }else{
//                            newPrice = menuDataModel.getPriceSpFood();
//                        }
//                    } else if (typeBill == 4) {
//                        if(cartDataGet.getTypePrice()==1){
//                            newPrice = menuDataModel.getPriceNmRobin();
//                        }else{
//                            newPrice = menuDataModel.getPriceSpRobin();
//                        }
//
//                    }
                    if (menuDataModel.getTypeMenu().equals("one")) {
                        if (typeBill == 1) {
                            newPrice = menuDataModel.getPrice_normal();
                        } else if (typeBill == 2) {
                            newPrice = menuDataModel.getPrice_grab();
                        } else if (typeBill == 3) {
                            newPrice = menuDataModel.getPrice_food();
                        } else if (typeBill == 4) {
                            newPrice = menuDataModel.getPrice_robin();
                        }
                    } else {
                        Map<String, Object> priceListMap = menuDataModel.getListPrice();
                        for (Map.Entry<String, Object> entryHave : priceListMap.entrySet()) {
                            Map<String, Object> value = (Map<String, Object>) entryHave.getValue();
                            Log.d("CHK_MENU_MANY","loop first");
                            if (entryHave.getKey().equals(cartDataGet.getKeyPrice())) {
                                if (typeBill == 1) {
                                    newPrice = Float.parseFloat(value.get("price_normal").toString());
                                } else if (typeBill == 2) {
                                    newPrice = Float.parseFloat(value.get("price_list_grab").toString());
                                } else if (typeBill == 3) {
                                    newPrice = Float.parseFloat(value.get("price_list_food").toString());
                                } else if (typeBill == 4) {
                                    newPrice = Float.parseFloat(value.get("price_list_robin").toString());
                                }
                            }
                        }
                    }

                    Map<String, Object> newTops = new HashMap<>();
                    if (menuDataModel.getStatusTopp().equals("yes")) {
                        Map<String, Object> topMapGetMenu = menuDataModel.getListTopping();
                        Map<String, Object> topMap = cartDataGet.getTopping();
                        if (topMap.size() > 0) {
                            for (Map.Entry<String, Object> entry : topMap.entrySet()) {
                                ToppingChooseModel toppingChooseModelGet = (ToppingChooseModel) entry.getValue();
                                for (Map.Entry<String, Object> entryHave : topMapGetMenu.entrySet()) {
                                    Map<String, Object> value = (Map<String, Object>) entryHave.getValue();
                                    if (entryHave.getKey().equals(toppingChooseModelGet.getKey())) {
                                        float totalNewTop = 0;
                                        float priceNewTop = 0;
                                        if (typeBill == 1) {
                                            priceNewTop = Float.parseFloat(value.get("priceTopping").toString());
                                            totalNewTop = Float.parseFloat(value.get("priceTopping").toString()) * toppingChooseModelGet.getNum();
                                            newPrice += Float.parseFloat(value.get("priceTopping").toString()) * toppingChooseModelGet.getNum();
                                        } else if (typeBill == 2) {
                                            priceNewTop = Float.parseFloat(value.get("priceToppingGrab").toString());
                                            totalNewTop = Float.parseFloat(value.get("priceToppingGrab").toString()) * toppingChooseModelGet.getNum();
                                            newPrice += Float.parseFloat(value.get("priceToppingGrab").toString()) * toppingChooseModelGet.getNum();
                                        } else if (typeBill == 3) {
                                            priceNewTop = Float.parseFloat(value.get("priceToppingFoodPanda").toString());
                                            totalNewTop = Float.parseFloat(value.get("priceToppingFoodPanda").toString()) * toppingChooseModelGet.getNum();
                                            newPrice += Float.parseFloat(value.get("priceToppingFoodPanda").toString()) * toppingChooseModelGet.getNum();
                                        } else if (typeBill == 4) {
                                            priceNewTop = Float.parseFloat(value.get("priceToppingRobinHood").toString());
                                            totalNewTop = Float.parseFloat(value.get("priceToppingRobinHood").toString()) * toppingChooseModelGet.getNum();
                                            newPrice += Float.parseFloat(value.get("priceToppingRobinHood").toString()) * toppingChooseModelGet.getNum();
                                        }
                                        newPriceTotalTops += priceNewTop;
                                        String uidNew = UUID.randomUUID().toString();
                                        ToppingChooseModel toppingChooseModelNew = new ToppingChooseModel();
                                        toppingChooseModelNew.setKey(entryHave.getKey());
                                        toppingChooseModelNew.setToppingName(toppingChooseModelGet.getToppingName());
                                        toppingChooseModelNew.setNum(toppingChooseModelGet.getNum());
                                        toppingChooseModelNew.setToppingTotal(totalNewTop);
                                        toppingChooseModelNew.setToppingPrice(priceNewTop);
                                        newTops.put(uidNew, toppingChooseModelNew);
                                    }
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
//            Log.d("CJKOP","opened");
            openSale.setVisibility(View.GONE);
            closeSale.setVisibility(View.VISIBLE);
        } else {
//            Log.d("CJKOP","not open");
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
                        menuData.setPrice_normal(Float.parseFloat(value.get("price_normal").toString()));
                        menuData.setPrice_grab(Float.parseFloat(value.get("price_grab").toString()));
                        menuData.setPrice_food(Float.parseFloat(value.get("price_food").toString()));
                        menuData.setPrice_robin(Float.parseFloat(value.get("price_robin").toString()));
                        menuData.setStatusTopp(value.get("statusTopp").toString());
                        menuData.setTypeMenu(value.get("typeMenu").toString());

                        if (value.get("statusTopp").toString().equals("yes")) {
                            HashMap<String, Object> getTp = (HashMap<String, Object>) value.get("listTopping");
                            menuData.setListTopping(getTp);
                        }

                        if (value.get("typeMenu").toString().equals("many")) {
                            HashMap<String, Object> getPriceOther = (HashMap<String, Object>) value.get("listPrice");
                            menuData.setListPrice(getPriceOther);
                        }

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
        if (isOpenSale && isOpenBill) {
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
            AtomicReference<Float> priceMenu = new AtomicReference<>((float) 0);
            AtomicReference<String> priceNameMenuMain = new AtomicReference<>("");

            ImageView plusNum = dialog.findViewById(R.id.plusNum);
            ImageView delNum = dialog.findViewById(R.id.delNum);
            EditText numShow = dialog.findViewById(R.id.numShow);
            EditText note = dialog.findViewById(R.id.note);
            LinearLayout confirmAdd = dialog.findViewById(R.id.confirmAdd);
            LinearLayout contNoTopping = dialog.findViewById(R.id.contNoTopping);
            LinearLayout contPriceList = dialog.findViewById(R.id.contPriceList);
            numShow.setSelection(numShow.getText().length());
            RecyclerView viewTopping = dialog.findViewById(R.id.viewTopping);
            RecyclerView viewPriceList = dialog.findViewById(R.id.viewPriceList);
            AtomicReference<String> keyPriceMain = new AtomicReference<>("");

            if (menuDataModel.getStatusTopp().equals("no")) {
                contNoTopping.setVisibility(View.VISIBLE);
                viewTopping.setVisibility(View.GONE);
            } else {
                contNoTopping.setVisibility(View.GONE);
                viewTopping.setVisibility(View.VISIBLE);
            }

            plusNum.setOnClickListener(view -> {
                numMenu.getAndIncrement();
                numShow.setText(String.valueOf(numMenu.get()));
                numShow.setSelection(numShow.getText().length());
            });

            delNum.setOnClickListener(view -> {
                if (numMenu.get() == 0) {
                    Toast.makeText(dialog.getContext(), "ไม่สามารถลดจำนวนเกินกว่านี้ได้", Toast.LENGTH_SHORT).show();
                } else {
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
                    } else {
                        int newVal = Integer.parseInt(charSequence.toString());
                        if (newVal == 0) {
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

            ArrayList<ListPriceModel> listPriceModelArrayList = new ArrayList<>();
            Map<String, Object> priceMap = menuDataModel.getListPrice();

            ListPriceAdaptor listPriceAdaptor = null;
            if (menuDataModel.getTypeMenu().equals("many")) {
                contPriceList.setVisibility(View.VISIBLE);
                for (Map.Entry<String, Object> entry : priceMap.entrySet()) {
                    Map<String, Object> value = (Map<String, Object>) entry.getValue();
                    ListPriceModel listPriceModel = new ListPriceModel();
                    listPriceModel.setKey(entry.getKey());
                    listPriceModel.setPos(Integer.parseInt(value.get("pos").toString()));
                    listPriceModel.setPrice_list_name(value.get("price_list_name").toString());
                    listPriceModel.setPrice_normal(Float.parseFloat(value.get("price_normal").toString()));
                    listPriceModel.setPrice_list_grab(Float.parseFloat(value.get("price_list_grab").toString()));
                    listPriceModel.setPrice_list_food(Float.parseFloat(value.get("price_list_food").toString()));
                    listPriceModel.setPrice_list_robin(Float.parseFloat(value.get("price_list_robin").toString()));
                    listPriceModelArrayList.add(listPriceModel);
                }
                Collections.sort(listPriceModelArrayList, (ListPriceModel m1, ListPriceModel m2) -> m1.getPos() - m2.getPos());
                priceNameMenuMain.set(listPriceModelArrayList.get(0).getPrice_list_name());
                keyPriceMain.set(listPriceModelArrayList.get(0).getKey());
                if (typeBill == 1) {
                    priceMenu.set(listPriceModelArrayList.get(0).getPrice_normal());
                } else if (typeBill == 2) {
                    priceMenu.set(listPriceModelArrayList.get(0).getPrice_list_grab());
                } else if (typeBill == 3) {
                    priceMenu.set(listPriceModelArrayList.get(0).getPrice_list_food());
                } else if (typeBill == 4) {
                    priceMenu.set(listPriceModelArrayList.get(0).getPrice_list_robin());
                }
                viewPriceList.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
                viewPriceList.setLayoutManager(mLayoutManager);
                listPriceAdaptor = new ListPriceAdaptor(getContext(), listPriceModelArrayList, position1 -> {
                    keyPriceMain.set(listPriceModelArrayList.get(position1).getKey());
                    priceNameMenuMain.set(listPriceModelArrayList.get(position1).getPrice_list_name());
                    if (typeBill == 1) {
                        posPrice.set(1);
                        priceMenu.set(listPriceModelArrayList.get(position1).getPrice_normal());
                    } else if (typeBill == 2) {
                        posPrice.set(2);
                        priceMenu.set(listPriceModelArrayList.get(position1).getPrice_list_grab());
                    } else if (typeBill == 3) {
                        posPrice.set(3);
                        priceMenu.set(listPriceModelArrayList.get(position1).getPrice_list_food());
                    } else if (typeBill == 4) {
                        posPrice.set(4);
                        priceMenu.set(listPriceModelArrayList.get(position1).getPrice_list_robin());
                    }
                });
                viewPriceList.setAdapter(listPriceAdaptor);
            } else {
                if (typeBill == 1) {
                    priceMenu.set(menuDataModel.getPrice_normal());
                } else if (typeBill == 2) {
                    priceMenu.set(menuDataModel.getPrice_grab());
                } else if (typeBill == 3) {
                    priceMenu.set(menuDataModel.getPrice_food());
                } else if (typeBill == 4) {
                    priceMenu.set(menuDataModel.getPrice_robin());
                }
            }

            ArrayList<ToppingChooseModel> toppingChooseModelArrayList = new ArrayList<>();
            ToppingChooseAdaptor toppingChooseAdaptor = null;
            Map<String, Object> topMap = menuDataModel.getListTopping();
            if (menuDataModel.getStatusTopp().equals("yes")) {
                for (Map.Entry<String, Object> entry : topMap.entrySet()) {
                    Map<String, Object> value = (Map<String, Object>) entry.getValue();
                    ToppingChooseModel toppingChooseModel = new ToppingChooseModel();
                    toppingChooseModel.setKey(entry.getKey());
                    toppingChooseModel.setToppingName(value.get("nameTopping").toString());
                    if (typeBill == 1) {
                        toppingChooseModel.setToppingPrice(Float.parseFloat(value.get("priceTopping").toString()));
                    } else if (typeBill == 2) {
                        toppingChooseModel.setToppingPrice(Float.parseFloat(value.get("priceToppingGrab").toString()));
                    } else if (typeBill == 3) {
                        toppingChooseModel.setToppingPrice(Float.parseFloat(value.get("priceToppingFoodPanda").toString()));
                    } else if (typeBill == 4) {
                        toppingChooseModel.setToppingPrice(Float.parseFloat(value.get("priceToppingRobinHood").toString()));
                    }
                    toppingChooseModel.setNum(0);
                    toppingChooseModel.setToppingTotal(0);
                    toppingChooseModelArrayList.add(toppingChooseModel);
                }

                viewTopping.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
                viewTopping.setLayoutManager(mLayoutManager);
                toppingChooseAdaptor = new ToppingChooseAdaptor(getContext(), toppingChooseModelArrayList, position1 -> {

                });
                viewTopping.setAdapter(toppingChooseAdaptor);
            }


            ToppingChooseAdaptor finalToppingChooseAdaptor = toppingChooseAdaptor;
            confirmAdd.setOnClickListener(view -> {
                if (numShow.getText().toString().isEmpty() || numShow.getText().toString().equals("0")) {
                    Toast.makeText(dialog.getContext(), "กรุณากรอกจำนวนให้ถูกต้อง", Toast.LENGTH_SHORT).show();
                } else {
                    // GET TOPPING //
                    float topPrice = 0;
                    final Map<String, Object> newListTpp = new HashMap<>();
                    if (menuDataModel.getStatusTopp().equals("yes")) {
                        ArrayList<String> getArr = finalToppingChooseAdaptor.getCheckList();
                        List<ToppingChooseModel> getTpp = finalToppingChooseAdaptor.getTppList();
                        Set<String> sortsTp = new LinkedHashSet<>(getArr);
                        List<ToppingChooseModel> toppingNew = new ArrayList<>();


                        for (ToppingChooseModel tpp : getTpp) {
                            for (String chooseTp : sortsTp) {
                                if (tpp.getKey().equals(chooseTp)) {
                                    toppingNew.add(tpp);
                                    topPrice += tpp.getToppingPrice() * tpp.getNum();
                                    break;
                                }
                            }
                        }

                        float total = Integer.parseInt(numShow.getText().toString()) * priceMenu.get();

                        for (ToppingChooseModel tppChoose : toppingNew) {
                            String uid = UUID.randomUUID().toString();
                            Log.d("CHK_CHK_TPP", tppChoose.getToppingName());
                            newListTpp.put(uid, tppChoose);
                        }
                    }


                    // END GET TOPPING //


                    float totalMenu = (priceMenu.get() * Integer.parseInt(numShow.getText().toString())) + topPrice;
                    float priceMenuOne = priceMenu.get() + topPrice;
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
                    cartData.setTypeMenu(menuDataModel.getTypeMenu());
                    cartData.setPriceNameMenu(priceNameMenuMain.get());
                    cartData.setKeyPrice(keyPriceMain.get());

                    cartDataArrayList.add(cartData);
                    setShowOrder();
                    dialog.dismiss();

                }
            });
        } else if (!isOpenBill) {
            Toast.makeText(getContext(), "กรุณาเปิดบิลก่อน!", Toast.LENGTH_SHORT).show();
        } else {
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
                                        setShowBtnSale();
                                        checkOpenDaily();
                                    })
                                    .addOnFailureListener(e -> Log.w("CHK_DB", "Error adding document", e));
                        } else {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                keyMonthData = document.getId();
                                setShowBtnSale();
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