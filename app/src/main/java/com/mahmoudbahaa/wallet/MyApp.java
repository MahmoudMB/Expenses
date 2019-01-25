package com.mahmoudbahaa.expenses;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by MahmoudBahaa on 17/01/2019.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}