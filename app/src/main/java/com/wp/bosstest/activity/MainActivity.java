package com.wp.bosstest.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;
import com.wp.bosstest.R;
import com.wp.bosstest.fragment.FragmentPhone;
import com.wp.bosstest.fragment.FragmentTask;
import com.wp.bosstest.fragment.FragmentTool;


/**
 * Created by wp on 2016/1/15.
 */
public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";
//    private TabPageIndicator mPageIndicator;
//    private ViewPager mViewPager;

    private String[] titles = {"任务", "工具", "设备"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabPageIndicator pageIndicator = (TabPageIndicator) findViewById(R.id.main_indicator);
        ViewPager viewPager = (ViewPager) findViewById(R.id.main_pager);

        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager()));

        pageIndicator.setViewPager(viewPager);
    }

    private class CustomAdapter extends FragmentPagerAdapter implements IconPagerAdapter { //FragmentPagerAdapter扩展自PagerAdapter   is a 的关系哦 亲

        public CustomAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getItem(int position), position = " + position);
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = FragmentTask.newInstance();
                    break;
                case 1:
                    fragment = new FragmentTool();
                    break;
                case 2:
                    fragment = new FragmentPhone();
                    break;
                default:
                    break;
            }
            return fragment;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public int getIconResId(int i) {
            Log.d(TAG, "getIconResId(int i), i = " + i);
            return R.mipmap.icon_app_store;
        }
    }
}
