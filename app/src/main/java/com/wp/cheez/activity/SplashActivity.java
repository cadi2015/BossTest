package com.wp.cheez.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.wp.cheez.R;
import com.wp.cheez.config.SharedConstant;
import com.wp.cheez.fragment.FragmentSplash;
import com.wp.cheez.sqlite.SQLiteManager;
import com.wp.cheez.utils.AppInfo;
import com.wp.cheez.utils.LogHelper;
import com.wp.cheez.utils.PermissionUtil;

/**
 * Created by wp on 2016/1/12.
 */
public class SplashActivity extends FragmentActivity {
    private static final String TAG  = LogHelper.makeTag(SplashActivity.class);
    private SharedPreferences mSharedPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new FragmentSplash();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.splash_frame_layout, fragment);
        fragmentTransaction.commit();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSharedPre.getBoolean("is_first", true)|| AppInfo.getVersionCode(getApplicationContext()) > mSharedPre.getInt("version_code", 0)) {
                    initDb();
                    Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                    intent.putExtra("from", "Splash");
                    startActivity(intent);
                    PermissionUtil.upgradeRootPermission(SplashActivity.this.getPackageCodePath()); //就是为了拉权限
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                SplashActivity.this.finish();
            }
        }, 400);
    }

    private void init() {
        mSharedPre = getSharedPreferences(SharedConstant.SHARED_BOSS_CONFIG_NAME, Context.MODE_PRIVATE);
    }

    private void initDb() {
        SQLiteManager sqliteManager = new SQLiteManager( );
        sqliteManager.fromAssetsCopyDB(this);
    }
}
