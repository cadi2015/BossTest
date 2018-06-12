package com.wp.cheez.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.bugly.Bugly;
import com.wp.cheez.BuildConfig;
import com.wp.cheez.R;
import com.wp.cheez.application.App;
import com.wp.cheez.config.SharedConstant;
import com.wp.cheez.fragment.FragmentMainDownloadManager;
import com.wp.cheez.fragment.FragmentMainFileExplorer;
import com.wp.cheez.fragment.FragmentShortVideo;
import com.wp.cheez.utils.PackageUtil;
import com.wp.cheez.utils.StatusBarUtil;


/**
 * Created by wp on 2016/1/15.
 */
public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedEditor;
    private DrawerLayout mDrawerLayout;
    private TabLayout mTabLayout;
    private FragmentManager mFragmentManager;
    private FragmentMainDownloadManager mFragmentMainDownloadManager;
    private FragmentShortVideo mFragmentShortVideo;
    private FragmentTransaction mFragmentTrans;
    private boolean mTabDownloadIsSelected;
    private boolean mTabShortVideoIsSelected;
    private final String KEY_TAB_DOWNLOAD_IS_SELECTED = "tabDownload_selected";
    private final String KEY_TAB_SHORT_VIDEO_SELECTED = "tabShortVideo_selected";
    private TextView mTvAboutThis;
    private TextView mTvGuide;
    private TextView mTvInstall;
    private FloatingActionButton mFabDevice;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate(Bundle savedInstanceState)");
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.color_system_bar_bg));
        }
        setContentView(R.layout.activity_main);
        init();
        initFragmentGroup(savedInstanceState);
        setupViews();
    }

    private void initFragmentGroup(Bundle bundle) {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTrans = mFragmentManager.beginTransaction();
        if (bundle == null) {
            mFragmentMainDownloadManager = new FragmentMainDownloadManager();
            mFragmentShortVideo = new FragmentShortVideo();
            mFragmentTrans.add(R.id.main_frame_fragment_position, mFragmentShortVideo, "ShortVideoTag");
        } else {
            mFragmentShortVideo = (FragmentShortVideo) mFragmentManager.findFragmentByTag("ShortVideoTag");
        }
        mFragmentTrans.commit();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x111) {
                Intent intent = new Intent(MainActivity.this, GuideActivity.class);
                intent.putExtra("from", "Main");
                startActivity(intent);
            }
        }
    }

    private void setupViews() {
        PackageUtil packageUtil = PackageUtil.getInstance(this);
        mFabDevice = (FloatingActionButton) findViewById(R.id.main_fab_device);
        mTvGuide = (TextView) findViewById(R.id.layout_drawer_lower_part_tv_guide);
        mTvInstall = (TextView) findViewById(R.id.layout_drawer_lower_part_tv_install_apk);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
        mDrawerLayout.setDrawerListener(new MyDrawerLis());
        mTabLayout = (TabLayout) findViewById(R.id.main_tab_layout_bottom);
        mTabLayout.setOnTabSelectedListener(new MyOnTabSelLis());
        mTvAboutThis = (TextView) findViewById(R.id.layout_drawer_lower_part_tv_about_this);
        TabLayout.Tab tabShortVideo = mTabLayout.newTab().setText(packageUtil.getAppName(PackageUtil.SHORT_VIDEO_PACKAGE_NAME));
        tabShortVideo.setIcon(packageUtil.getAppIcon(PackageUtil.SHORT_VIDEO_PACKAGE_NAME));
        mTabLayout.addTab(tabShortVideo, mTabShortVideoIsSelected);
        MyLowerTvClickLis myTvClickLis = new MyLowerTvClickLis();
        mTvAboutThis.setOnClickListener(myTvClickLis);
        mTvGuide.setOnClickListener(myTvClickLis);
        mTvInstall.setOnClickListener(myTvClickLis);
        mFabDevice.setOnClickListener(new MyBtnClickLis());
        mCoordinatorLayout.setPadding(0, getStatusBarHeight(), 0, 0);
    }

    private int getStatusBarHeight() {
        int statusBarHeight1 = -1;
//获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        Log.e("WangJ", "状态栏-方法1:" + statusBarHeight1);
        return statusBarHeight1;
    }

    private class MyBtnClickLis implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.main_fab_device:
                    Intent intent = new Intent(MainActivity.this, PhoneActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    private class MyLowerTvClickLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_drawer_lower_part_tv_about_this:
                    Intent intentAbout = new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(intentAbout);
                    break;
                case R.id.layout_drawer_lower_part_tv_guide:
                    Intent intentGuide = new Intent(MainActivity.this, GuideActivity.class);
                    startActivity(intentGuide);
                    break;
                case R.id.layout_drawer_lower_part_tv_install_apk:
                    Intent intentInstall = new Intent(MainActivity.this, InstallAppActivity.class);
                    startActivity(intentInstall);
                    break;
                default:
                    break;
            }
        }
    }


    private class MyOnTabSelLis implements TabLayout.OnTabSelectedListener {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            mFragmentTrans = mFragmentManager.beginTransaction();
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onTabSelected(TabLayout.Tab tab), tab.getPosition() = " + tab.getPosition());
            }
            switch (tab.getPosition()) {
                case 0:
                    mFragmentTrans.show(mFragmentShortVideo).commit();
                    mSharedEditor.putBoolean(KEY_TAB_SHORT_VIDEO_SELECTED, true);
                    break;
                case 1:
                    break;
                default:
                    break;
            }
            mSharedEditor.apply();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onTabUnselected(TabLayout.Tab tab), tab.Position() = " + tab.getPosition());
            }
            if (tab.getPosition() == 0) {
                mSharedEditor.putBoolean(KEY_TAB_DOWNLOAD_IS_SELECTED, false);
            } else if (tab.getPosition() == 1) {
                mSharedEditor.putBoolean(KEY_TAB_SHORT_VIDEO_SELECTED, false);
            }
            mSharedEditor.apply();
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            PackageManager pm = getPackageManager();
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onTabReselected(TabLayout.Tab) tab.getPosition() = " + tab.getPosition());
            }
            if (tab.getPosition() == 0) {
                Intent intent = pm.getLaunchIntentForPackage(PackageUtil.UiPackageName);
                if (intent != null) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "Intent String = " + intent.toString());
                    }
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "没有找到启动的App", Toast.LENGTH_SHORT).show();
                }
            } else if (tab.getPosition() == 1) {
                Intent intent = pm.getLaunchIntentForPackage(PackageUtil.SHORT_VIDEO_PACKAGE_NAME);
                if (intent != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "没有找到启动的App", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void init() {
        mSharedPreferences = getSharedPreferences(SharedConstant.SHARED_BOSS_CONFIG_NAME, Context.MODE_PRIVATE);
        mSharedEditor = mSharedPreferences.edit();
        mTabDownloadIsSelected = mSharedPreferences.getBoolean(KEY_TAB_DOWNLOAD_IS_SELECTED, true);
        mTabShortVideoIsSelected = mSharedPreferences.getBoolean(KEY_TAB_SHORT_VIDEO_SELECTED, false);
        Bugly.init(App.getAppContext(), App.APP_ID, false);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Build.M^^^ == " + Build.MANUFACTURER);
        }
        if (Build.MANUFACTURER.equals("Xiaomi")) {
            StatusBarUtil.MIUISetStatusBarLightMode(getWindow(), true);
        }
    }

    private class MyDrawerLis implements DrawerLayout.DrawerListener {

        WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display mDisplay = mWindowManager.getDefaultDisplay();

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onDrawerSlide(View drawerView, float slideOffset) ");
                Log.d(TAG, "drawerView = " + drawerView + " slideOffset = " + slideOffset);
            }
            mCoordinatorLayout.layout(drawerView.getRight(), drawerView.getTop(), drawerView.getWidth() + mDisplay.getWidth(), mDisplay.getHeight());
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "drawerView getRight = " + drawerView.getWidth() + " getTop = " + drawerView.getTop());
            }
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

    private long mFirstOnBackPressedTime = 0L;

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            long currentPressedBackTime = System.currentTimeMillis();
            if (currentPressedBackTime - mFirstOnBackPressedTime > 2000) {
                Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                mFirstOnBackPressedTime = currentPressedBackTime;
            } else {
                super.onBackPressed();
            }
        }

        Log.d(TAG, "on BackPressed()");
    }

}
