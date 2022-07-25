package com.af.cafeapp.models;

import java.util.Date;

public class ExpenseHisData {
    private String key;
    private float total;
    private String nameExpense;
    private int posCreate;
    private String posCreateMore;
    private Date dateCreate;

    public ExpenseHisData() {

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

    public String getNameExpense() {
        return nameExpense;
    }

    public void setNameExpense(String nameExpense) {
        this.nameExpense = nameExpense;
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
