package com.wp.bosstest.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.wp.bosstest.utils.LogHelper;

/**
 * Created by cadi on 2016/10/9.
 */

public class App extends Application {
    private static Context mContext;
    private static final String TAG = LogHelper.makeTag(App.class);

    public static Context getAppContext(){
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "App ___ onCreate()");
        mContext = this;
    }

}
