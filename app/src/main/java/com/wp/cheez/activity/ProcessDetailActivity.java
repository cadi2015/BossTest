package com.wp.cheez.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

import com.wp.cheez.R;
import com.wp.cheez.fragment.FragmentProcessDetail;
import com.wp.cheez.utils.LogHelper;

/**
 * Created by cadi on 2016/9/22.
 */

public class ProcessDetailActivity extends BaseActivity {
    private static final String TAG  = LogHelper.makeTag(ProcessDetailActivity.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new FragmentProcessDetail();
        fragmentManager.beginTransaction().add(R.id.process_detail_frame_layout, fragment).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionResult()");
    }

    @Override
    public boolean isDisplayHomeAsUp() {
        return true;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_process_detail;
    }

    @Override
    public boolean isDisplayToolBar() {
        return true;
    }

    @Override
    public String getToolBarTitle() {
        return "进程详情";
    }

}
