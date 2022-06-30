package com.af.cafeapp.models;

import java.util.Date;

public class DailyData {
    private Date dateCreate;
    private Date dateDaily;
    private Date dateEnd;
    private String dateStr;
    private String nameUser;
    private float totalDrawerStart;
    private float totalSale;
    private float totalChange;
    private float totalExpends;
    private float totalTakeOut;
    private float totalAmount;
    private String status;

    public DailyData() {

    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateDaily() {
        return dateDaily;
    }

    public void setDateDaily(Date dateDaily) {
        this.dateDaily = dateDaily;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public float getTotalDrawerStart() {
        return totalDrawerStart;
    }

    public void setTotalDrawerStart(float totalDrawerStart) {
        this.totalDrawerStart = totalDrawerStart;
    }

    public float getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(float totalSale) {
        this.totalSale = totalSale;
    }

    public float getTotalChange() {
        return totalChange;
    }

    public void setTotalChange(float totalChange) {
        this.totalChange = totalChange;
    }

    public float getTotalExpends() {
        return totalExpends;
    }

    public void setTotalExpends(float totalExpends) {
        this.totalExpends = totalExpends;
    }

    public float getTotalTakeOut() {
        return totalTakeOut;
    }

    public void setTotalTakeOut(float totalTakeOut) {
        this.totalTakeOut = totalTakeOut;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
