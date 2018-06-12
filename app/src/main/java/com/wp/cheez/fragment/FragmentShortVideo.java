package com.wp.cheez.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wp.cheez.R;
import com.wp.cheez.config.FileConstant;
import com.wp.cheez.utils.FileUtil;
import com.wp.cheez.utils.PackageUtil;
import com.wp.cheez.utils.SnackbarUtil;

/**
 * Created by cadi on 2017/4/5.
 */

public class FragmentShortVideo extends Fragment {

    private View mRootView;
    private TextView mTvPackageInfo;
    private ImageView mIvAppIcon;
    private final String  PACKAGE_NAME = "com.cmcm.shorts";

    public FragmentShortVideo() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_main_short_video, null);
        setupViews();
        return mRootView;
    }

    private void setupViews() {
        mTvPackageInfo = (TextView) mRootView.findViewById(R.id.tv_show_package);
        mIvAppIcon = (ImageView) mRootView.findViewById(R.id.iv_app_icon);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PackageUtil packageUtil = PackageUtil.getInstance(getActivity());
        String packageStr = packageUtil.getPackageMessages(packageUtil.getPackageInfoDefault(PACKAGE_NAME));
        Drawable appIcon = packageUtil.getAppIcon(PACKAGE_NAME);
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
