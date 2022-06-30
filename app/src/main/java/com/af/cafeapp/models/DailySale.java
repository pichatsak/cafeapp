package com.af.cafeapp.models;

import java.util.Date;
import java.util.HashMap;

public class DailySale {
    private Date dateCreate;
    private String dateStr;
    private HashMap<String, Object> listBill;
    private int billRun;

    public DailySale() {

    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public HashMap<String, Object> getListBill() {
        return listBill;
    }

    public void setListBill(HashMap<String, Object> listBill) {
        this.listBill = listBill;
    }

    public int getBillRun() {
        return billRun;
    }

    public void setBillRun(int billRun) {
        this.billRun = billRun;
    }
}
