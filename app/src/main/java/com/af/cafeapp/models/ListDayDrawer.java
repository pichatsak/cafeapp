package com.af.cafeapp.models;

import java.util.Date;
import java.util.HashMap;

public class ListDayDrawer {
    private String id;
    private String date;
    private HashMap<String, Object> listDrawer;

    public ListDayDrawer() {

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

    public HashMap<String, Object> getListDrawer() {
        return listDrawer;
    }

    public void setListDrawer(HashMap<String, Object> listDrawer) {
        this.listDrawer = listDrawer;
    }
}
