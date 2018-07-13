package com.wp.cheez.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.wp.cheez.utils.LogHelper;

/**
 * Created by cadi on 2016/10/9.
 */

public class App extends Application {
    private static Context mContext;
    private static final String TAG = LogHelper.makeTag(App.class);
    public static final String APP_ID = "f4d4e86687";

    public static Context getAppContext(){
        return mContext;
    }

    //类变量在实例方法里初始化，我真够low的
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "App ___ onCreate()");
        mContext = this;
    }

}
