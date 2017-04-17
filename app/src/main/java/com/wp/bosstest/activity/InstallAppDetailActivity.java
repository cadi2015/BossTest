package com.wp.bosstest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.wp.bosstest.R;
import com.wp.bosstest.fragment.FragmentInstallAppDetail;

/**
 * Created by cadi on 2017/4/17.
 */

public class InstallAppDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String packageNameStr = intent.getStringExtra("packageName");
        Bundle bundle = new Bundle();
        bundle.putString("packageName", packageNameStr);
        Fragment fragment = new FragmentInstallAppDetail();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.install_app_detail_frame_position, fragment).commit();
    }

    @Override
    public boolean isDisplayHomeAsUp() {
        return true;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_install_app_detail;
    }

    @Override
    public boolean isDisplayToolBar() {
        return true;
    }
}
