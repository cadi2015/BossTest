package com.wp.cheez.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.wp.cheez.R;
import com.wp.cheez.fragment.FragmentTaskDetail;

/**
 * Created by cadi on 2016/6/21.
 */
public class TaskDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.task_detail_frame_layout, new FragmentTaskDetail());
        fragmentTransaction.commit();
    }

    @Override
    public boolean isDisplayHomeAsUp() {
        return true;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_task_detail;
    }

    @Override
    public boolean isDisplayToolBar() {
        return true;
    }

    @Override
    public String getToolBarTitle() {
        return "任务详情";
    }
}
