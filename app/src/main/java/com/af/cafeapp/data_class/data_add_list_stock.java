package com.af.cafeapp.data_class;

public class data_add_list_stock {
    private String add_name_stock;
    private int add_num_stock;
    private String add_unit_stock;
    private String id;

    public int getAdd_num_stock() {
        return add_num_stock;
    }

    public void setAdd_num_stock(int add_num_stock) {
        this.add_num_stock = add_num_stock;
    }

    public data_add_list_stock() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdd_name_stock() {
        return add_name_stock;
    }

    public void setAdd_name_stock(String add_name_stock) {
        this.add_name_stock = add_name_stock;
    }



    public String getAdd_unit_stock() {
        return add_unit_stock;
    }

    public void setAdd_unit_stock(String add_unit_stock) {
        this.add_unit_stock = add_unit_stock;
    }


}
