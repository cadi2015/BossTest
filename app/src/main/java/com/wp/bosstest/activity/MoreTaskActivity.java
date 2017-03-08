package com.wp.bosstest.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wp.bosstest.R;
import com.wp.bosstest.fragment.FragmentMoreTask;

/**
 * Created by cadi on 2016/6/20.
 */
public class MoreTaskActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.more_task_frame_layout, new FragmentMoreTask());
        ft.commit();
    }

    @Override
    public boolean isDisplayHomeAsUp() {
        return true;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_more_task;
    }

    @Override
    public boolean isDisplayToolBar() {
        return true;
    }
}
