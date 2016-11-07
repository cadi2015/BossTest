package com.wp.bosstest.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import com.wp.bosstest.R;
import com.wp.bosstest.fragment.FragmentAbout;

/**
 * Created by cadi on 2016/8/10.
 */
public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.about_frame_layout, new FragmentAbout()).commit();
    }
}
