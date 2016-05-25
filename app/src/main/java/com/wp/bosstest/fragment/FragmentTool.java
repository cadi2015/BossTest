package com.wp.bosstest.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wp.bosstest.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by cadi on 2016/5/18.
 */
public class FragmentTool extends Fragment {
    private static final String TAG = "FragmentTool";
    private View mRootView;
    private Button mBtnSwit;
    private TextView mTvShow;
    private Button mBtnAd;
    private String mRootPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_tools, container, false); //我太傻逼了，需要根View，不懂得把View整成实例变量，非要拿个局部变量在这里玩
        initViews();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRootPath = Environment.getExternalStorageDirectory().getPath();
        if (fileIsExists(mRootPath + File.separator + ".dlprovider" + File.separator + ".is_api_test")) {
            mTvShow.setText("测试环境");
        } else {
            mTvShow.setText("正式环境");
        }
    }

    private boolean fileIsExists(String path) {
        File file = new File(path);
        if(file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    private void initViews() {
        mBtnSwit = (Button) mRootView.findViewById(R.id.tool_btn_switch);
        mTvShow = (TextView) mRootView.findViewById(R.id.tool_tv_current_show);
        mBtnAd = (Button) mRootView.findViewById(R.id.tool_btn_ad);
        View.OnClickListener myClick = new MyBtnClick();
        mBtnSwit.setOnClickListener(myClick);
        mBtnAd.setOnClickListener(myClick);
    }

    private class MyBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.tool_btn_switch:
                    Log.d(TAG, "rootPath = " + mRootPath);
                    String logName = ".is_api_test";
                    File dlFile = new File(mRootPath + File.separator + ".dlprovider");
                    Log.d(TAG, ".dlprovider = " + dlFile);
                    boolean haveTestApiFile = false;
                    if (dlFile.isDirectory() && dlFile.exists()) {
                        File apiTestFile = null;
                        for (File f : dlFile.listFiles()) {
                            if (f.getName().equals(logName)) {
                                haveTestApiFile = true;
                                apiTestFile = f;
                                break;
                            }
                        }
                        if (haveTestApiFile) {
                            Toast.makeText(getActivity(), "切换正式环境成功", Toast.LENGTH_SHORT).show();
                            apiTestFile.delete();
                            mTvShow.setText("正式环境");
                        } else {
                            File is_api_test = new File(dlFile + File.separator + ".is_api_test");
                            if (!is_api_test.exists()) {
                                try {
                                    is_api_test.createNewFile();
                                    mTvShow.setText("测试环境");
                                    Toast.makeText(getActivity(), "切换测试环境成功", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    } else {
                        Toast.makeText(getActivity(), ".dlprovider目录不存在", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.tool_btn_ad:
                    Log.d(TAG, "click------tool_btn_ad-----");
                    if(fileIsExists(mRootPath + File.separator + ".dlprovider")) {
                        File ad_show = new File(mRootPath + File.separator + ".dlprovider" + File.separator + ".ad_show");
                        if(!ad_show.exists()) {
                            try {
                                ad_show.createNewFile();
                                Toast.makeText(getActivity(), "创建成功", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getActivity(), ".ad_show文件已经创建", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
