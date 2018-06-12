package com.wp.cheez.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import static android.text.TextUtils.isEmpty;

public class DeviceUtil {
    private static DisplayMetrics dm;

    public static String getAndroidDeviceId (Context context) {
        String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        return ANDROID_ID;
    }

    public static int getScreenWidthPx(Activity activity){
        int width = displayMetrics(activity).widthPixels;
        return width;
    }

    public static int getScreenHeightPx(Activity activity){
        int height = displayMetrics(activity).heightPixels;
        return height;
    }

    public static float getScreenDensity(Activity activity){
        return displayMetrics(activity).density;
    }

    public static int getScreenDensityDpi(Activity activity){
        return displayMetrics(activity).densityDpi;
    }

    public static float getScreenScaledDensity(Activity activity){
        return displayMetrics(activity).scaledDensity;
    }

    public static float getScreenXdpi(Activity activity){
        return displayMetrics(activity).xdpi;
    }

    public static float getScreenYdpi(Activity activity){
        return displayMetrics(activity).ydpi;
    }

    public static String getImei(Activity activity){
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE ) != PackageManager.PERMISSION_GRANTED){
            return "没有权限";
        }
        return tm.getDeviceId();
    }

    private static DisplayMetrics displayMetrics(Activity activity){
        if(dm == null){
            dm = new DisplayMetrics();
        }
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static String getMac(Context context) {
        //wifi mac地址
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String wifiMac = info.getMacAddress();
        if(!isEmpty(wifiMac)){
        }
        return wifiMac;
    }

    public static String getSimId (Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//        String SimSerialNumber = tm.getSimSerialNumber();
//        return SimSerialNumber;
        return "";
    }

    public static String getSerialNumber(Context context) {
        String SerialNumber = android.os.Build.SERIAL;
        return SerialNumber;
    }

}
