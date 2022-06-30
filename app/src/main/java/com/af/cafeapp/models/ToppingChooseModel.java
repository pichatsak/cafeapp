package com.af.cafeapp.models;

public class ToppingChooseModel {
    private String key;
    private String toppingName;
    private float toppingPrice;
    private float toppingTotal;
    private int num;

    public ToppingChooseModel() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getToppingName() {
        return toppingName;
    }

    public void setToppingName(String toppingName) {
        this.toppingName = toppingName;
    }

    public float getToppingPrice() {
        return toppingPrice;
    }

    public void setToppingPrice(float toppingPrice) {
        this.toppingPrice = toppingPrice;
    }

    public float getToppingTotal() {
        return toppingTotal;
    }

    public void setToppingTotal(float toppingTotal) {
        this.toppingTotal = toppingTotal;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
