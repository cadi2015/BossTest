package com.wp.cheez.config;

import android.content.Context;
import android.content.SharedPreferences;

public class AppConfig {

    private static AppConfig instance;
    private Context mContext;

    private AppConfig(Context applicationContext) {
        super();
        mContext = applicationContext;
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
        SharedPreferences sp = context.getSharedPreferences(SharedConstant.SHARED_BOSS_CONFIG_NAME, Context.MODE_PRIVATE);
        return sp;
    }

    public void put(String key, String value) {
        SharedPreferences sp = getSp(mContext);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStr(String key, String defValue) {
        SharedPreferences sp = getSp(mContext);
        return sp.getString(key, defValue);
    }

    public boolean getBool(String key, boolean defValue) {
        return getSp(mContext).getBoolean(key, defValue);
    }

    public void putBool(String key, boolean defValue) {
        SharedPreferences sp = getSp(mContext);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, defValue);
        editor.apply();
    }

}
