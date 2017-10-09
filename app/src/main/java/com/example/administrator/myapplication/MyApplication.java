package com.example.administrator.myapplication;

import android.app.Application;

/**
 * Created by Jorgejie on 2017/9/21.
 */

public class MyApplication extends Application {

    private static MyApplication mMyApplication;

    public static MyApplication getContext() {
        return mMyApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMyApplication = this;
    }
}
