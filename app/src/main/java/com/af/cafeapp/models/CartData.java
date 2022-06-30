package com.af.cafeapp.models;

import java.util.Date;
import java.util.Map;

public class CartData {
    private String nameMenu;
    private String keyMenu;
    private int num;
    private float price;
    private float total;
    private float toppingTotal;
    private String note;
    private int typePrice;
    private Map<String, Object> topping;
    private Date dateCreate;

    public CartData() {

    }

    public String getKeyMenu() {
        return keyMenu;
    }

    public void setKeyMenu(String keyMenu) {
        this.keyMenu = keyMenu;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getNameMenu() {
        return nameMenu;
    }

    public void setNameMenu(String nameMenu) {
        this.nameMenu = nameMenu;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getToppingTotal() {
        return toppingTotal;
    }

    public void setToppingTotal(float toppingTotal) {
        this.toppingTotal = toppingTotal;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getTypePrice() {
        return typePrice;
    }

    public void setTypePrice(int typePrice) {
        this.typePrice = typePrice;
    }

    public Map<String, Object> getTopping() {
        return topping;
    }

    public void setTopping(Map<String, Object> topping) {
        this.topping = topping;
    }
}
