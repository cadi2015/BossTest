package com.wp.bosstest.fragment;

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

import com.wp.bosstest.R;
import com.wp.bosstest.config.FileConstant;
import com.wp.bosstest.utils.FileUtil;
import com.wp.bosstest.utils.PackageUtil;
import com.wp.bosstest.utils.SnackbarUtil;

/**
 * Created by cadi on 2017/4/5.
 */

public class FragmentShortVideo extends Fragment {

    private View mRootView;
    private Button mBtnTest;
    private Button mBtnPre;
    private Button mBtnOnline;
    private TextView mTvPackageInfo;
    private ImageView mIvAppIcon;

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
        MyBtnClickLis myBtnClickLis = new MyBtnClickLis();
        mBtnTest = (Button) mRootView.findViewById(R.id.main_fragment_short_video_btn_file_test);
        mBtnPre = (Button) mRootView.findViewById(R.id.main_fragment_short_video_btn_file_pre);
        mBtnOnline = (Button) mRootView.findViewById(R.id.main_fragment_short_video_btn_file_online);
        mTvPackageInfo = (TextView) mRootView.findViewById(R.id.tv_show_package);
        mIvAppIcon = (ImageView) mRootView.findViewById(R.id.iv_app_icon);
        mBtnTest.setOnClickListener(myBtnClickLis);
        mBtnPre.setOnClickListener(myBtnClickLis);
        mBtnOnline.setOnClickListener(myBtnClickLis);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PackageUtil packageUtil = PackageUtil.getInstance(getActivity());
        String packageStr = packageUtil.getPackageMessages(packageUtil.getPackageInfoDefault("cn.kuaipan.android"));
        Drawable appIcon = packageUtil.getAppIcon("cn.kuaipan.android");
        if (packageStr == null) {
            mTvPackageInfo.setText("无");
        } else {
            mTvPackageInfo.setText(packageStr);
        }
        if (appIcon != null) {
            mIvAppIcon.setBackground(appIcon);
        }
    }

    private class MyBtnClickLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_fragment_short_video_btn_file_test:
                    if (switchServer(FileConstant.SHORT_VIDEO_SERVER_DIR_PATH, FileConstant.SERVER_TEST_FILE_NAME)) {
                        FileUtil.removeFile(FileConstant.SHORT_VIDEO_SERVER_DIR_PATH, FileConstant.SERVER_PRE_FILE_NAME);
                        SnackbarUtil.createSnackBar(getContext(), mBtnTest, "成功切换测试环境").show();
                    }
                    break;
                case R.id.main_fragment_short_video_btn_file_pre:
                    if (switchServer(FileConstant.SHORT_VIDEO_SERVER_DIR_PATH, FileConstant.SERVER_PRE_FILE_NAME)) {
                        FileUtil.removeFile(FileConstant.SHORT_VIDEO_SERVER_DIR_PATH, FileConstant.SERVER_TEST_FILE_NAME);
                        SnackbarUtil.createSnackBar(getContext(), mRootView, "成功切换定版环境").show();
                    }
                    break;
                case R.id.main_fragment_short_video_btn_file_online:
                    boolean removeFileTest = FileUtil.removeFile(FileConstant.SHORT_VIDEO_SERVER_DIR_PATH, FileConstant.SERVER_TEST_FILE_NAME);
                    boolean removeFilePre = FileUtil.removeFile(FileConstant.SHORT_VIDEO_SERVER_DIR_PATH, FileConstant.SERVER_PRE_FILE_NAME);
                    if (removeFileTest || removeFilePre) {
                        SnackbarUtil.createSnackBar(getContext(), mRootView, "成功切换线上环境").show();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private boolean switchServer(String envPath, String fileName) {
        boolean successFile = false;
        if (!FileUtil.fileIsExists(envPath + fileName)) {
            successFile = FileUtil.createFile(envPath, fileName);
        }
        if (!FileUtil.fileIsExists(envPath + FileConstant.SERVER_LOG_FILE_NAME)) {
            FileUtil.createFile(envPath, FileConstant.SERVER_LOG_FILE_NAME);
        }
        return successFile;
    }

}
