package com.wp.bosstest.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.wp.bosstest.R;
import com.wp.bosstest.fragment.FragmentProcessDetail;

/**
 * Created by cadi on 2016/9/22.
 */

public class ProcessDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_detail);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new FragmentProcessDetail();
        fragmentManager.beginTransaction().add(R.id.process_detail_frame_layout, fragment).commit();
    }

}
