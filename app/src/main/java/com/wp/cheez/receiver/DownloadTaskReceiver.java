package com.wp.cheez.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wp.cheez.utils.LogHelper;

/**
 * Created by cadi on 2016/8/25.
 */
public class DownloadTaskReceiver extends BroadcastReceiver {
    private static final String TAG = LogHelper.makeTag(DownloadTaskReceiver.class);

    /**
     * 这个onReceive方法会被回到
     *
     * @param context 上下文
     * @param intent  Intent对象
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive(Context context, Intent, intent)");
        Log.d(TAG, "action = " + intent.getAction());
    }
}
