package com.af.cafeapp.models;

import java.util.HashMap;

public class ExpenseData {
    private int month;
    private int year;
    private HashMap<String, Object> listDayExpense;

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

    public HashMap<String, Object> getListDayExpense() {
        return listDayExpense;
    }

    public void setListDayExpense(HashMap<String, Object> listDayExpense) {
        this.listDayExpense = listDayExpense;
    }
}

