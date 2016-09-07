package com.wp.bosstest.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wp.bosstest.R;
import com.wp.bosstest.utils.AppInfo;
import com.wp.bosstest.config.SharedConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wp on 2016/2/18.
 */
public class GuideActivity extends FragmentActivity {
    private static final String TAG = "GuideActivity";
    private ViewPager mViewPager;
    private ImageView[] mBg;
    private int[] imageIds = {
            R.drawable.introduction_1_bg,
            R.drawable.introduction_2_bg,
            R.drawable.introduction_3_bg,
            R.drawable.introduction_4_bg
    };
    private TextView mTvShow;
    private Button mBtnStart;
    private ImageView mIvPrev;
    private ImageView mIvNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        setupViews();
        init();
    }

    private void init() {
        mViewPager.addOnPageChangeListener(new MyOnPageLis());
    }

    private class MyOnPageLis implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            Log.d(TAG, "onPageSelected(int position), position = " + position);
            switch (position) {
                case 0:
                    mTvShow.setText(R.string.guide_page_0);
                    mIvPrev.setVisibility(View.GONE);
                    break;
                case 1:
                    mTvShow.setText(R.string.guide_page_1);
                    mIvPrev.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    mTvShow.setText(R.string.guide_page_2);
                    mIvNext.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    mTvShow.setText(R.string.guide_page_3);
                    mBtnStart.setVisibility(View.VISIBLE);
                    mIvNext.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    private void setupViews() {
        mViewPager = (ViewPager) findViewById(R.id.guide_view_pager);
        mTvShow = (TextView) findViewById(R.id.guide_tv_show);
        mBtnStart = (Button) findViewById(R.id.guide_btn_start);
        mBtnStart.setOnClickListener(new MyClickLis());
        mBg = new ImageView[imageIds.length];
        mIvPrev = (ImageView) findViewById(R.id.guide_iv_prev);
        mIvNext = (ImageView) findViewById(R.id.guide_iv_next);
        List<View> mViews = new ArrayList<>();
        for (int i = 0; i < mBg.length; i++) {
            mBg[i] = new ImageView(this);
            mBg[i].setImageResource(imageIds[i]);
            mViews.add(mBg[i]);
        }
        mViewPager.setAdapter(new MyPageAdapter(mViews));
    }

    private class MyClickLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SharedPreferences sharedPreferences = getSharedPreferences(SharedConstant.SHARED_PRE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putBoolean(SharedConstant.GUIDE_KEY_IS_FIRST, false);
            editor.putInt(SharedConstant.GUIDE_KEY_VERSION_CODE, AppInfo.getVersionCode(getApplicationContext()));
            editor.commit();
            startActivity(new Intent(GuideActivity.this, MainActivity.class));
            GuideActivity.this.finish();
        }
    }

    private class MyPageAdapter extends PagerAdapter {
        private List<View> mMyViews;

        public MyPageAdapter(List<View> mMyViews) {
            this.mMyViews = mMyViews;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return mMyViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mMyViews.get(position));
            return mMyViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mMyViews.get(position));
        }
    }
}
