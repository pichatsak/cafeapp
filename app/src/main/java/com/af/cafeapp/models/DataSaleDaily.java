package com.af.cafeapp.models;

import java.util.Date;
import java.util.HashMap;

public class DataSaleDaily {
    private Date dateData;
    private String dateStr;
    private int billRun;
    private HashMap<String, Object> listBill;

    public DataSaleDaily() {

    }

    public int getBillRun() {
        return billRun;
    }

    public void setBillRun(int billRun) {
        this.billRun = billRun;
    }

    public Date getDateData() {
        return dateData;
    }

    public void setDateData(Date dateData) {
        this.dateData = dateData;
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
}
