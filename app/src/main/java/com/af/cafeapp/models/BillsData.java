package com.af.cafeapp.models;

import java.util.Date;
import java.util.HashMap;

public class BillsData {
    private Date dateCreate;
    private HashMap<String, Object> listMenu;
    private int noBill;
    private float totalAmount;
    private float totalTopping;
    private float total;
    private float totalPay;
    private float totalChange;
    private HashMap<String, Object> listPay;
    private String typeBill;

    public BillsData() {

    }

    public float getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(float totalPay) {
        this.totalPay = totalPay;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public HashMap<String, Object> getListMenu() {
        return listMenu;
    }

    public void setListMenu(HashMap<String, Object> listMenu) {
        this.listMenu = listMenu;
    }

    public int getNoBill() {
        return noBill;
    }

    public void setNoBill(int noBill) {
        this.noBill = noBill;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public float getTotalTopping() {
        return totalTopping;
    }

    public void setTotalTopping(float totalTopping) {
        this.totalTopping = totalTopping;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getTotalChange() {
        return totalChange;
    }

    public void setTotalChange(float totalChange) {
        this.totalChange = totalChange;
    }

    public HashMap<String, Object> getListPay() {
        return listPay;
    }

    public void setListPay(HashMap<String, Object> listPay) {
        this.listPay = listPay;
    }

    public String getTypeBill() {
        return typeBill;
    }

    public void setTypeBill(String typeBill) {
        this.typeBill = typeBill;
    }
}
