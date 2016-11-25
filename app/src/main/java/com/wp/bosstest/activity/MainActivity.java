package com.wp.bosstest.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.tencent.bugly.Bugly;
import com.wp.bosstest.R;
import com.wp.bosstest.application.App;
import com.wp.bosstest.config.SharedConstant;
import com.wp.bosstest.fragment.FragmentMainDownloadManager;
import com.wp.bosstest.fragment.FragmentMainFileExplorer;
import com.wp.bosstest.utils.PackageUtil;


/**
 * Created by wp on 2016/1/15.
 */
public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedEditor;
    private DrawerLayout mDrawer;
    private TabLayout mTabLayout;
    private FragmentManager mFragmentManager;
    private FragmentMainDownloadManager mFragmentMainDownloadManager;
    private FragmentMainFileExplorer mFragmentMainFileExplorer;
    private ImageView mIvBox;
    private AnimationDrawable mBoxAniDrawable;
    private Handler mMainHandler;
    private FragmentTransaction mFragmentTrans;
    private boolean mTabDownloadIsSelected;
    private boolean mTabFileExplorerIsSelected;
    private final String KEY_TAB_DOWNLOAD_IS_SELECTED = "tabDownload_selected";
    private final String KEY_TAB_FILE_IS_SELECTED = "tabFile_selected";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle savedInstanceState)");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
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
            mFragmentTrans.add(R.id.main_frame_fragment_position, mFragmentMainDownloadManager, "DownloadManagerTag");
            mFragmentTrans.add(R.id.main_frame_fragment_position, mFragmentMainFileExplorer, "MainFileExplorerTag");
        } else {
            mFragmentMainDownloadManager = (FragmentMainDownloadManager) mFragmentManager.findFragmentByTag("DownloadManagerTag");
            mFragmentMainFileExplorer = (FragmentMainFileExplorer) mFragmentManager.findFragmentByTag("MainFileExplorerTag");
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
        mDrawer = (DrawerLayout) findViewById(R.id.main_drawer);
        mDrawer.setDrawerListener(new MyDrawerLis());
        mTabLayout = (TabLayout) findViewById(R.id.main_tab_layout_bottom);
        mTabLayout.setOnTabSelectedListener(new MyOnTabSelLis());
        mIvBox = (ImageView) findViewById(R.id.main_iv_box);
        TabLayout.Tab tabDownload = mTabLayout.newTab().setText("下载管理");
        TabLayout.Tab tabFileExplorer = mTabLayout.newTab().setText("文件管理");
        tabFileExplorer.setIcon(R.mipmap.ic_launcher_file);
        tabDownload.setIcon(R.mipmap.ic_launcher_download);
        mTabLayout.addTab(tabDownload, mTabDownloadIsSelected);
        mTabLayout.addTab(tabFileExplorer, mTabFileExplorerIsSelected);
        mBoxAniDrawable = getBoxBg();
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
            mMainHandler.sendEmptyMessageDelayed(0x111, 2000);
        }
    }

    private class MyOnTabSelLis implements TabLayout.OnTabSelectedListener {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            mFragmentTrans = mFragmentManager.beginTransaction();
            switch (tab.getPosition()) {
                case 0:
                    Log.d(TAG, "tab.getPosition() = " + tab.getPosition());
                    mFragmentTrans.hide(mFragmentMainFileExplorer).show(mFragmentMainDownloadManager).commit();
                    mSharedEditor.putBoolean(KEY_TAB_DOWNLOAD_IS_SELECTED, true);
                    break;
                case 1:
                    Log.d(TAG, "tab.getPosition() = " + tab.getPosition());
                    mFragmentTrans.hide(mFragmentMainDownloadManager).show(mFragmentMainFileExplorer).commit();
                    mSharedEditor.putBoolean(KEY_TAB_FILE_IS_SELECTED, true);
                    break;
                default:
                    break;
            }
            mSharedEditor.apply();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            Log.d(TAG, "onTabUnselected(TabLayout.Tab tab), tab position = " + tab.getPosition());
            if (tab.getPosition() == 0) {
                mSharedEditor.putBoolean(KEY_TAB_DOWNLOAD_IS_SELECTED, false);
            } else if (tab.getPosition() == 1) {
                mSharedEditor.putBoolean(KEY_TAB_FILE_IS_SELECTED, false);
            }
            mSharedEditor.apply();
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            PackageManager pm = getPackageManager();
            Log.d(TAG, "on TabReseleted(TabLayout.Tab tab.getposition = " + tab.getPosition());
            if (tab.getPosition() == 0) {
                Intent intent = pm.getLaunchIntentForPackage(PackageUtil.UiPackageName);
                if (intent != null) {
                    Log.d(TAG, "Intent String = " + intent.toString());
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
            }
        }
    }

    private void init() {
        mSharedPreferences = getSharedPreferences(SharedConstant.SHARED_BOSS_CONFIG_NAME, Context.MODE_PRIVATE);
        mSharedEditor = mSharedPreferences.edit();
        mTabDownloadIsSelected = mSharedPreferences.getBoolean(KEY_TAB_DOWNLOAD_IS_SELECTED, true);
        mTabFileExplorerIsSelected = mSharedPreferences.getBoolean(KEY_TAB_FILE_IS_SELECTED, false);
        mMainHandler = new MyHandler();
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
