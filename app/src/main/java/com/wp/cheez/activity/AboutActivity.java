package com.wp.cheez.activity;

import android.app.FragmentManager;
import android.os.Bundle;

import com.wp.cheez.R;
import com.wp.cheez.fragment.FragmentAbout;

/**
 * Created by cadi on 2016/8/10.
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.about_frame_layout, new FragmentAbout()).commit();
    }

    @Override
    public boolean isDisplayHomeAsUp() {
        return true;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_about;
    }

    @Override
    public boolean isDisplayToolBar() {
        return true;
    }

    @Override
    public String getToolBarTitle() {
        return "关于";
    }
}
