package com.wp.bosstest.fragment;

import android.app.Fragment;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wp.bosstest.R;

/**
 * Created by cadi on 2016/11/3.
 */

public class FragmentAbout extends Fragment {
    private View mRootView;
    private ImageView mIvYouliao;
    private AnimationDrawable mAnimationDrawableYouliao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_about, null);
        init();
        setupViews();
        return mRootView;
    }

    private void init() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            mAnimationDrawableYouliao = (AnimationDrawable) getResources().getDrawable(R.drawable.frame_by_frame_youliao);
        } else {
            mAnimationDrawableYouliao = (AnimationDrawable) getResources().getDrawable(R.drawable.frame_by_frame_youliao, null);
        }
    }

    private void setupViews() {
        mIvYouliao = (ImageView) mRootView.findViewById(R.id.about_iv_youliao);
        mIvYouliao.setBackground(mAnimationDrawableYouliao);
        mAnimationDrawableYouliao.start();
    }
}
