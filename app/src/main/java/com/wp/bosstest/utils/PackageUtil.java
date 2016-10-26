package com.wp.bosstest.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** 这个单例模式是线程不安全的，在多线程时，将不能正常工作，待优化中
 * Created by cadi on 2016/8/12.
 */
public class PackageUtil {
    private PackageManager packageManager;
    private Context context;
    private static PackageUtil packageUtil;
    public static final String UiPackageName = "com.android.providers.downloads.ui";
    public static final String DpPackageName = "com.android.providers.downloads";
    public static final String FileExplorerPackageName = "com.android.fileexplorer";

    private PackageUtil(Context context) {
        super();
        this.context = context;
        init();
    }

    private void init() {
        packageManager = context.getPackageManager();
    }

    public static synchronized PackageUtil getInstance(Context context) {
        if (packageUtil == null) {
            packageUtil = new PackageUtil(context);
        }
        return packageUtil;
    }

    public String getPackageMessages(PackageInfo info) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String uiLastTime = dateFormat.format(new Date(info.lastUpdateTime));
        sb.append("1.)应用名称 :" + info.applicationInfo.loadLabel(packageManager).toString() + "\n");
        sb.append("2.)版本号 :" + info.versionName + "\n");
        sb.append("3.)VersionCode :" + info.versionCode + "\n");
        sb.append("4.)包名 :" + info.packageName + "\n");
        sb.append("5.)最后安装时间 :" + uiLastTime + "\n");
        sb.append("6.)进程名称 :" + info.applicationInfo.processName + "\n");
        sb.append("7.)本地数据路径 :" + info.applicationInfo.dataDir + "\n");
        sb.append("8.)安装apk路径 :" + info.applicationInfo.publicSourceDir + "\n");
        sb.append("9.)Application类名: " + info.applicationInfo.className + "\n");
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
