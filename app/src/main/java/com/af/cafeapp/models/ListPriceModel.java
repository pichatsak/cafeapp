package com.af.cafeapp.models;

public class ListPriceModel {
    private String key;
    private int pos;
    private float price_list_food;
    private float price_list_grab;
    private float price_list_robin;
    private float price_normal;
    private String price_list_name;

    public ListPriceModel() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public float getPrice_list_food() {
        return price_list_food;
    }

    public void setPrice_list_food(float price_list_food) {
        this.price_list_food = price_list_food;
    }

    public float getPrice_list_grab() {
        return price_list_grab;
    }

    public void setPrice_list_grab(float price_list_grab) {
        this.price_list_grab = price_list_grab;
    }

    public float getPrice_list_robin() {
        return price_list_robin;
    }

    public void setPrice_list_robin(float price_list_robin) {
        this.price_list_robin = price_list_robin;
    }

    public float getPrice_normal() {
        return price_normal;
    }

    public void setPrice_normal(float price_normal) {
        this.price_normal = price_normal;
    }

    public String getPrice_list_name() {
        return price_list_name;
    }

    public void setPrice_list_name(String price_list_name) {
        this.price_list_name = price_list_name;
    }
}
