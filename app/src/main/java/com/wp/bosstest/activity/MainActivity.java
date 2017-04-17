package com.wp.bosstest.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
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
import com.wp.bosstest.BuildConfig;
import com.wp.bosstest.R;
import com.wp.bosstest.application.App;
import com.wp.bosstest.config.SharedConstant;
import com.wp.bosstest.fragment.FragmentMainDownloadManager;
import com.wp.bosstest.fragment.FragmentMainFileExplorer;
import com.wp.bosstest.fragment.FragmentShortVideo;
import com.wp.bosstest.utils.PackageUtil;
import com.wp.bosstest.utils.StatusBarUtil;


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
    private FragmentMainFileExplorer mFragmentMainFileExplorer;
    private FragmentShortVideo mFragmentShortVideo;
    private Handler mMainHandler;
    private FragmentTransaction mFragmentTrans;
    private boolean mTabDownloadIsSelected;
    private boolean mTabFileExplorerIsSelected;
    private boolean mTabShortVideoIsSelected;
    private final String KEY_TAB_DOWNLOAD_IS_SELECTED = "tabDownload_selected";
    private final String KEY_TAB_FILE_IS_SELECTED = "tabFile_selected";
    private final String KEY_TAB_SHORT_VIDEO_SELECTED = "tabShortVideo_selected";
    private TextView mTvAboutThis;
    private TextView mTvGuide;
    private TextView mTvInstall;
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
            mFragmentMainFileExplorer = new FragmentMainFileExplorer();
            mFragmentShortVideo = new FragmentShortVideo();
            mFragmentTrans.add(R.id.main_frame_fragment_position, mFragmentMainDownloadManager, "DownloadManagerTag");
            mFragmentTrans.add(R.id.main_frame_fragment_position, mFragmentMainFileExplorer, "MainFileExplorerTag");
            mFragmentTrans.add(R.id.main_frame_fragment_position, mFragmentShortVideo, "ShortVideoTag");
        } else {
            mFragmentMainDownloadManager = (FragmentMainDownloadManager) mFragmentManager.findFragmentByTag("DownloadManagerTag");
            mFragmentMainFileExplorer = (FragmentMainFileExplorer) mFragmentManager.findFragmentByTag("MainFileExplorerTag");
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
        mTvGuide = (TextView) findViewById(R.id.layout_drawer_lower_part_tv_guide);
        mTvInstall = (TextView) findViewById(R.id.layout_drawer_lower_part_tv_install_apk);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
        mDrawerLayout.setDrawerListener(new MyDrawerLis());
        mTabLayout = (TabLayout) findViewById(R.id.main_tab_layout_bottom);
        mTabLayout.setOnTabSelectedListener(new MyOnTabSelLis());
        mTvAboutThis = (TextView) findViewById(R.id.layout_drawer_lower_part_tv_about_this);
        TabLayout.Tab tabDownload = mTabLayout.newTab().setText("下载管理");
        TabLayout.Tab tabFileExplorer = mTabLayout.newTab().setText("文件管理");
        TabLayout.Tab tabShortVideo = mTabLayout.newTab().setText("有料");
        tabFileExplorer.setIcon(R.mipmap.ic_launcher_file);
        tabDownload.setIcon(R.mipmap.ic_launcher_download);
        tabShortVideo.setIcon(R.mipmap.ic_launcher_short_video);
        mTabLayout.addTab(tabDownload, mTabDownloadIsSelected);
        mTabLayout.addTab(tabFileExplorer, mTabFileExplorerIsSelected);
        mTabLayout.addTab(tabShortVideo, mTabShortVideoIsSelected);
        MyLowerTvClickLis myTvClickLis = new MyLowerTvClickLis();
        mTvAboutThis.setOnClickListener(myTvClickLis);
        mTvGuide.setOnClickListener(myTvClickLis);
        mTvInstall.setOnClickListener(myTvClickLis);
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
                    mFragmentTrans.hide(mFragmentMainFileExplorer).hide(mFragmentShortVideo).show(mFragmentMainDownloadManager).commit();
                    mSharedEditor.putBoolean(KEY_TAB_DOWNLOAD_IS_SELECTED, true);
                    break;
                case 1:
                    mFragmentTrans.hide(mFragmentMainDownloadManager).hide(mFragmentShortVideo).show(mFragmentMainFileExplorer).commit();
                    mSharedEditor.putBoolean(KEY_TAB_FILE_IS_SELECTED, true);
                    break;
                case 2:
                    mFragmentTrans.hide(mFragmentMainDownloadManager).hide(mFragmentMainFileExplorer).show(mFragmentShortVideo).commit();
                    mSharedEditor.putBoolean(KEY_TAB_SHORT_VIDEO_SELECTED, true);
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
                mSharedEditor.putBoolean(KEY_TAB_FILE_IS_SELECTED, false);
            } else if (tab.getPosition() == 2) {
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
                Intent intent = pm.getLaunchIntentForPackage(PackageUtil.FileExplorerPackageName);
                if (intent != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "没有找到启动的App", Toast.LENGTH_SHORT).show();
                }
            } else if (tab.getPosition() == 2) {
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
        mTabFileExplorerIsSelected = mSharedPreferences.getBoolean(KEY_TAB_FILE_IS_SELECTED, false);
        mTabShortVideoIsSelected = mSharedPreferences.getBoolean(KEY_TAB_SHORT_VIDEO_SELECTED, false);
        mMainHandler = new MyHandler();
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
