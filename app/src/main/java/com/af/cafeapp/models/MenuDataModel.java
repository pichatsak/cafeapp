package com.af.cafeapp.models;

import java.util.HashMap;

public class MenuDataModel {
    private String key;
    private String menuName;
    private float price_food ;
    private float price_grab ;
    private float price_normal ;
    private float price_robin ;
    private String  statusTopp ;
    private String typeMenu ;
    private HashMap<String, Object> listTopping;
    private HashMap<String, Object> listPrice;

    public MenuDataModel() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public float getPrice_food() {
        return price_food;
    }

    public void setPrice_food(float price_food) {
        this.price_food = price_food;
    }

    public float getPrice_grab() {
        return price_grab;
    }

    public void setPrice_grab(float price_grab) {
        this.price_grab = price_grab;
    }

    public float getPrice_normal() {
        return price_normal;
    }

    public void setPrice_normal(float price_normal) {
        this.price_normal = price_normal;
    }

    public float getPrice_robin() {
        return price_robin;
    }

    public void setPrice_robin(float price_robin) {
        this.price_robin = price_robin;
    }

    public String getStatusTopp() {
        return statusTopp;
    }

    public void setStatusTopp(String statusTopp) {
        this.statusTopp = statusTopp;
    }

    public String getTypeMenu() {
        return typeMenu;
    }

    public void setTypeMenu(String typeMenu) {
        this.typeMenu = typeMenu;
    }

    public HashMap<String, Object> getListTopping() {
        return listTopping;
    }

    public void setListTopping(HashMap<String, Object> listTopping) {
        this.listTopping = listTopping;
    }

    public HashMap<String, Object> getListPrice() {
        return listPrice;
    }

    public void setListPrice(HashMap<String, Object> listPrice) {
        this.listPrice = listPrice;
    }
}
