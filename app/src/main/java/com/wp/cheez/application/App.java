package com.wp.cheez.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tencent.bugly.Bugly;
import com.wp.cheez.utils.LogHelper;

/**
 * Created by cadi on 2016/10/9.
 */

public class App extends Application {
    private static Context mContext;
    private static final String TAG = LogHelper.makeTag(App.class);
    public static final String APP_ID = "9a9876dedd";

    public static Context getAppContext(){
        return mContext;
    }

    //类变量在实例方法里初始化，我真够low的，就是要一low到底
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "App ___ onCreate()");
//        Bugly.init(getApplicationContext(), APP_ID, true);
        mContext = this;
    }

}
