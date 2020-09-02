package com.wp.cheez.activity;

import android.os.Bundle;


import com.wp.cheez.R;
import com.wp.cheez.fragment.FragmentInstallAppList;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

/**
 * Created by cadi on 2017/4/14.
 * 手机上安装了哪些App
 */

public class InstallAppActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.install_app_frame_layout_position, new FragmentInstallAppList()).commit();
    }

    @Override
    public boolean isDisplayHomeAsUp() {
        return true;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_install_app;
    }

    @Override
    public boolean isDisplayToolBar() {
        return true;
    }

    @Override
    public String getToolBarTitle() {
        return "安装应用";
    }
}
