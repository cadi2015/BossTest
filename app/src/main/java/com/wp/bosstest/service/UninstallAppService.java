package com.wp.bosstest.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.widget.Toast;

import com.wp.bosstest.application.App;
import com.wp.bosstest.utils.LogHelper;
import com.wp.bosstest.utils.PackageUtil;
import com.wp.bosstest.utils.ShellUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cadi on 2016/10/28.
 */

public class UninstallAppService extends IntentService {
    private static final String TAG = LogHelper.makeTag(UninstallAppService.class);

    public UninstallAppService(){
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
        uninstallApps();
    }

    private void uninstallApps(){
        List<ApplicationInfo> apps = PackageUtil.getInstance(App.getAppContext()).getAllApplication();
        int temp = 0;
        List<String> commandList = new ArrayList();
        for(ApplicationInfo applicationInfo : apps) {
            if(((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM ) > 0) || ((applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) > 0)
                    || applicationInfo.processName.equals("com.wp.bosstest") || applicationInfo.processName.equals("cn.kuaipan.android")
                    || applicationInfo.processName.equals("com.xunlei.downloadprovider") || applicationInfo.processName.equals("com.tencent.mm")
                    || applicationInfo.processName.equals("com.tencent.mobileqq")){
                continue;
            }
            String command = "pm uninstall " + applicationInfo.processName;
            commandList.add(command);
            Log.d(TAG, "appInfo " + temp++  + " : " + "packageName = " + applicationInfo.packageName + ", processName = " + applicationInfo.processName);
        }
        ShellUtils.execCommand(commandList, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "卸载全部完成", Toast.LENGTH_SHORT).show();
    }
}
