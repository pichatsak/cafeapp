package com.af.cafeapp.models;

import java.util.Date;

public class DrawerHisData {
    private String key;
    private float total;
    private String note;
    private int posCreate;
    private String typeData;
    private String posCreateMore;
    private Date dateCreate;
    private String statusStart;

    public DrawerHisData() {

    }

    public String getStatusStart() {
        return statusStart;
    }

    public void setStatusStart(String statusStart) {
        this.statusStart = statusStart;
    }

    public String getTypeData() {
        return typeData;
    }

    public void setTypeData(String typeData) {
        this.typeData = typeData;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getPosCreate() {
        return posCreate;
    }

    public void setPosCreate(int posCreate) {
        this.posCreate = posCreate;
    }

    public String getPosCreateMore() {
        return posCreateMore;
    }

    public void setPosCreateMore(String posCreateMore) {
        this.posCreateMore = posCreateMore;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }
}
