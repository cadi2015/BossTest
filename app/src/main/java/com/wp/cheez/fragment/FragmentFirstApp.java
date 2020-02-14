package com.wp.cheez.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wp.cheez.R;
import com.wp.cheez.application.App;
import com.wp.cheez.config.AppConfig;
import com.wp.cheez.config.SharedConstant;
import com.wp.cheez.utils.LogHelper;
import com.wp.cheez.utils.PackageUtil;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by cadi on 2017/4/5.
 */

public class FragmentFirstApp extends Fragment {
    private String TAG = LogHelper.makeTag(FragmentFirstApp.class);
    private View mRootView;
    private TextView mTvPackageInfo;
    private ImageView mIvAppIcon;
    private AppConfig mAppConfig;
    private PackageUtil mPkgUtil;

    public FragmentFirstApp() {
        super();
        init();
    }

    private void init(){
        mAppConfig = AppConfig.getInstance(App.getAppContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_main_short_video, null);
        setupViews();
        Log.d(TAG, "onCreateView(^^^^^)");
        return mRootView;
    }

    private void setupViews() {
        mTvPackageInfo = (TextView) mRootView.findViewById(R.id.tv_show_package);
        mIvAppIcon = (ImageView) mRootView.findViewById(R.id.iv_app_icon);
    }


    @Override
    public void onStart() {
        super.onStart();
        updateViews();
    }

    private void updateViews(){
        String packageStr = mPkgUtil.getPackageMessages(mPkgUtil.getPackageInfoDefault(mAppConfig.getStr(SharedConstant.SELECT_FRIST_APP_PKG_KEY,PackageUtil.SHORT_VIDEO_PACKAGE_NAME)));
        mTvPackageInfo.setText(packageStr);
        Drawable appIcon = mPkgUtil.getAppIcon(mAppConfig.getStr(SharedConstant.SELECT_FRIST_APP_PKG_KEY,PackageUtil.SHORT_VIDEO_PACKAGE_NAME));
        mIvAppIcon.setBackground(appIcon);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPkgUtil= PackageUtil.getInstance(getActivity());
        String packageStr = mPkgUtil.getPackageMessages(mPkgUtil.getPackageInfoDefault(mAppConfig.getStr(SharedConstant.SELECT_FRIST_APP_PKG_KEY,PackageUtil.SHORT_VIDEO_PACKAGE_NAME)));
        Drawable appIcon = mPkgUtil.getAppIcon(mAppConfig.getStr(SharedConstant.SELECT_FRIST_APP_PKG_KEY,PackageUtil.SHORT_VIDEO_PACKAGE_NAME));
        if (packageStr == null) {
            mTvPackageInfo.setText("无");
        } else {
            mTvPackageInfo.setText(packageStr);
        }
        if (appIcon != null) {
            mIvAppIcon.setBackground(appIcon);
        }
    }


}
