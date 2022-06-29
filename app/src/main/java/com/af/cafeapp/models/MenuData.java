package com.af.cafeapp.models;

import java.util.HashMap;

public class MenuData {
    private String menuName;
    private float priceNm ;
    private float priceSp ;
    private float priceNmGrab ;
    private float priceSpGrab ;
    private float priceNmFood ;
    private float priceSpFood ;
    private float priceNmRobin ;
    private float priceSpRobin ;
    private HashMap<String, Object> listTopping;

    public MenuData() {

    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public float getPriceNm() {
        return priceNm;
    }

    public void setPriceNm(float priceNm) {
        this.priceNm = priceNm;
    }

    public float getPriceSp() {
        return priceSp;
    }

    public void setPriceSp(float priceSp) {
        this.priceSp = priceSp;
    }

    public float getPriceNmGrab() {
        return priceNmGrab;
    }

    public void setPriceNmGrab(float priceNmGrab) {
        this.priceNmGrab = priceNmGrab;
    }

    public float getPriceSpGrab() {
        return priceSpGrab;
    }

    public void setPriceSpGrab(float priceSpGrab) {
        this.priceSpGrab = priceSpGrab;
    }

    public float getPriceNmFood() {
        return priceNmFood;
    }

    public void setPriceNmFood(float priceNmFood) {
        this.priceNmFood = priceNmFood;
    }

    public float getPriceSpFood() {
        return priceSpFood;
    }

    public void setPriceSpFood(float priceSpFood) {
        this.priceSpFood = priceSpFood;
    }

    public float getPriceNmRobin() {
        return priceNmRobin;
    }

    public void setPriceNmRobin(float priceNmRobin) {
        this.priceNmRobin = priceNmRobin;
    }

    public float getPriceSpRobin() {
        return priceSpRobin;
    }

    public void setPriceSpRobin(float priceSpRobin) {
        this.priceSpRobin = priceSpRobin;
    }

    public HashMap<String, Object> getListTopping() {
        return listTopping;
    }

    public void setListTopping(HashMap<String, Object> listTopping) {
        this.listTopping = listTopping;
    }
}
