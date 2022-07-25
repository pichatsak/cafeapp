package com.af.cafeapp;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import io.realm.Realm;

public class MyApplication extends Application {

    private static String monthDataKey="";
    @Override
    public void onCreate() {
        Realm.init(this);
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static String getMonthDataKey() {
        return monthDataKey;
    }

    public static void setMonthDataKey(String monthDataKey) {
        MyApplication.monthDataKey = monthDataKey;
    }
}
