package com.wp.cheez.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 这个单例模式是线程不安全的，在多线程时，将不能正常工作，待优化中 , 2017年3月6日已经优化
 * Created by cadi on 2016/8/12.
 */
public class PackageUtil {
    private PackageManager packageManager;
    private Context mContext;
    private static PackageUtil packageUtil;
    public static final String UiPackageName = "com.android.providers.downloads.ui";
    public static final String DpPackageName = "com.android.providers.downloads";
    public static final String SHORT_VIDEO_PACKAGE_NAME = "com.cmcm.shorts";
    public static final String CRUSH_PACKAGE_NAME = "com.cmcm.crush";

    private PackageUtil(Context context) {
        super();
        this.mContext = context;
        init();
    }

    private void init() {
        packageManager = mContext.getPackageManager();
    }

    /**
     * 使用双重校验锁，高效无比
     *
     * @param context
     * @return
     */
    public static PackageUtil getInstance(Context context) {

        if (context == null) {
            return null;
        }

        if (packageUtil == null) { //这里可能多个线程对象进来
            synchronized (PackageUtil.class) { //第一个线程对象先拿到Class对象锁(因为是静态方法，所以Class对象锁，而不用实例对象锁,开始执行，第二个线程对象等待第一个线程对象释放锁
                if (packageUtil == null) { //如果不做==null判断，会创建两次PackageUtil对象,我做了判断，第二个线程对象就不会再new了
                    packageUtil = new PackageUtil(context);
                }
            }
        }
        return packageUtil;
    }

    public String getPackageMessages(PackageInfo info) {
        if (info == null) {
            return null;
        }
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

    public PackageInfo getPackageInfoDefault(String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            System.err.println("名字找不到，我就草了");
        }
        return packageInfo;
    }

    public List<ApplicationInfo> getAllApplication() {
        List<ApplicationInfo> allApplications = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        return allApplications;
    }

    public String getAppName(String packageName) {
        PackageInfo info = getPackageInfoDefault(packageName);
        if(info != null){
            String appName = info.applicationInfo.loadLabel(packageManager).toString();
            return appName;
        }
        return "Not Installed";
    }

    public Drawable getAppIcon(String packageName) {
        PackageInfo info = getPackageInfoDefault(packageName); //要是用户没装apk，packageInfo就是null啊
        if (info != null) {
            return info.applicationInfo.loadIcon(packageManager);
        }
        return null;
    }

}
