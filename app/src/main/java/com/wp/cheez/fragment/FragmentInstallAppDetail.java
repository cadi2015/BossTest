package com.wp.cheez.fragment;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wp.cheez.R;
import com.wp.cheez.utils.PackageUtil;

/**
 * Created by cadi on 2017/4/17.
 */

public class FragmentInstallAppDetail extends Fragment {
    private View mRootView;
    private TextView mTvShowPackageMes;
    private ImageView mIvAppIc;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_install_app_detail, null);
        setupViews();
        Bundle bundle = getArguments();
        String packageNameVal = bundle.getString("packageName");
        PackageUtil packageUtil = PackageUtil.getInstance(getContext());
        PackageInfo packageInfo = packageUtil.getPackageInfoDefault(packageNameVal);
        String packageMessages = packageUtil.getPackageMessages(packageInfo);
        Drawable appIc = packageUtil.getAppIcon(packageNameVal);
        mTvShowPackageMes.setText(packageMessages);
        mIvAppIc.setBackground(appIc);
        return mRootView;
    }

    private void setupViews() {
        mTvShowPackageMes = (TextView) mRootView.findViewById(R.id.tv_show_package);
        mIvAppIc = (ImageView) mRootView.findViewById(R.id.iv_app_icon);
    }
}
