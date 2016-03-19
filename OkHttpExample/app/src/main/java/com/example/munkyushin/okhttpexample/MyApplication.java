package com.example.munkyushin.okhttpexample;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by MunkyuShin on 3/19/16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
