package com.af.cafeapp.data_class;

public class data_add_list_shop_expenses {
    private String add_name_shop_expenses;
    private int add_money_shop_expenses;
    private String id;

    public data_add_list_shop_expenses() {
    }
    public int getAdd_money_shop_expenses() {
        return add_money_shop_expenses;
    }

    public void setAdd_money_shop_expenses(int add_money_shop_expenses) {
        this.add_money_shop_expenses = add_money_shop_expenses;
    }

    public String getAdd_name_shop_expenses() {
        return add_name_shop_expenses;
    }

    public void setAdd_name_shop_expenses(String add_name_shop_expenses) {
        this.add_name_shop_expenses = add_name_shop_expenses;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
