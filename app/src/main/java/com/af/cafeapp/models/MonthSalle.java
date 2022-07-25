package com.af.cafeapp.models;

import java.util.HashMap;

public class MonthSalle {
    private int month;
    private int year;
    private HashMap<String, Object> listDaySale;

    public MonthSalle() {

    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public HashMap<String, Object> getListDaySale() {
        return listDaySale;
    }

    public void setListDaySale(HashMap<String, Object> listDaySale) {
        this.listDaySale = listDaySale;
    }
}
