package com.wp.bosstest.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wp.bosstest.R;
import com.wp.bosstest.activity.ProcessDetailActivity;
import com.wp.bosstest.application.App;
import com.wp.bosstest.utils.LogHelper;
import com.wp.bosstest.utils.ProcessUtil;
import com.wp.bosstest.window.IconCallback;
import com.wp.bosstest.window.Magnet;


import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by cadi on 2016/10/9.
 */

public class MonitorService extends Service implements IconCallback {
    private static final String TAG = LogHelper.makeTag(MonitorService.class);
    private static final String KEY_UI_MEMORY = "uiMemory";
    private static final String KEY_DP_MEMORY = "dpMemory";
    private static final String KEY_UI_CPU = "uiCpu";
    private static final String KEY_DP_CPU = "dpCpu";
    private Magnet mMagnet;
    private boolean isMoveIconKill = false;
    private View mMagnetRootView;
    private TextView mTvUiMem;
    private TextView mTvDpMem;
    private TextView mTvUiCpu;
    private TextView mTvDpCpu;
    private Timer mTimer;
    private Handler mMainHandler;

    @Override
    public void onFlingAway() {
        Log.d(TAG, "onFlingAway()");
    }

    @Override
    public void onMove(float v, float v1) {
        Log.d(TAG, "onMovie(float v, float v1)");
    }

    @Override
    public void onIconClick(View view, float v, float v1) {
        Log.d(TAG, "onIconClick(View view, float v, float v1");
        Intent intent = new Intent(this, ProcessDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onIconDestroyed() {
        Log.d(TAG, "onIconDestroyed()");
        isMoveIconKill = true;
        stopSelf();
    }

    //startService后会先调用onCreate方法,接着调用onStartCommmand方法
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MonitorService------------onCreate()");
        setupViews();
        init();
    }

    private void init() {
        Magnet.Builder magnetBuilder = new Magnet.Builder(App.getAppContext(), false);
        mMagnet = magnetBuilder.setIconView(mMagnetRootView).setIconCallback(this).setShouldFlingAway(false).build();
        mMainHandler = new MyHandler();
        mMagnet.show();
    }

    private void setupViews() {
        mMagnetRootView = getMagnetRootView();
        mTvUiMem = (TextView) mMagnetRootView.findViewById(R.id.magnet_tv_ui_mem);
        mTvDpMem = (TextView) mMagnetRootView.findViewById(R.id.magnet_tv_dp_mem);
        mTvUiCpu = (TextView) mMagnetRootView.findViewById(R.id.magnet_tv_ui_cpu);
        mTvDpCpu = (TextView) mMagnetRootView.findViewById(R.id.magnet_tv_dp_cpu);
    }

    private View getMagnetRootView() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return layoutInflater.inflate(R.layout.layout_window_magnet, null);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg != null) {
                Bundle myBundle = msg.getData();
                String uiMemory = myBundle.getString(KEY_UI_MEMORY);
                String dpMemory = myBundle.getString(KEY_DP_MEMORY);
                String uiCpu = myBundle.getString(KEY_UI_CPU);
                String dpCpu = myBundle.getString(KEY_DP_CPU);
                mTvUiMem.setText(getString(R.string.ui_memory_msg, uiMemory));
                mTvDpMem.setText(getString(R.string.dp_memory_msg, dpMemory));
                mTvUiCpu.setText(getString(R.string.ui_cpu_time, uiCpu));
                mTvDpCpu.setText(getString(R.string.dp_cpu_time, dpCpu));
            }
        }
    }


    //如果多次使用startService方法，会多次调用该实例方法
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new MyTimeTask(), 0, 4000);
        return Service.START_STICKY;
    }

    private class MyTimeTask extends TimerTask {
        @Override
        public void run() {
            String[] uiMsg = ProcessUtil.getRunningProcessInfo(ProcessUtil.UI_PROCESS_NAME);
            String[] dpMsg = ProcessUtil.getRunningProcessInfo(ProcessUtil.DP_PROCESS_NAME);
            String uiPid = uiMsg[1];
            String dpPid = uiMsg[1];
            String uiTotalPss = uiMsg[2];
            String dpTotalPss = dpMsg[2];
            String uiCpuTime;
            String dpCpuTime;
            uiCpuTime = ProcessUtil.getProcessCpuRate(Integer.valueOf(uiPid));
            dpCpuTime = ProcessUtil.getProcessCpuRate(Integer.valueOf(dpPid));
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString(KEY_UI_MEMORY, uiTotalPss);
            bundle.putString(KEY_DP_MEMORY, dpTotalPss);
            bundle.putString(KEY_UI_CPU, uiCpuTime);
            bundle.putString(KEY_DP_CPU, dpCpuTime);
            message.setData(bundle);
            mMainHandler.sendMessage(message);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MonitorService------------onDestroy()");
        if (!isMoveIconKill) {
            mMagnet.destroy();
        }
        mTimer.cancel();
        mTimer = null; // 为了gc 尽快回收
    }
}
