package com.wp.cheez.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.wp.cheez.R;
import com.wp.cheez.application.App;

import java.io.File;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.text.TextUtils.isEmpty;

public class DeviceUtil {
    private static DisplayMetrics dm;
    private static final int MCC_CHINA = 460;

    /**
     * 获取Android Device Id
     *
     * @param context
     * @return
     */
    public static String getAndroidDeviceId(Context context) {
        String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return ANDROID_ID;
    }

    /**
     * 获取屏幕宽度，单位像素
     *
     * @param activity
     * @return
     */
    public static int getScreenWidthPx(Activity activity) {
        return displayMetrics(activity).widthPixels;
    }

    /**
     * 获取屏幕高度，单位：像素
     *
     * @param activity
     * @return
     */
    public static int getScreenHeightPx(Activity activity) {
        return displayMetrics(activity).heightPixels;
    }

    /**
     * 获取屏幕密度
     *
     * @param activity
     * @return
     */
    public static float getScreenDensity(Activity activity) {
        return displayMetrics(activity).density;
    }

    /**
     * 获取屏幕dpi
     *
     * @param activity
     * @return
     */
    public static int getScreenDensityDpi(Activity activity) {
        return displayMetrics(activity).densityDpi;
    }

    public static float getScreenScaledDensity(Activity activity) {
        return displayMetrics(activity).scaledDensity;
    }

    public static float getScreenXdpi(Activity activity) {
        return displayMetrics(activity).xdpi;
    }

    public static float getScreenYdpi(Activity activity) {
        return displayMetrics(activity).ydpi;
    }

    public static String getImei(Activity activity) {
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "没有权限";
        }
        return tm.getDeviceId();
    }

    private static DisplayMetrics displayMetrics(Activity activity) {
        if (dm == null) {
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
        if (!isEmpty(wifiMac)) {
        }
        return wifiMac;
    }

    public static String getSimId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        String SimSerialNumber = tm.getSimSerialNumber();
//        return SimSerialNumber;
        return "";
    }

    /**
     * 返回mcc
     */
    public static String getMccByTM(Activity activity, boolean isFull) {
        if (activity == null) {
            return "";
        }
        if (ActivityCompat.checkSelfPermission(App.getAppContext(),
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_PHONE,
                    REQUEST_PHONE_STATE);
        } else {
            TelephonyManager tm = (TelephonyManager) App.getAppContext().
                    getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null && tm.getSubscriberId() != null && tm.getSubscriberId().length() > 3) {
                String mcc;
                if (isFull) {
                    mcc = tm.getSubscriberId();
                } else {
                    mcc = tm.getSubscriberId().substring(0, 3);
                }
                return mcc;
            }
        }
        return "";
    }

    private static final String[] PERMISSIONS_PHONE = {Manifest.permission.READ_PHONE_STATE};

    private static final int REQUEST_PHONE_STATE = 13;

    public static String getSerialNumber(Context context) {
        return android.os.Build.SERIAL;
    }


    // Get internal (data partition) free space
    // This will match what's shown in System Settings > Storage for
    // Internal Space, when you subtract Total - Used
    public static long getFreeInternalMemory() {
        return getFreeMemory(Environment.getDataDirectory());
    }

    // Get external (SDCARD) free space  unit - > byte
    public static long getFreeExternalMemory() {
        return getFreeMemory(Environment.getExternalStorageDirectory());
    }

    // Get Android OS (system partition) free space
    public static long getFreeSystemMemory() {
        return getFreeMemory(Environment.getRootDirectory());
    }

    // Get free space for provided path
    // Note that this will throw IllegalArgumentException for invalid paths
    public static long getFreeMemory(File path) {
        StatFs stats = new StatFs(path.getAbsolutePath());
        return (long) stats.getAvailableBlocks() * stats.getBlockSize();
    }


    public static String spaceSizeWithUnit(Context context, long byteSize) {
        long size;
        String unit;
        if (byteSize <= 1024) {
            size = byteSize;
            unit = "BYTE";
        } else if (byteSize / 1024 <= 1024) {
            size = byteSize / 1024;
            unit = "KB";
        } else if (byteSize / 1024 / 1024 <= 1024) {
            size = byteSize / 1024 / 1024;
            unit = "MB";
        } else {
            size = byteSize / 1024 / 1024 / 1024;
            unit = "GB";
        }
        return context.getString(R.string.free_space_size_with_unit, size, unit);
    }

}
