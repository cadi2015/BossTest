package com.wp.bosstest.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by cadi on 2016/6/8.
 */
public class AppInfo {
    private static final String TAG = "AppInfo";

    AppInfo() {
        super();
    }

    public static int getVersionCode(Context context) {

        if (context == null) {
            return 0;
        }

        PackageManager pm = context.getPackageManager();
        int versionCode = 0;
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "versionCode = " + versionCode);
        return versionCode;
    }
}
