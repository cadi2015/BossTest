package com.wp.cheez.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.wp.cheez.R;
import com.wp.cheez.fragment.FragmentPhone;

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
        return "设备信息";
    }
}
