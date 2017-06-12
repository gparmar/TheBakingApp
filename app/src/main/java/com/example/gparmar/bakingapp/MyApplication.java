package com.example.gparmar.bakingapp;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by gparmar on 08/06/17.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
