package com.wp.bosstest.fragment;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wp.bosstest.R;
import com.wp.bosstest.utils.PackageUtil;

/**
 * Created by cadi on 2016/5/20.
 */
public class FragmentSplash extends Fragment {
    private View mRootView;
    private ImageView mIvAppIcon;
    private TextView mTvAppName;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void setupViews() {
        mIvAppIcon = (ImageView) mRootView.findViewById(R.id.splash_iv_app_icon);
        mTvAppName = (TextView) mRootView.findViewById(R.id.splash_tv_version);
        mTvAppName.setText(PackageUtil.getInstance(getContext()).getAppName(getContext().getPackageName()));
        mIvAppIcon.setBackground(PackageUtil.getInstance(getContext()).getAppIcon(getContext().getPackageName()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_splash, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
    }
}
