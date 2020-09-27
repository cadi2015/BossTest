package com.wp.cheez.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.widget.Toast;

import com.wp.cheez.application.App;
import com.wp.cheez.utils.LogHelper;
import com.wp.cheez.utils.PackageUtil;
import com.wp.cheez.utils.ShellUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cadi on 2016/10/28.
 */

public class UninstallAppService extends IntentService {
    private static final String TAG = LogHelper.makeTag(UninstallAppService.class);
    private int mUninstallAppCount = 0;

    public UninstallAppService() {
        this("UninstallAppService");
    }

    public UninstallAppService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent(Intent intent)");
        Log.d(TAG, "current Thread = " + Thread.currentThread().getName());
        mUninstallAppCount = uninstallApps();
    }

    /**
     * root手机可用
     * @return 返回卸载了多少个应用，这她妈的是我写的吗？
     */
    private int uninstallApps() {
        List<ApplicationInfo> apps = PackageUtil.getInstance(App.getAppContext()).getAllApplication();
        int temp = 0;
        List<String> commandList = new ArrayList<>();
        for (ApplicationInfo applicationInfo : apps) {
            if (((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) || ((applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) > 0)
                    || applicationInfo.processName.equals("com.wp.bosstest") || applicationInfo.processName.equals("cn.kuaipan.android")
                    || applicationInfo.processName.equals("com.xunlei.downloadprovider") || applicationInfo.processName.equals("com.tencent.mm")
                    || applicationInfo.processName.equals("com.tencent.mobileqq")) {
                continue; //不卸载系统App、不卸载我们迅雷的应用
            }
            String command = "pm uninstall " + applicationInfo.processName;
            commandList.add(command);
            Log.d(TAG, "appInfo " + temp++ + " : " + "packageName = " + applicationInfo.packageName + ", processName = " + applicationInfo.processName);
        }
        ShellUtils.execCommand(commandList, true); //最后调用shell命令删除App
        return commandList.size();
    }

    /**
     * Service销毁前，发出一个广播
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent();
        intent.setAction("bossTest.intent.action.UNINSTALL_FINISH");
        if (mUninstallAppCount > 0) {
            Toast.makeText(this, "成功卸载" + mUninstallAppCount + "个App", Toast.LENGTH_LONG).show();
            intent.putExtra("isSuccess", true);
        } else {
            Toast.makeText(this, "没有需要卸载的App", Toast.LENGTH_LONG).show();
            intent.putExtra("isSuccess", false);
        }
        sendBroadcast(intent);
    }
}
