package com.os.operando.myandroidtemplate;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

import timber.log.Timber;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidThreeTen.init(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
