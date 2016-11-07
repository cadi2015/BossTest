package com.wp.bosstest.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tencent.bugly.Bugly;
import com.wp.bosstest.utils.LogHelper;
import com.wp.bosstest.utils.PermissionUtil;

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

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "App ___ onCreate()");
        mContext = this;
    }

}
