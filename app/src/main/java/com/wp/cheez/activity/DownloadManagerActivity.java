package com.wp.cheez.activity;

import android.os.Bundle;

import com.wp.cheez.R;
import com.wp.cheez.fragment.FragmentMainDownloadManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DownloadManagerActivity extends BaseActivity {
    private static final String MAIN_DOWNLOAD_MANAGER_TAG = "fk";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.fl_content,new FragmentMainDownloadManager(),MAIN_DOWNLOAD_MANAGER_TAG);
        ft.commit();
    }

    @Override
    public boolean isDisplayHomeAsUp() {
        return true;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_temp;
    }

    @Override
    public boolean isDisplayToolBar() {
        return true;
    }

    @Override
    public String getToolBarTitle() {
        return "下载管理中心";
    }
}
