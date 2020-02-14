package com.wp.cheez.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wp.cheez.R;
import com.wp.cheez.application.App;
import com.wp.cheez.config.AppConfig;
import com.wp.cheez.config.SharedConstant;
import com.wp.cheez.utils.PackageUtil;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class FragmentSecondApp extends Fragment {
    private AppConfig mAppConfig;
    private PackageUtil mPkgUtil;
    private TextView mTvPackageInfo;
    private ImageView mIvAppIcon;
    public FragmentSecondApp() {
        super();
        init();
    }
    private void init(){
        mAppConfig = AppConfig.getInstance(App.getAppContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        updateViews();
    }

    private void updateViews(){
        String packageStr = mPkgUtil.getPackageMessages(mPkgUtil.getPackageInfoDefault(mAppConfig.getStr(SharedConstant.SELECT_SECOND_APP_PKG_KEY,PackageUtil.CRUSH_PACKAGE_NAME)));
        mTvPackageInfo.setText(packageStr);
        Drawable appIcon = mPkgUtil.getAppIcon(mAppConfig.getStr(SharedConstant.SELECT_SECOND_APP_PKG_KEY,PackageUtil.CRUSH_PACKAGE_NAME));
        mIvAppIcon.setBackground(appIcon);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crush, null);
        mTvPackageInfo = (TextView) rootView.findViewById(R.id.tv_show_package);
        mIvAppIcon = (ImageView)rootView.findViewById(R.id.iv_app_icon);
        mPkgUtil = PackageUtil.getInstance(getActivity());
        String packageStr = mPkgUtil.getPackageMessages(mPkgUtil.getPackageInfoDefault(mAppConfig.getStr(SharedConstant.SELECT_SECOND_APP_PKG_KEY,PackageUtil.CRUSH_PACKAGE_NAME)));
        Drawable appIcon = mPkgUtil.getAppIcon(mAppConfig.getStr(SharedConstant.SELECT_SECOND_APP_PKG_KEY,PackageUtil.CRUSH_PACKAGE_NAME));

        if (packageStr == null) {
            mTvPackageInfo.setText("无");
        } else {
            mTvPackageInfo.setText(packageStr);
        }
        if (appIcon != null) {
            mIvAppIcon.setBackground(appIcon);
        }
        return rootView;
    }

}
