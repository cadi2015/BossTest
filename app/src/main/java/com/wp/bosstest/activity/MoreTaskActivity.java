package com.wp.bosstest.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.wp.bosstest.R;
import com.wp.bosstest.fragment.FragmentMoreTask;

/**
 * Created by cadi on 2016/6/20.
 */
public class MoreTaskActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_task);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.more_task_frame_layout, new FragmentMoreTask());
        ft.commit();
    }
}
