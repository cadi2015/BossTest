package com.wp.cheez.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wp.cheez.R;
import com.wp.cheez.utils.FpsUtil;
import com.wp.cheez.utils.LogHelper;
import com.wp.cheez.window.IconCallback;
import com.wp.cheez.window.Magnet;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by cadi on 2016/10/17.
 */

public class FpsService extends Service implements IconCallback{
    private static final String TAG = LogHelper.makeTag(FpsService.class);
    private TextView mTvFps;
    private Handler mHandler;
    private Timer mTimer;
    private Magnet mMagnet;
    @Override
    public void onCreate() {
        super.onCreate();
        Magnet.Builder builder = new Magnet.Builder(getApplicationContext(), false);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View rootView = inflater.inflate(R.layout.layout_window_magnet_fps, null);
        mTvFps = (TextView) rootView.findViewById(R.id.magnet_fps_tv_value);
        mHandler = new MyHandler(mTvFps);
        mTimer = new Timer();
        mMagnet = builder.setIconView(rootView).setInitialPosition(-300, 0).setIconCallback(this).build();
        mMagnet.show();
    }


    private static class MyHandler extends Handler {
        private final WeakReference<TextView> weakReference;
        public MyHandler(TextView textView){
            weakReference = new WeakReference<>(textView);
        }
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            weakReference.get().setText(String.valueOf(bundle.getFloat("fps")));
        }
    }





    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                float fpsValue = FpsUtil.fps();
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putFloat("fps", fpsValue);
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        }, 0, 1000);
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMagnet.destroy();
        mTimer.cancel();
        mTimer = null;
    }

    @Override
    public void onFlingAway() {

    }

    @Override
    public void onMove(float x, float y) {

    }

    @Override
    public void onIconClick(View icon, float iconXPose, float iconYPose) {

    }

    @Override
    public void onIconDestroyed() {

    }
}
