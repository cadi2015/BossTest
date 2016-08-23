package com.wp.bosstest.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import com.wp.bosstest.R;
import com.wp.bosstest.fragment.FragmentWeb;

/**
 * Created by cadi on 2016/8/23.
 */
public class WebActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.web_frame_layout, new FragmentWeb()).commit();
    }

}
