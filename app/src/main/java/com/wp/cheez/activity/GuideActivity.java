package com.wp.cheez.activity;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wp.cheez.R;
import com.wp.cheez.utils.AppInfo;
import com.wp.cheez.config.SharedConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wp on 2016/2/18.
 */
public class GuideActivity extends FragmentActivity {
    private static final String TAG = "GuideActivity";
    private ViewPager mViewPager;
    private ImageView[] mBg;
    List<View> mViews;
    private int mCurrentPageIndex = 0;
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
    private LinearLayout mLlDot;

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

        private void updateDot(int position) {
            for (int index = 0; index < mViews.size(); index++) {
                if (position == index) {
                    mLlDot.getChildAt(position).setBackgroundResource(R.drawable.guide_dot_selected);
                    continue;
                }
                mLlDot.getChildAt(index).setBackgroundResource(R.drawable.guide_dot_normal);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.d(TAG, "onPageScrolled(int position, float positionOffset, int positionOffsetPixels)");

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
            updateDot(position);
            mCurrentPageIndex = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.d(TAG, "onPageScrollStateChanged(int state)");
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
        mLlDot = (LinearLayout) findViewById(R.id.guide_ll_dot);
        mViews = new ArrayList<>();
        for (int i = 0; i < mBg.length; i++) {
            mBg[i] = new ImageView(this);
            mBg[i].setImageResource(imageIds[i]);
            mViews.add(mBg[i]);
        }
        mViewPager.setAdapter(new MyPageAdapter(mViews));
        LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(25, 25);
        dotParams.leftMargin = 15;
        for (int index = 0; index < mViews.size(); index++) {
            View view = new View(this);
            view.setLayoutParams(dotParams);
            if (index == 0) {
                view.setBackgroundResource(R.drawable.guide_dot_selected);
            } else {
                view.setBackgroundResource(R.drawable.guide_dot_normal);
            }
            mLlDot.addView(view);
        }
        MyIvClickLis myIvClickLis = new MyIvClickLis();
        mIvPrev.setOnClickListener(myIvClickLis);
        mIvNext.setOnClickListener(myIvClickLis);
    }

    private class MyIvClickLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "MyIvClickLis onClick(View v) mCurrentPageIndex = " + mCurrentPageIndex);
            switch (v.getId()) {
                case R.id.guide_iv_prev:
                    mViewPager.setCurrentItem(mCurrentPageIndex - 1, true);
                    break;
                case R.id.guide_iv_next:
                    mViewPager.setCurrentItem(mCurrentPageIndex + 1, true);
                    break;
                default:
                    break;
            }
        }
    }

    private class MyClickLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SharedPreferences sharedPreferences = getSharedPreferences(SharedConstant.SHARED_BOSS_CONFIG_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(SharedConstant.GUIDE_KEY_IS_FIRST, false);
            editor.putInt(SharedConstant.GUIDE_KEY_VERSION_CODE, AppInfo.getVersionCode(getApplicationContext()));
            editor.apply(); //apply 替代了 commit
            Intent intent = getIntent();
            String extraStr = intent.getStringExtra("from");
            if (intent != null && extraStr != null && extraStr.equals("Splash")) {
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
            }
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
