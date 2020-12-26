package com.wp.cheez.activity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.wp.cheez.BuildConfig;
import com.wp.cheez.R;
import com.wp.cheez.config.AppConfig;
import com.wp.cheez.config.SharedConstant;
import com.wp.cheez.fragment.FragmentFirstApp;
import com.wp.cheez.fragment.FragmentSecondApp;
import com.wp.cheez.utils.PackageUtil;
import com.wp.cheez.utils.StatusBarUtil;

import java.lang.reflect.Field;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


/**
 * Created by wp on 2016/1/15.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private AppConfig mAppConfig;
    private DrawerLayout mDrawerLayout;
    private TabLayout mTabLayout;
    private FragmentManager mFragmentManager;
    private FragmentFirstApp mFragmentShortVideo;
    private FragmentSecondApp mFragmentSecondApp;
    private FragmentTransaction mFragmentTrans;
    private boolean mTabShortVideoIsSelected;
    private boolean mTabCrushIsSelected;
    private final String KEY_TAB_CRUSH_IS_SELECTED = "tabCrush_selected";
    private final String KEY_TAB_SHORT_VIDEO_SELECTED = "tabShortVideo_selected";
    private TextView mTvAboutThis;
    private TextView mTvGuide;
    private TextView mTvInstall;
    private FloatingActionButton mFabDevice;
    private FloatingActionButton mFabTool;
    private FloatingActionButton mFabWebView;
    private FloatingActionButton mFabTemp;
    private CoordinatorLayout mCoordinatorLayout;
    private int mActionBarHeight;
    private TabLayout.Tab mFirstTab;
    private TabLayout.Tab mSecondTab;
    private PackageUtil mPackageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate(Bundle savedInstanceState)");
        }
        setContentView(R.layout.activity_main);
        setupToolBar();
        init();
        initFragmentGroup(savedInstanceState);
        setupViews();
    }


    /**
     * 服了，用actionBar，getHeight方法，老是返回0
     * 所以换了个办法，还没有理解
     */
    private void setupToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        final TypedArray styledAttributes = this.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        mActionBarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        Log.d(TAG, "mActionBarHeight = " + mActionBarHeight);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true; //看来不用break，用return也可以哈
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFragmentGroup(Bundle bundle) {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTrans = mFragmentManager.beginTransaction();
        if (bundle == null) {
            mFragmentShortVideo = new FragmentFirstApp();
            mFragmentSecondApp = new FragmentSecondApp();
            mFragmentTrans.add(R.id.main_frame_fragment_position, mFragmentShortVideo, "ShortVideoTag");
            mFragmentTrans.add(R.id.main_frame_fragment_position, mFragmentSecondApp, "CrushTag");
        } else {
            mFragmentShortVideo = (FragmentFirstApp) mFragmentManager.findFragmentByTag("ShortVideoTag");
            mFragmentSecondApp = (FragmentSecondApp) mFragmentManager.findFragmentByTag("CrushTag");
        }
        mFragmentTrans.commit();
    }

    private void setupViews() {
        mPackageUtil = PackageUtil.getInstance(this);
        FloatingActionButton mFabGps = (FloatingActionButton) findViewById(R.id.main_fab_gps);
        mFabDevice = (FloatingActionButton) findViewById(R.id.main_fab_device);
        mFabTool = (FloatingActionButton) findViewById(R.id.main_fab_tool);
        mFabWebView = (FloatingActionButton) findViewById(R.id.main_fab_web_view);
        mTvGuide = (TextView) findViewById(R.id.layout_drawer_lower_part_tv_guide);
        mTvInstall = (TextView) findViewById(R.id.layout_drawer_lower_part_tv_install_apk);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
        mDrawerLayout.setDrawerListener(new MyDrawerLis());
        mTabLayout = findViewById(R.id.main_tab_layout_bottom);
        mTabLayout.setOnTabSelectedListener(new MyOnTabSelLis());
        mTvAboutThis = (TextView) findViewById(R.id.layout_drawer_lower_part_tv_about_this);
        mFirstTab = mTabLayout.newTab().setText(mPackageUtil.getAppName(mAppConfig.getStr(SharedConstant.SELECT_FRIST_APP_PKG_KEY,PackageUtil.SHORT_VIDEO_PACKAGE_NAME)));
        mFirstTab.setIcon(mPackageUtil.getAppIcon(mAppConfig.getStr(SharedConstant.SELECT_FRIST_APP_PKG_KEY,PackageUtil.SHORT_VIDEO_PACKAGE_NAME)));
        mSecondTab = mTabLayout.newTab().setText(mPackageUtil.getAppName(mAppConfig.getStr(SharedConstant.SELECT_SECOND_APP_PKG_KEY,PackageUtil.CRUSH_PACKAGE_NAME)));
        mSecondTab.setIcon(mPackageUtil.getAppIcon(mAppConfig.getStr(SharedConstant.SELECT_SECOND_APP_PKG_KEY,PackageUtil.CRUSH_PACKAGE_NAME)));
        mTabLayout.addTab(mFirstTab, mTabShortVideoIsSelected);
        mTabLayout.addTab(mSecondTab,mTabCrushIsSelected);
        MyLowerTvClickLis myTvClickLis = new MyLowerTvClickLis();
        mTvAboutThis.setOnClickListener(myTvClickLis);
        mTvGuide.setOnClickListener(myTvClickLis);
        mTvInstall.setOnClickListener(myTvClickLis);
        MyFabBtnClickLis fabBtnClickLis = new MyFabBtnClickLis();
        mFabDevice.setOnClickListener(fabBtnClickLis);
        mFabTool.setOnClickListener(fabBtnClickLis);
        mFabGps.setOnClickListener(fabBtnClickLis);
        mFabWebView.setOnClickListener(fabBtnClickLis);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_base);
        relativeLayout.setPadding(0,getStatusBarHeight()+getActionBarHeight(),0,0);//再加个actionBar的高度就好
        mFabTemp = findViewById(R.id.main_fab_temp);
        mFabTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DownloadManagerActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateTab(){
        mFirstTab.setText(mPackageUtil.getAppName(mAppConfig.getStr(SharedConstant.SELECT_FRIST_APP_PKG_KEY,PackageUtil.SHORT_VIDEO_PACKAGE_NAME)));
        mFirstTab.setIcon(mPackageUtil.getAppIcon(mAppConfig.getStr(SharedConstant.SELECT_FRIST_APP_PKG_KEY,PackageUtil.SHORT_VIDEO_PACKAGE_NAME)));
        mSecondTab.setText(mPackageUtil.getAppName(mAppConfig.getStr(SharedConstant.SELECT_SECOND_APP_PKG_KEY,PackageUtil.CRUSH_PACKAGE_NAME)));
        mSecondTab.setIcon(mPackageUtil.getAppIcon(mAppConfig.getStr(SharedConstant.SELECT_SECOND_APP_PKG_KEY,PackageUtil.CRUSH_PACKAGE_NAME)));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateTab();
    }

    public View getTextView(int index){
        View tabView = null;
        TextView textView = null;
        TabLayout.Tab tab = mTabLayout.getTabAt(index); //先拿到Tab
        Field tabViewField = null;
        Field textViewField = null; //我cao，要反射的TabView，是个default级别的啊，反射怎么拿啊，下次研究吧，好累
        try {
            tabViewField = TabLayout.Tab.class.getDeclaredField("mView"); //准备个Field，我们要拿到Tab中的TabView
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        tabViewField.setAccessible(true);
        try {
            tabView = (View) tabViewField.get(tab);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return textView;
    }



    private int getActionBarHeight(){
        return mActionBarHeight;
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

    private class MyFabBtnClickLis implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.main_fab_device:
                    Intent intentDevice = new Intent(MainActivity.this, PhoneActivity.class);
                    startActivity(intentDevice);
                    break;
                case R.id.main_fab_tool:
                    Intent intentTool = new Intent(MainActivity.this, FillSpaceActivity.class);
                    startActivity(intentTool);
                    break;
                case R.id.main_fab_gps:
                    Intent intentLocation = new Intent(MainActivity.this, LocationActivity.class);
                    startActivity(intentLocation);
                    break;
                case R.id.main_fab_web_view:
                    Intent intentWeb = new Intent(MainActivity.this, WebActivity.class);
                    intentWeb.putExtra("jumpUrl", "http://qa.www.cheez.com/crushu/faq/dist/index.html#/");
                    startActivity(intentWeb);
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
                    mFragmentTrans.show(mFragmentShortVideo).hide(mFragmentSecondApp).commit();
                    mAppConfig.putBool(KEY_TAB_SHORT_VIDEO_SELECTED, true);
                    mAppConfig.putBool(KEY_TAB_CRUSH_IS_SELECTED,false);
                    break;
                case 1:
                    mFragmentTrans.show(mFragmentSecondApp).hide(mFragmentShortVideo).commit();
                    mAppConfig.putBool(KEY_TAB_CRUSH_IS_SELECTED,true);
                    mAppConfig.putBool(KEY_TAB_SHORT_VIDEO_SELECTED,false);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onTabUnselected(TabLayout.Tab tab), tab.Position() = " + tab.getPosition());
            }
            if (tab.getPosition() == 0) {
                mAppConfig.putBool(KEY_TAB_CRUSH_IS_SELECTED, false);
            } else if (tab.getPosition() == 1) {
                mAppConfig.putBool(KEY_TAB_SHORT_VIDEO_SELECTED, false);
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            PackageManager pm = getPackageManager();
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onTabReselected(TabLayout.Tab) tab.getPosition() = " + tab.getPosition());
            }
            Intent intent;
            boolean showNotFoundAppFlag = false;
            String jumpPackageName = "";
            switch (tab.getPosition()) {
                case 0:
                    jumpPackageName = mAppConfig.getStr(SharedConstant.SELECT_FRIST_APP_PKG_KEY, PackageUtil.SHORT_VIDEO_PACKAGE_NAME);
                    ;
                    break;
                case 1:
                    jumpPackageName = mAppConfig.getStr(SharedConstant.SELECT_SECOND_APP_PKG_KEY, PackageUtil.CRUSH_PACKAGE_NAME);
                    break;
                default:
                    break;
            }

            intent = pm.getLaunchIntentForPackage(jumpPackageName);
            if(intent != null) {
                startActivity(intent);
            } else {
                showNotFoundAppFlag = true;
            }
            if(showNotFoundAppFlag) {
                Toast.makeText(MainActivity.this, "没有找到启动的App", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void init() {
        mAppConfig = AppConfig.getInstance(getApplicationContext());
        mTabShortVideoIsSelected = mAppConfig.getBool(KEY_TAB_SHORT_VIDEO_SELECTED, true);
        mTabCrushIsSelected = mAppConfig.getBool(KEY_TAB_CRUSH_IS_SELECTED, false);
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
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
