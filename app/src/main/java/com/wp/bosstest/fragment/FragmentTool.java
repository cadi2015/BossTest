package com.wp.bosstest.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.wp.bosstest.R;

import java.io.File;

/**
 * Created by cadi on 2016/5/18.
 */
public class FragmentTool extends Fragment {
    private static final String TAG = "FragmentTool";
    private View mRootView;
    private Button mBtnSwit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_tools, container, false); //我太傻逼了，需要根View，不懂得把View整成实例变量，非要拿个局部变量在这里玩
        initViews();
        return mRootView;
    }


    private void initViews() {
        mBtnSwit = (Button) mRootView.findViewById(R.id.tool_btn_switch);
        mBtnSwit.setOnClickListener(new MyBtnClick());
    }

    private class MyBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String rootPath = Environment.getExternalStorageDirectory().getPath();
            Log.d(TAG, "rootPath = " + rootPath);
            String logName = ".is_api_test";
            File dlFile = new File(rootPath + File.separator + ".dlprovider");
            Log.d(TAG, ".dlProvider = " + dlFile);
            if(dlFile.isDirectory() && dlFile.exists()) {
                for(File f :dlFile.listFiles()) {
                    if(f.getName().equals(logName) ){
                        mBtnSwit.setText("切换正式环境");
                        break;
                    }
                }
            } else {
                Toast.makeText(getActivity(), ".dlprovider目录不存在", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
