package com.wp.bosstest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.wp.bosstest.R;
import com.wp.bosstest.fragment.FragmentSplash;

/**
 * Created by wp on 2016/1/12.
 */
public class SplashActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new FragmentSplash();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.splash_frame_layout, fragment);
        fragmentTransaction.commit();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        }, 500);

    }

}
