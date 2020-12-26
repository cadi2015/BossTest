package com.wp.cheez.activity;

import android.os.Bundle;

import com.wp.cheez.R;
import com.wp.cheez.fragment.FragmentMainDownloadManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DownloadManagerActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.fl_content,new FragmentMainDownloadManager(),"fk");
        ft.commit();
    }

    @Override
    public boolean isDisplayHomeAsUp() {
        return false;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_temp;
    }

    @Override
    public boolean isDisplayToolBar() {
        return false;
    }

    @Override
    public String getToolBarTitle() {
        return null;
    }
}
