package com.wp.cheez.fragment;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wp.cheez.R;
import com.wp.cheez.config.FileConstant;
import com.wp.cheez.utils.LogHelper;
import com.wp.cheez.utils.PackageUtil;
import com.wp.cheez.utils.SpannableUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by cadi on 2016/8/11.
 */
public class FragmentMainFileExplorer extends Fragment {
    private final static String TAG = LogHelper.makeTag(FragmentMainFileExplorer.class);
    private View mRootView;
    private Context mContext;
    private Button mBtnFileTest;
    private ImageView mIvAppIcon;
    private Button mBtnFilePre;
    private Button mBtnFileOnline;
    private PackageUtil mPackageUtil;
    private PackageInfo mPackageInfo;
    private TextView mTvShowPackageMess;

    public FragmentMainFileExplorer() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_main_file_explorer, null);
        init();
        setupViews();
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setupViews() {
        mIvAppIcon = (ImageView) mRootView.findViewById(R.id.iv_app_icon);
        mBtnFileTest = (Button) mRootView.findViewById(R.id.main_fragment_file_explorer_btn_file_test);
        mBtnFilePre = (Button) mRootView.findViewById(R.id.main_fragment_file_explorer_btn_file_pre);
        mBtnFileOnline = (Button) mRootView.findViewById(R.id.main_fragment_file_explorer_btn_file_online);
        mTvShowPackageMess = (TextView) mRootView.findViewById(R.id.tv_show_package);
        if (mPackageInfo != null) {
            String filePackageInfoMessage = mPackageUtil.getPackageMessages(mPackageInfo);
            SpannableString fileSpan = SpannableUtils.setTextColorDefault(mContext, filePackageInfoMessage, filePackageInfoMessage.indexOf("5.)"), filePackageInfoMessage.indexOf("6.)"), R.color.colorRed);
            mTvShowPackageMess.setText(fileSpan);
            Drawable appIcon = mPackageUtil.getAppIcon(mPackageInfo.packageName);
            if (appIcon != null) {
                mIvAppIcon.setBackground(appIcon);
            }
        } else {
            mTvShowPackageMess.setText("无");
        }
        View.OnClickListener myClick = new MyBtnClick();
        mBtnFileTest.setOnClickListener(myClick);
        mBtnFilePre.setOnClickListener(myClick);
        mBtnFileOnline.setOnClickListener(myClick);
    }


    private void init() {
        mContext = getActivity();
        mPackageUtil = PackageUtil.getInstance(mContext);
        mPackageInfo = mPackageUtil.getPackageInfoDefault("com.android.fileexplorer");
        Log.d(TAG, "mPackageInfo = " + mPackageInfo);
    }

    private boolean createDic(String path, String name) {
        File file = new File(path, name);
        if (file.exists()) {
            return false;
        } else {
            file.mkdir();
            return true;
        }
    }

    private boolean createFile(String path, String name) {
        boolean createSuccess = false;
        File file = new File(path, name);
        if (file.exists()) {
            return createSuccess;
        } else {
            try {
                createSuccess = file.createNewFile();
                return createSuccess;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return createSuccess;
    }


    private boolean fileIsExists(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }


    private boolean removeFile(String path, String name) {
        boolean removeSuccess;
        File file = new File(path, name);
        if (fileIsExists(path + File.separator + name)) {
            removeSuccess = file.delete();
            return removeSuccess;
        } else {
            removeSuccess = false;
            return removeSuccess;
        }
    }

    private class MyBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_fragment_file_explorer_btn_file_test:
                    createDic(FileConstant.ROOT_PATH, FileConstant.FILE_EXPLORER_SERVER_DIR_NAME);
                    if (fileIsExists(FileConstant.FILE_EXPLORER_SERVER_PRE_PATH)) {
                        removeFile(FileConstant.FILE_EXPLORER_SERVER_DIR_PATH, FileConstant.SERVER_PRE_FILE_NAME);
                    }
                    createFile(FileConstant.FILE_EXPLORER_SERVER_DIR_PATH, FileConstant.SERVER_TEST_FILE_NAME);
                    createFile(FileConstant.FILE_EXPLORER_SERVER_DIR_PATH, FileConstant.SERVER_LOG_FILE_NAME);
                    createSnackBar(mBtnFileOnline, "切换Test环境成功").show();
                    break;
                case R.id.main_fragment_file_explorer_btn_file_pre:
                    createDic(FileConstant.ROOT_PATH, FileConstant.FILE_EXPLORER_SERVER_DIR_NAME);
                    if (fileIsExists(FileConstant.FILE_EXPLORER_SERVER_TEST_PATH)) {
                        removeFile(FileConstant.FILE_EXPLORER_SERVER_DIR_PATH, FileConstant.SERVER_TEST_FILE_NAME);
                    }
                    createFile(FileConstant.FILE_EXPLORER_SERVER_DIR_PATH, FileConstant.SERVER_PRE_FILE_NAME);
                    createFile(FileConstant.FILE_EXPLORER_SERVER_DIR_PATH, FileConstant.SERVER_LOG_FILE_NAME);
                    createSnackBar(mBtnFileOnline, "切换Pre环境成功").show();
                    break;
                case R.id.main_fragment_file_explorer_btn_file_online:
                    removeFile(FileConstant.FILE_EXPLORER_SERVER_DIR_PATH, FileConstant.SERVER_TEST_FILE_NAME);
                    removeFile(FileConstant.FILE_EXPLORER_SERVER_DIR_PATH, FileConstant.SERVER_PRE_FILE_NAME);
                    createSnackBar(mBtnFileOnline, "切换Online环境成功").show();
                    break;
                default:
                    break;
            }
        }
    }

    private Snackbar createSnackBar(View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        View viewGroup = snackbar.getView();
        TextView message = (TextView) viewGroup.findViewById(R.id.snackbar_text);
        viewGroup.setBackgroundColor(ActivityCompat.getColor(mContext, R.color.colorRed));
        message.setTextColor(ActivityCompat.getColor(mContext, R.color.color_guide_txt_white));
        snackbar.setActionTextColor(ActivityCompat.getColor(mContext, R.color.color_guide_txt_white));
        return snackbar;
    }
}