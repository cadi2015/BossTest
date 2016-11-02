package com.wp.bosstest.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.DataOutputStream;

/**
 * Created by cadi on 2016/10/12.
 */

public class PermissionUtil {
    private static final String TAG = LogHelper.makeTag(PermissionUtil.class);
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 200;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
    };


    /**
     * 8      * Checks if the app has permission to write to device storage
     * 9      *
     * 10      * If the app does not has permission then the user will be prompted to
     * 11      * grant permissions
     * 12      *
     * 13      * @param activity
     * 14
     */

    @TargetApi(23)
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permissionWrite = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionRead = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionMount = ActivityCompat.checkSelfPermission(activity, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
        Log.d(TAG, "permissionWrite = " + permissionWrite);
        Log.d(TAG, "permissionRead = " + permissionRead);
        Log.d(TAG, "permissionMount = " + permissionMount);
        if (permissionWrite != PackageManager.PERMISSION_GRANTED || permissionRead != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE);
            activity.requestPermissions(PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }


    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限) 该静态方法已经失效了,默认就返回true，醉了，不过让操作系统弹出Root权限对话框，挺好的
     *
     * @return 应用程序是/否获取Root权限
     */
    public static boolean upgradeRootPermission(String pkgCodePath) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "chmod 777 " + pkgCodePath;
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }

}
