package com.wp.cheez.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.wp.cheez.R;
import com.wp.cheez.config.SharedConstant;
import com.wp.cheez.utils.LogHelper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by cadi on 2016/8/11.
 */
public class FragmentMainDownloadManager extends Fragment {
    private final static String TAG = LogHelper.makeTag(FragmentMainDownloadManager.class);
    private final String KEY_TAB_ITEM = "download_tab_item";
    private String[] titles = {"工具", "任务", "性能", "设备"};
    private View mRootView;
    private ViewPager mViewPager;
    private TabLayout mTabPage;
    private SharedPreferences mSharedPreConfig;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        init(context);
    }

    public FragmentMainDownloadManager() {
        super();
        Log.d(TAG, "FragmentMainDownloadManager()");
    }

    private void init(Context context){
        mSharedPreConfig = context.getSharedPreferences(SharedConstant.SHARED_BOSS_CONFIG_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState(Bundle outState)");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView(......)");
        mRootView = inflater.inflate(R.layout.fragment_main_download_manager, null);
        setupViews();
        return mRootView;
    }


    private void setupViews(){
        mTabPage =  mRootView.findViewById(R.id.main_fragment_download_manager_indicator);
        mViewPager =  mRootView.findViewById(R.id.main_fragment_download_manager_pager);
        mViewPager.setAdapter(new CustomAdapter(getChildFragmentManager()));
        mTabPage.setupWithViewPager(mViewPager);
        mTabPage.addOnTabSelectedListener(new MyPageChangeLis());
    }


    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated(Bundle savedInstanceState)");
        mTabPage.getTabAt(mSharedPreConfig.getInt(KEY_TAB_ITEM, 0)).select();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated(View view, Bundle savedInstanceState)");
    }



    private class CustomAdapter extends FragmentPagerAdapter {

        public CustomAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getItem(int position), position = " + position);
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new FragmentTool();
                    break;
                case 1:
                    fragment = FragmentTask.newInstance();
                    break;
                case 2:
                    fragment = new FragmentPerformance();
                    break;
                case 3:
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
    }



    private class MyPageChangeLis implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            Log.d(TAG, "onPageSelected(int position) position = " + tab.getPosition());
            SharedPreferences.Editor editor = mSharedPreConfig.edit();
            editor.putInt(KEY_TAB_ITEM, tab.getPosition());
            editor.apply();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }
}
