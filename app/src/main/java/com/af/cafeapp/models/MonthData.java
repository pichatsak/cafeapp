package com.af.cafeapp.models;

import java.util.ArrayList;
import java.util.HashMap;

public class MonthData {
    private int month;
    private int year;
    private HashMap<String, Object> listData;

    public MonthData() {

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

    public HashMap<String, Object> getListData() {
        return listData;
    }

    public void setListData(HashMap<String, Object> listData) {
        this.listData = listData;
    }
}
