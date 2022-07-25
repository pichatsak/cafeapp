package com.af.cafeapp.models;

import java.util.HashMap;

public class StockHisData {
    private int month;
    private int year;
    private HashMap<String, Object> listDayStock;

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

}
