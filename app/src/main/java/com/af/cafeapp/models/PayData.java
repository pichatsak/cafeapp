package com.af.cafeapp.models;

public class PayData {
    private float totalPay;
    private String typePay;
    private int typePayPos;

    public PayData() {

    }

    public float getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(float totalPay) {
        this.totalPay = totalPay;
    }

    public String getTypePay() {
        return typePay;
    }

    public void setTypePay(String typePay) {
        this.typePay = typePay;
    }

    public int getTypePayPos() {
        return typePayPos;
    }

    public void setTypePayPos(int typePayPos) {
        this.typePayPos = typePayPos;
    }
}
