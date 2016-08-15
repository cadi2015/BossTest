package com.wp.bosstest.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cadi on 2016/8/12.
 */
public class PackageUtil {
    private PackageManager packageManager;
    private Context context;
    private static PackageUtil packageUtil;

    private PackageUtil(Context context) {
        super();
        this.context = context;
        init();
    }

    private void init() {
        packageManager = context.getPackageManager();
    }

    public static PackageUtil getInstance(Context context) {
        if (packageUtil == null) {
            packageUtil = new PackageUtil(context);
        }
        return packageUtil;
    }

    public String getPackageMessages(PackageInfo info) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String uiLastTime = dateFormat.format(new Date(info.lastUpdateTime));
        sb.append("应用名称 :" + info.applicationInfo.loadLabel(packageManager).toString() + "\n");
        sb.append("ui版本号 :" + info.versionName + "\n");
        sb.append("versionCode :" + info.versionCode + "\n");
        sb.append("包名 :" + info.packageName + "\n");
        sb.append("最后安装时间 :" + uiLastTime + "\n");
        sb.append("进程名称 :" + info.applicationInfo.processName + "\n");
        sb.append("本地数据路径 :" + info.applicationInfo.dataDir + "\n");
        sb.append("安装apk路径 :" + info.applicationInfo.publicSourceDir + "\n");
        return sb.toString();
    }


    public PackageManager getPackageManager() {
        return packageManager;
    }

    public PackageInfo getPackageInfoDefault(String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            return packageInfo;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            System.err.println("名字找不到，我就草了");
        }
        return null;
    }

}
