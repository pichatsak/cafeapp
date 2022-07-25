package com.af.cafeapp.models;

import java.util.HashMap;

public class LogUseData {
    private int month;
    private int year;
    private HashMap<String, Object> listDayUse;

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

    public HashMap<String, Object> getListDayUse() {
        return listDayUse;
    }

    public void setListDayUse(HashMap<String, Object> listDayUse) {
        this.listDayUse = listDayUse;
    }
}
