package com.wp.bosstest.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wp.bosstest.R;
import com.wp.bosstest.utils.LogHelper;
import com.wp.bosstest.utils.PackageUtil;

/**
 * Created by cadi on 2016/11/3.
 */

public class FragmentAbout extends Fragment {
    private static final String TAG = LogHelper.makeTag(FragmentAbout.class);
    private View mRootView;
    private ImageView mIvAppIcon;
    private TextView mTvAppName;
    private TextView mTvAppVersion;
    private Activity mHostActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_about, null);
        init();
        setupViews();
        return mRootView;
    }

    private void init() {
        mHostActivity = getActivity();
    }

    private void setupViews() {
        PackageManager pm = mHostActivity.getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(mHostActivity.getPackageName(),PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "not found the package name");
        }
        mIvAppIcon = (ImageView) mRootView.findViewById(R.id.about_iv_app_icon);
        mTvAppName = (TextView) mRootView.findViewById(R.id.about_tv_app_name);
        mTvAppVersion = (TextView) mRootView.findViewById(R.id.about_tv_app_version);
        mTvAppName.setText(info.applicationInfo.loadLabel(pm));
        mTvAppVersion.setText(info.versionName);
        mIvAppIcon.setBackground(info.applicationInfo.loadIcon(pm));
    }
}
