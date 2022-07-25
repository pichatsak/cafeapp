package com.af.cafeapp.models;

import java.util.HashMap;

public class ListDayExpense {
    private String id;
    private String date;
    private HashMap<String, Object> listExpense;

    public ListDayExpense() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public HashMap<String, Object> getListExpense() {
        return listExpense;
    }

    public void setListExpense(HashMap<String, Object> listExpense) {
        this.listExpense = listExpense;
    }
}
