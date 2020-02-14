package com.wp.cheez.fragment;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wp.cheez.R;
import com.wp.cheez.config.AppConfig;
import com.wp.cheez.config.SharedConstant;
import com.wp.cheez.utils.PackageUtil;

/**
 * Created by cadi on 2017/4/17.
 */

public class FragmentInstallAppDetail extends Fragment {
    private View mRootView;
    private TextView mTvShowPackageMes;
    private ImageView mIvAppIc;
    private Button mBtnSetAppAtHome;
    private String mPkgName;
    private static int mKeyFlag = 0; //不用静态变量的话，生命周期太短暂，静态变量的生命周期是随着进程的，所以一直可以++
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_install_app_detail, null);
        setupViews();
        Bundle bundle = getArguments();
        mPkgName = bundle.getString("packageName");
        PackageUtil packageUtil = PackageUtil.getInstance(getContext());
        PackageInfo packageInfo = packageUtil.getPackageInfoDefault(mPkgName);
        String packageMessages = packageUtil.getPackageMessages(packageInfo);
        Drawable appIc = packageUtil.getAppIcon(mPkgName);
        mTvShowPackageMes.setText(packageMessages);
        mIvAppIc.setBackground(appIc);
        return mRootView;
    }

    private void setupViews() {
        mTvShowPackageMes = (TextView) mRootView.findViewById(R.id.tv_show_package);
        mIvAppIc = (ImageView) mRootView.findViewById(R.id.iv_app_icon);
        mBtnSetAppAtHome = (Button)mRootView.findViewById(R.id.btn_set_app_at_home);
        Log.e("Tyson", "mKeyFlag == " + mKeyFlag);
        String btnTxtIndex = mKeyFlag % 2 == 0 ? "1":"2";
        String btnTxtBase = getResources().getString(R.string.btn_set_app_to_home);
        String btnTxtFInal = String.format(btnTxtBase,btnTxtIndex);
        mBtnSetAppAtHome.setText(btnTxtFInal);
        mBtnSetAppAtHome.setOnClickListener(new View.OnClickListener() {
            String key;
            @Override
            public void onClick(View v) {
                AppConfig appConfig = AppConfig.getInstance(getContext().getApplicationContext());
                if(mKeyFlag % 2 == 0) {
                    key = SharedConstant.SELECT_FRIST_APP_PKG_KEY;
                    Toast.makeText(getContext().getApplicationContext(),"第1个Tab设置成功",Toast.LENGTH_SHORT).show();
                } else {
                    key = SharedConstant.SELECT_SECOND_APP_PKG_KEY;
                    Toast.makeText(getContext().getApplicationContext(),"第2个Tab设置成功",Toast.LENGTH_SHORT).show();
                }
                appConfig.put(key, mPkgName);
                mKeyFlag++;
                getActivity().finish();
            }
        });
    }
}
