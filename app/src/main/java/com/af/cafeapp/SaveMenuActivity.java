package com.af.cafeapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.af.cafeapp.adaptor.ToppingAdaptor;
import com.af.cafeapp.models.MenuData;
import com.af.cafeapp.models.ToppingMenuData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class SaveMenuActivity extends AppCompatActivity {
    LinearLayout back;
    LinearLayout addTopping,choosePic;
    ArrayList<ToppingMenuData> toppingMenuDataArrayList = new ArrayList<>();
    RecyclerView viewTopping;
    RadioGroup radioGroupTypeTop;
    int posChoose = 1;
    final static int LAUNCH_CHOOSE_IMG = 1;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText priceMenu1,priceMenu2;
    String priceNm = "50";
    String priceSp = "60";
    String priceNmGrab = "70";
    String priceSpGrab = "80";
    String priceNmFood = "90";
    String priceSpFood = "110";
    String priceNmRobin = "120";
    String priceSpRobin = "130";
    int statusChooseImg =0;
    Uri selectedImageUri = null;
    ImageView imgShow;
    EditText menuName;
    LinearLayout save;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = storage.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_menu);
        getSupportActionBar().hide();
        back = findViewById(R.id.back);
        addTopping = findViewById(R.id.add_topping);
        viewTopping = findViewById(R.id.viewTopping);
        radioGroupTypeTop = findViewById(R.id.typeTop);
        priceMenu1 = findViewById(R.id.priceMenu1);
        priceMenu2 = findViewById(R.id.priceMenu2);
        choosePic = findViewById(R.id.choosePic);
        imgShow = findViewById(R.id.imgShow);
        menuName = findViewById(R.id.menuName);
        save = findViewById(R.id.save);

        addTopping.setOnClickListener(view -> openDialogAddTp());

        back.setOnClickListener(view -> finish());

        setOnChoose();
        setOnChangePrice();
        setChoseImgClick();
        checkPermit();

        save.setOnClickListener(view -> setSave());
    }

    private void setSave() {
        if(priceNm.isEmpty()||priceSp.isEmpty()){
            Toast.makeText(this, "กรุณากรอกราคาหน้าร้านให้ครบถ้วน", Toast.LENGTH_SHORT).show();
        }else if(priceNmGrab.isEmpty()||priceSpGrab.isEmpty()){
            Toast.makeText(this, "กรุณากรอกราคา Grab ให้ครบถ้วน", Toast.LENGTH_SHORT).show();
        }else if(priceNmFood.isEmpty()||priceSpFood.isEmpty()){
            Toast.makeText(this, "กรุณากรอกราคา Food Panda ให้ครบถ้วน", Toast.LENGTH_SHORT).show();
        }else if(priceNmRobin.isEmpty()||priceSpRobin.isEmpty()){
            Toast.makeText(this, "กรุณากรอกราคา Robin Hood ให้ครบถ้วน", Toast.LENGTH_SHORT).show();
        }else if(statusChooseImg==0){
            Toast.makeText(this, "กรุณาเลือกรูปภาพ", Toast.LENGTH_SHORT).show();
        }else{
            setSaveDb();
        }
    }

    private void setSaveDb() {
        MenuData menuData = new MenuData();
        menuData.setMenuName(menuName.getText().toString());
        menuData.setPriceNm(Float.parseFloat(priceNm));
        menuData.setPriceSp(Float.parseFloat(priceSp));
        menuData.setPriceNmGrab(Float.parseFloat(priceNmGrab));
        menuData.setPriceSpGrab(Float.parseFloat(priceSpGrab));
        menuData.setPriceNmFood(Float.parseFloat(priceNmFood));
        menuData.setPriceSpFood(Float.parseFloat(priceSpFood));
        menuData.setPriceNmRobin(Float.parseFloat(priceNmRobin));
        menuData.setPriceSpRobin(Float.parseFloat(priceSpRobin));

        final HashMap<String, Object> newList = new HashMap<>();

        for (ToppingMenuData tpp : toppingMenuDataArrayList) {
            String uid = UUID.randomUUID().toString();
            newList.put(uid, tpp);
        }

        menuData.setListTopping(newList);

        String menuNewId = UUID.randomUUID().toString();
        db.collection("menu").document("menu-all").update("listMenu."+menuNewId,menuData).addOnSuccessListener(aVoid -> {

            StorageReference ref = storageReference.child("picMenu/" + menuNewId);
            ref.putFile(selectedImageUri)
                    .addOnSuccessListener(
                            taskSnapshot -> finish())
                    .addOnFailureListener(e -> Toast.makeText(SaveMenuActivity.this, "เกิดข้อผิดผลาดโปรดลองอีกครั้ง", Toast.LENGTH_SHORT).show());

        });

    }

    private void checkPermit(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("CHK_PER","Permission is granted");
            }
        }
    }

    private void setChoseImgClick() {
        choosePic.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "เลือกรูปภาพ"), LAUNCH_CHOOSE_IMG);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == LAUNCH_CHOOSE_IMG) {
                if (resultCode == Activity.RESULT_OK) {
                    selectedImageUri = data.getData();
                    statusChooseImg = 1;
                    imgShow.setImageURI(selectedImageUri);
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    statusChooseImg = 0;
                }
            }
        } catch (Exception e) {
            statusChooseImg = 0;
            Log.e("CHK_IMG", "Exception in onActivityResult : " + e.getMessage());
        }
    }

    private void setOnChangePrice(){
        priceMenu1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(posChoose==1){
                    priceNm = editable.toString();
                    priceMenu1.setSelection(priceMenu1.getText().length());
                }else if(posChoose==2){
                    priceNmGrab = editable.toString();
                    priceMenu1.setSelection(priceMenu1.getText().length());
                }else if(posChoose==3){
                    priceNmFood = editable.toString();
                    priceMenu1.setSelection(priceMenu1.getText().length());
                }else if(posChoose==4){
                    priceNmRobin = editable.toString();
                    priceMenu1.setSelection(priceMenu1.getText().length());
                }
            }
        });
        priceMenu2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(posChoose==1){
                    priceSp = editable.toString();
                    priceMenu2.setSelection(priceMenu2.getText().length());
                }else if(posChoose==2){
                    priceSpGrab = editable.toString();
                    priceMenu2.setSelection(priceMenu2.getText().length());
                }else if(posChoose==3){
                    priceSpFood = editable.toString();
                    priceMenu2.setSelection(priceMenu2.getText().length());
                }else if(posChoose==4){
                    priceSpRobin = editable.toString();
                    priceMenu2.setSelection(priceMenu2.getText().length());
                }
            }
        });
    }

    private void setShowPrice(){
        if(posChoose==1){
            priceMenu1.setText(priceNm);
            priceMenu2.setText(priceSp);
        }else if(posChoose==2){
            priceMenu1.setText(priceNmGrab);
            priceMenu2.setText(priceSpGrab);
        }else if(posChoose==3){
            priceMenu1.setText(priceNmFood);
            priceMenu2.setText(priceSpFood);
        }else if(posChoose==4){
            priceMenu1.setText(priceNmRobin);
            priceMenu2.setText(priceSpRobin);
        }
    }

    private void setOnChoose() {
        radioGroupTypeTop.setOnCheckedChangeListener((radioGroup, i) -> {
            if(i==R.id.sale){
                posChoose =1;
                setShowTopping();
                setShowPrice();
                Log.d("CHK_RDO","sale");
            }else if(i==R.id.grab){
                posChoose =2;
                setShowTopping();
                setShowPrice();
                Log.d("CHK_RDO","grab");
            }else if(i==R.id.food){
                posChoose =3;
                setShowTopping();
                setShowPrice();
                Log.d("CHK_RDO","food");
            }else if(i==R.id.robin){
                posChoose =4;
                setShowTopping();
                setShowPrice();
                Log.d("CHK_RDO","robin");
            }
        });
    }

    private void setShowTopping(){
        viewTopping.setLayoutManager(new LinearLayoutManager(this));
        ToppingAdaptor toppingAdaptor = new ToppingAdaptor(this, toppingMenuDataArrayList,posChoose, position -> {
            toppingMenuDataArrayList.remove(position);
            setShowTopping();
        });
        viewTopping.setAdapter(toppingAdaptor);
    }

    private void openDialogAddTp() {
        final Dialog dialog = new Dialog(SaveMenuActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_add_topping);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ImageView ex_dialog_add_topping = dialog.findViewById(R.id.ex_dialog_add_topping);
        dialog.show();

        EditText nameTopping,priceTopping,priceToppingGrab,priceToppingFoodPanda,priceToppingRobinHood;
        nameTopping = dialog.findViewById(R.id.nameTopping);
        priceTopping = dialog.findViewById(R.id.priceTopping);
        priceToppingGrab = dialog.findViewById(R.id.priceToppingGrab);
        priceToppingFoodPanda = dialog.findViewById(R.id.priceToppingFoodPanda);
        priceToppingRobinHood = dialog.findViewById(R.id.priceToppingRobinHood);

        LinearLayout confirmAdd = dialog.findViewById(R.id.confirmAdd);
        confirmAdd.setOnClickListener(view -> {
            if(nameTopping.getText().toString().isEmpty()|| priceTopping.getText().toString().isEmpty()|| priceToppingGrab.getText().toString().isEmpty()||
                    priceToppingFoodPanda.getText().toString().isEmpty()|| priceToppingRobinHood.getText().toString().isEmpty()
            ){
                Toast.makeText(SaveMenuActivity.this, "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
            }else {
                ToppingMenuData toppingMenuData = new ToppingMenuData();
                toppingMenuData.setNameTopping(nameTopping.getText().toString());
                toppingMenuData.setPriceTopping(Float.parseFloat(priceTopping.getText().toString()));
                toppingMenuData.setPriceToppingGrab(Float.parseFloat(priceToppingGrab.getText().toString()));
                toppingMenuData.setPriceToppingFoodPanda(Float.parseFloat(priceToppingFoodPanda.getText().toString()));
                toppingMenuData.setPriceToppingRobinHood(Float.parseFloat(priceToppingRobinHood.getText().toString()));
                toppingMenuDataArrayList.add(toppingMenuData);
                setShowTopping();
                dialog.dismiss();
            }
        });

        ex_dialog_add_topping.setOnClickListener(view -> dialog.dismiss());
    }

}