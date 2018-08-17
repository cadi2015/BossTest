package com.wp.cheez.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wp.cheez.R;
import com.wp.cheez.utils.PackageUtil;


public class FragmentCrush extends Fragment{


    public FragmentCrush() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crush, null);
        TextView showPackageInfo = (TextView) rootView.findViewById(R.id.tv_show_package);
        ImageView showAppIcon = (ImageView)rootView.findViewById(R.id.iv_app_icon);
        PackageUtil packageUtil = PackageUtil.getInstance(getActivity());
        String packageStr = packageUtil.getPackageMessages(packageUtil.getPackageInfoDefault(PackageUtil.CRUSH_PACKAGE_NAME));
        Drawable appIcon = packageUtil.getAppIcon(PackageUtil.CRUSH_PACKAGE_NAME);
        if (packageStr == null) {
            showPackageInfo.setText("无");
        } else {
            showPackageInfo.setText(packageStr);
        }
        if (appIcon != null) {
            showAppIcon.setBackground(appIcon);
        }
        return rootView;
    }

}
