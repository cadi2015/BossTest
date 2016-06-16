package com.wp.bosstest.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wp.bosstest.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cadi on 2016/5/18.
 */
public class FragmentTool extends Fragment {
    private static final String TAG = "FragmentTool";
    private View mRootView;
    private Button mBtnSwit;
    private Button mBtnBt;
    private TextView mTvShow;
    private TextView mTvUiInfo;
    private TextView mTvDpInfo;
    private Button mBtnAd;
    private ProgressBar mProBarBt;
    private Button mBtnBrowserDownloadList;
    private Button mBtnMarketDownloadList;
    private String mRootPath;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_tools, container, false); //我太傻逼了，需要根View，不懂得把View整成实例变量，非要拿个局部变量在这里玩
        initViews();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated(View view, Bundle savedInstanceState)");
        mRootPath = Environment.getExternalStorageDirectory().getPath();
        mContext = getActivity();
        if (fileIsExists(mRootPath + File.separator + ".dlprovider" + File.separator + ".is_api_test")) {
            mTvShow.setText("测试");
        } else {
            mTvShow.setText("正式");
        }

        if (fileIsExists(mRootPath + File.separator + ".dlprovider" + File.separator + ".ad_show")) {
            mBtnAd.setText("一键删除.ad_show");
        } else {
            mBtnAd.setText("一键添加.ad_show");
        }

        StringBuilder uiSb = new StringBuilder();
        StringBuilder dpSb = new StringBuilder();
        PackageManager packageManager = mContext.getPackageManager();

        try {
            PackageInfo uiInfo = packageManager.getPackageInfo("com.android.providers.downloads.ui", PackageManager.GET_PERMISSIONS);
            PackageInfo dpInfo = packageManager.getPackageInfo("com.android.providers.downloads", PackageManager.GET_PERMISSIONS);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String uiLastTime = dateFormat.format(new Date(uiInfo.lastUpdateTime));
            uiSb.append("ui版本号：" + uiInfo.versionName + "\n");
            uiSb.append("versionCode: " + uiInfo.versionCode + "\n");
            uiSb.append("包名: " + uiInfo.packageName + "\n");
            uiSb.append("最后安装时间: " + uiLastTime + "\n");
            uiSb.append("进程名称：" + uiInfo.applicationInfo.processName + "\n");
            uiSb.append("应用数据路径：" + uiInfo.applicationInfo.dataDir + "\n");
            uiSb.append("安装apk路径：" + uiInfo.applicationInfo.publicSourceDir + "\n");
            mTvUiInfo.setText(uiSb.toString());

            String dpLastTime = dateFormat.format(new Date(dpInfo.lastUpdateTime));

            dpSb.append("dp版本号: " + dpInfo.versionName + "\n");
            dpSb.append("versionCode: " + dpInfo.versionCode + "\n");
            dpSb.append("包名：" + dpInfo.packageName + "\n");
            dpSb.append("最后安装时间: " + dpLastTime + "\n");
            dpSb.append("进程名称：" + dpInfo.applicationInfo.processName + "\n");
            dpSb.append("应用数据路径：" + dpInfo.applicationInfo.dataDir + "\n");
            dpSb.append("安装apk路径: " + dpInfo.applicationInfo.publicSourceDir +"\n");
            mTvDpInfo.setText(dpSb.toString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "找不到指定包的info信息");
        }


    }

    private boolean fileIsExists(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    private void initViews() {
        mBtnSwit = (Button) mRootView.findViewById(R.id.tool_btn_switch);
        mTvShow = (TextView) mRootView.findViewById(R.id.tool_tv_current_show);
        mBtnAd = (Button) mRootView.findViewById(R.id.tool_btn_ad);
        mBtnBt = (Button) mRootView.findViewById(R.id.tool_btn_bt);
        mBtnBrowserDownloadList = (Button) mRootView.findViewById(R.id.tool_btn_browser_download_list);
        mBtnMarketDownloadList = (Button) mRootView.findViewById(R.id.tool_btn_market_download_list);
        mTvUiInfo = (TextView) mRootView.findViewById(R.id.tool_tv_ui_info);
        mTvDpInfo = (TextView) mRootView.findViewById(R.id.tool_tv_dp_info);
        mProBarBt = (ProgressBar) mRootView.findViewById(R.id.tool_pro_bar_bt);
        View.OnClickListener myClick = new MyBtnClick();
        mBtnSwit.setOnClickListener(myClick);
        mBtnAd.setOnClickListener(myClick);
        mBtnBt.setOnClickListener(myClick);
        mBtnBrowserDownloadList.setOnClickListener(myClick);
        mBtnMarketDownloadList.setOnClickListener(myClick);
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
                            Toast.makeText(mContext, "切换正式环境成功", Toast.LENGTH_SHORT).show();
                            apiTestFile.delete();
                            mTvShow.setText("正式");
                        } else {
                            File is_api_test = new File(dlFile + File.separator + ".is_api_test");
                            if (!is_api_test.exists()) {
                                try {
                                    is_api_test.createNewFile();
                                    mTvShow.setText("测试");
                                    Toast.makeText(mContext, "切换测试环境成功", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    } else {
                        Toast.makeText(mContext, ".dlprovider目录不存在", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.tool_btn_ad:
                    Log.d(TAG, "click------tool_btn_ad-----");
                    if (fileIsExists(mRootPath + File.separator + ".dlprovider")) {
                        File ad_show = new File(mRootPath + File.separator + ".dlprovider" + File.separator + ".ad_show");
                        if (!ad_show.exists()) {
                            try {
                                if (ad_show.createNewFile()) {
                                    Toast.makeText(mContext, "创建成功", Toast.LENGTH_SHORT).show();
                                    mBtnAd.setText("一键删除.ad_show");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (ad_show.delete()) {
                                Toast.makeText(mContext, ".ad_show文件删除成功", Toast.LENGTH_SHORT).show();
                                mBtnAd.setText("一键添加.ad_show");
                            }
                        }
                    }
                    break;
                case R.id.tool_btn_bt:
                    mBtnBt.setClickable(false);
                    mBtnBt.setEnabled(false);
                    mProBarBt.setVisibility(View.VISIBLE);
                    new MyTask().execute(); //这里采用异步copy，不然ui线程直接anr了
                    break;
                case R.id.tool_btn_browser_download_list:
                    Intent broIntent = new Intent();
                    broIntent.setAction("android.intent.action.VIEW_DOWNLOADS_LIST");
                    startActivity(broIntent);
                    break;
                case R.id.tool_btn_market_download_list:
                    Intent  marIntent= new Intent();
                    marIntent.setAction("android.intent.action.VIEW_DOWNLOADS");
                    startActivity(marIntent);
                default:
                    break;
            }
        }
    }

    private  class MyTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object... params) {
            Log.d(TAG," ******** doInBackground (Object[] params)*********");
            copyBtToSdcard("测试bt种子文件");
            publishProgress();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.d(TAG, "********* onPostExecute(Object o)");
            mProBarBt.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "******** onProgressUpdate(Object[]) values) *********");
            Toast.makeText(mContext, "拷贝完成，文件在SD根目录", Toast.LENGTH_LONG).show();
            mBtnBt.setClickable(true);
            mBtnBt.setEnabled(true);
//            mProBarBt.setVisibility(View.VISIBLE);
        }
    }




    private void copyBtToSdcard(String dir) {
        AssetManager assetManager = mContext.getAssets();
        File btDir = new File(mRootPath + File.separator + dir);
        if (!btDir.exists()) {
            btDir.mkdir();
        }
        String[] fileNames = null;
        try {
            fileNames = assetManager.list("bt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String name : fileNames) {
            try {
                Log.d(TAG, "assets name = " + name);
                InputStream inputStream = assetManager.open("bt" + File.separator + name); //先把文件整成输入字节流
                File outFile = new File(btDir, name);
                byte[] bytes = new byte[1024]; //每次读取到内存1kb 先整个byte数组对象用来持有byte
                int length; //将输入字节流撸到byte数组对象（该数组的持有的每一个元素是byte）
                OutputStream outputStream = new FileOutputStream(outFile); //整个输出字节流到文件
                while ((length = inputStream.read(bytes)) > 0) { // 程序从输入字节流中读取到byte数组里,
                    outputStream.write(bytes, 0, length); //然后写byte数组里写入到输出字节流里，直接再到文件里
                }
                inputStream.close();
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
