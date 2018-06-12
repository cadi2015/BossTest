package com.wp.cheez.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;
import com.wp.cheez.R;
import com.wp.cheez.config.SharedConstant;
import com.wp.cheez.utils.LogHelper;

/**
 * Created by cadi on 2016/8/11.
 */
public class FragmentMainDownloadManager extends Fragment {
    private final static String TAG = LogHelper.makeTag(FragmentMainDownloadManager.class);
    private final String KEY_TAB_ITEM = "download_tab_item";
    private String[] titles = {"工具", "任务", "性能", "设备"};
    private View mRootView;
    private ViewPager mViewPager;
    private TabPageIndicator mTabPage;
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
        mTabPage = (TabPageIndicator) mRootView.findViewById(R.id.main_fragment_download_manager_indicator);
        mViewPager = (ViewPager) mRootView.findViewById(R.id.main_fragment_download_manager_pager);
        mViewPager.setAdapter(new CustomAdapter(getChildFragmentManager()));
        mTabPage.setOnPageChangeListener(new MyPageChangeLis());
        mTabPage.setViewPager(mViewPager);
    }


    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated(Bundle savedInstanceState)");
        mTabPage.setCurrentItem(mSharedPreConfig.getInt(KEY_TAB_ITEM, 0));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated(View view, Bundle savedInstanceState)");
    }



    private class CustomAdapter extends FragmentPagerAdapter implements IconPagerAdapter { //FragmentPagerAdapter扩展自PagerAdapter   is a 的关系哦 亲

        public CustomAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            Log.d(TAG, "getItem(int position), position = " + position);
            android.support.v4.app.Fragment fragment = null;
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

        @Override
        public int getIconResId(int i) {
            Log.d(TAG, "getIconResId(int i), i = " + i);
            switch (i) {
                case 0:
                    return R.drawable.tab_tool;
                case 1:
                    return R.drawable.tab_download;
                case 2:
                    return R.drawable.tab_performance;
                case 3:
                    return R.drawable.tab_phone;
                default:
                    break;
            }
            return -1;
        }
    }



    private class MyPageChangeLis implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.d(TAG, "onPageSelected(int position) position = " + position);
            SharedPreferences.Editor editor = mSharedPreConfig.edit();
            editor.putInt(KEY_TAB_ITEM, position);
            editor.apply();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
