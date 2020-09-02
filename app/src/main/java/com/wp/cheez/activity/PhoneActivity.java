package com.wp.cheez.activity;


import android.os.Bundle;

import com.wp.cheez.R;
import com.wp.cheez.fragment.FragmentPhone;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

/**
 * 设备信息情况
 */
public class PhoneActivity extends BaseActivity{
    @Override
    public boolean isDisplayHomeAsUp() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.phone_frame_layout,new FragmentPhone()).commit();
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_phone;
    }

    @Override
    public boolean isDisplayToolBar() {
        return true;
    }

    @Override
    public String getToolBarTitle() {
        return "设备资料";
    }
}
