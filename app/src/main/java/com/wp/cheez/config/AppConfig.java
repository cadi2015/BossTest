package com.wp.cheez.config;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;

public class AppConfig {

    private static AppConfig instance;
    private final WeakReference<Context> contextWeakReference;
    private AppConfig(Context context) {
        super();
        contextWeakReference = new WeakReference<>(context);
    }

    public static AppConfig getInstance(Context context) {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    instance = new AppConfig(context);
                }
            }
        }

        return instance;
    }

    private SharedPreferences getSp(Context context) {
        return context.getSharedPreferences(SharedConstant.SHARED_BOSS_CONFIG_NAME, Context.MODE_PRIVATE);
    }

    public void put(String key, String value) {
        SharedPreferences sp = getSp(contextWeakReference.get());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStr(String key, String defValue) {
        SharedPreferences sp = getSp(contextWeakReference.get());
        return sp.getString(key, defValue);
    }

    public boolean getBool(String key, boolean defValue) {
        return getSp(contextWeakReference.get()).getBoolean(key, defValue);
    }

    public void putBool(String key, boolean defValue) {
        SharedPreferences sp = getSp(contextWeakReference.get());
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, defValue);
        editor.apply();
    }

}
