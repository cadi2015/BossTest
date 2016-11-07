package com.wp.bosstest.activity;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.BuglyStrategy;
import com.wp.bosstest.R;
import com.wp.bosstest.application.App;
import com.wp.bosstest.fragment.FragmentMainDownloadManager;
import com.wp.bosstest.fragment.FragmentMainFileExplorer;
import com.wp.bosstest.utils.PackageUtil;


/**
 * Created by wp on 2016/1/15.
 */
public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawer;
    private TabLayout mTabLayout;
    private FragmentManager mFragmentManager;
    private FragmentMainDownloadManager mFragmentMainDownloadManager;
    private FragmentMainFileExplorer mFragmentMainFileExplorer;
    private ImageView mIvBox;
    private AnimationDrawable mBoxAniDrawable;
    private Handler mMainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setupViews();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x111) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        }
    }

    private void setupViews() {
        mDrawer = (DrawerLayout) findViewById(R.id.main_drawer);
        mDrawer.setDrawerListener(new MyDrawerLis());
        mTabLayout = (TabLayout) findViewById(R.id.main_tab_layout_bottom);
        mTabLayout.setOnTabSelectedListener(new MyOnTabSelLis());
        mIvBox = (ImageView) findViewById(R.id.main_iv_box);
        TabLayout.Tab tabDownload = mTabLayout.newTab().setText("下载管理");
        TabLayout.Tab tabFileExplorer = mTabLayout.newTab().setText("文件管理");
        tabFileExplorer.setIcon(R.mipmap.ic_launcher_file);
        tabDownload.setIcon(R.mipmap.ic_launcher_download);
        mTabLayout.addTab(tabDownload);
        mTabLayout.addTab(tabFileExplorer);
        mIvBox.setBackground(mBoxAniDrawable);
        mIvBox.setOnClickListener(new MyBoxClickLis());
    }

    private AnimationDrawable getBoxBg() {
        AnimationDrawable animationDrawable = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.frame_by_frame_box, null);
        } else {
            animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.frame_by_frame_box);
        }
        return animationDrawable;
    }

    private class MyBoxClickLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mBoxAniDrawable.start();
//            mMainHandler.sendEmptyMessageDelayed(0x111, 3000);
        }
    }

    private class MyOnTabSelLis implements TabLayout.OnTabSelectedListener {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            switch (tab.getPosition()) {
                case 0:
                    Log.d(TAG, "tab.getPosition() = " + tab.getPosition());
                    if (mFragmentManager.findFragmentByTag("DownloadManagerTag") == null) {
                        ft.add(R.id.main_frame_fragment_position, mFragmentMainDownloadManager, "DownloadManagerTag");
                        Log.d(TAG, "fragmentMainDownloadManager = " + mFragmentMainDownloadManager);
                        ft.commit();
                    } else {
                        ft.hide(mFragmentMainFileExplorer).show(mFragmentMainDownloadManager).commit();
                    }
                    break;
                case 1:
                    Log.d(TAG, "tab.getPosition() = " + tab.getPosition());
                    if (mFragmentManager.findFragmentByTag("MainFileExplorerTag") == null) {
                        ft.add(R.id.main_frame_fragment_position, mFragmentMainFileExplorer, "MainFileExplorerTag");
                        ft.hide(mFragmentMainDownloadManager);
                        ft.commit();
                    } else {
                        ft.hide(mFragmentMainDownloadManager).show(mFragmentMainFileExplorer).commit();
                    }
                    break;
                default:
                    break;
            }

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            PackageManager pm = getPackageManager();
            Log.d(TAG, "on TabReseleted(TabLayout.Tab tab.getposition = " + tab.getPosition());
            if (tab.getPosition() == 0) {
                Intent intent = pm.getLaunchIntentForPackage(PackageUtil.UiPackageName);
                Log.d(TAG, "Intent String = " + intent.toString());
                startActivity(intent);
            } else if (tab.getPosition() == 1) {
                Intent intent = pm.getLaunchIntentForPackage(PackageUtil.FileExplorerPackageName);
                startActivity(intent);
            }
        }
    }

    private void init() {
        mMainHandler = new MyHandler();
        mFragmentManager = getSupportFragmentManager();
        mFragmentMainDownloadManager = new FragmentMainDownloadManager();
        mFragmentMainFileExplorer = new FragmentMainFileExplorer();
        mBoxAniDrawable = getBoxBg();
        Bugly.init(App.getAppContext(), App.APP_ID, false);
    }

    private class MyDrawerLis implements DrawerLayout.DrawerListener {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            Log.d(TAG, "onDrawerSlide(View drawerView, float slideOffset)");
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            Log.d(TAG, "onDrawerOpened(View drawerView)");
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            Log.d(TAG, "onDrawerClosed(View drawerView)");
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            Log.d(TAG, "onDrawerStateChanged(int newState)");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState(Bundle outState)");
    }


}
