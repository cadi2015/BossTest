package com.wp.bosstest.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wp.bosstest.R;
import com.wp.bosstest.utils.PackageUtil;
import com.wp.bosstest.utils.SpannableUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by cadi on 2016/5/18.
 */
public class FragmentTool extends Fragment {
    private static final String TAG = "FragmentTool";
    private static final String SERVICE_TEST_NAME = ".is_api_test";
    private static final String SERVICE_PRE_NAME = ".is_api_pre";
    private static final String SERVICE_DIR_NAME = ".dlprovider";
    private View mRootView;
    private Button mBtnSwitch;
    private Button mBtnBt;
    private TextView mTvShow;
    private TextView mTvUiInfo;
    private TextView mTvDpInfo;
    private Button mBtnAd;
    private ProgressBar mProBarBt;
    private Button mBtnBrowserDownloadList;
    private Button mBtnMarketDownloadList;
    private Button mBtnSwitchMarketPre;
    private Button mBtnMakeLog;
    private Button mBtnSlogConfig;
    private String mRootPath;
    private Context mContext;
    private String mService_test;
    private String mService_pre;
    private String mService_online;
    private boolean isServiceTest = false;
    private boolean isServicePre = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate");
        mRootView = inflater.inflate(R.layout.fragment_tools, container, false); //我太傻逼了，需要根View，不懂得把View整成实例变量，非要拿个局部变量在这里玩
        initViews();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated(View view, Bundle savedInstanceState)");
        super.onViewCreated(view, savedInstanceState);
        mRootPath = Environment.getExternalStorageDirectory().getPath();
        Log.d(TAG, "mRootPath = " + mRootPath);
        mContext = getActivity();
        mService_test = getString(R.string.btn_switch_production_test);
        mService_pre = getString(R.string.btn_switch_production_pre);
        mService_online = getString(R.string.btn_switch_production_online);
        String serviceTestPath = mRootPath + File.separator + SERVICE_DIR_NAME + File.separator + SERVICE_TEST_NAME;
        String servicePrePath = mRootPath + File.separator + SERVICE_DIR_NAME + File.separator + SERVICE_PRE_NAME;
        if (fileIsExists(serviceTestPath)) {
            isServiceTest = true;
            mTvShow.setText(mService_test);
        } else if (fileIsExists(servicePrePath)) {
            isServicePre = true;
            mTvShow.setText(mService_pre);
        } else {
            mTvShow.setText(mService_online);
        }

        if (fileIsExists(mRootPath + File.separator + SERVICE_DIR_NAME + File.separator + ".ad_show")) {
            mBtnAd.setText("删除.ad_show");
        } else {
            mBtnAd.setText("添加.ad_show");
        }

        if (fileIsExists(mRootPath + File.separator + "market_staging")) {
            mBtnSwitchMarketPre.setText("应用商店自升级删除preView环境");
        }

        PackageManager packageManager = mContext.getPackageManager();

        try {
            PackageInfo uiInfo = packageManager.getPackageInfo(PackageUtil.UiPackageName, PackageManager.GET_PERMISSIONS);
            PackageInfo dpInfo = packageManager.getPackageInfo(PackageUtil.DpPackageName, PackageManager.GET_PERMISSIONS);
            PackageUtil packageUtil = PackageUtil.getInstance(mContext);
            String uiStr = packageUtil.getPackageMessages(uiInfo);
            String dpStr = packageUtil.getPackageMessages(dpInfo);
            SpannableString uiSpan = SpannableUtils.setTextColorDefault(mContext, uiStr, uiStr.indexOf("5.)"), uiStr.indexOf("6.)"), R.color.colorRed);
            SpannableString dpSpan = SpannableUtils.setTextColorDefault(mContext, dpStr, dpStr.indexOf("5.)"), dpStr.indexOf("6.)"), R.color.colorRed);
            mTvUiInfo.setText(uiSpan);
            mTvDpInfo.setText(dpSpan);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "找不到指定包的info信息");
            mTvUiInfo.setText("未安装");
            mTvDpInfo.setText("未安装");
        }
        showAlertDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    private void showAlertDialog() {

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
        mBtnSwitch = (Button) mRootView.findViewById(R.id.tool_btn_switch);
        mTvShow = (TextView) mRootView.findViewById(R.id.tool_tv_current_show);
        mBtnAd = (Button) mRootView.findViewById(R.id.tool_btn_ad);
        mBtnBt = (Button) mRootView.findViewById(R.id.tool_btn_bt);
        mBtnBrowserDownloadList = (Button) mRootView.findViewById(R.id.tool_btn_browser_download_list);
        mBtnMarketDownloadList = (Button) mRootView.findViewById(R.id.tool_btn_market_download_list);
        mBtnSlogConfig = (Button) mRootView.findViewById(R.id.tool_btn_slog_config);
        mTvUiInfo = (TextView) mRootView.findViewById(R.id.tool_tv_ui_info);
        mTvDpInfo = (TextView) mRootView.findViewById(R.id.tool_tv_dp_info);
        mProBarBt = (ProgressBar) mRootView.findViewById(R.id.tool_pro_bar_bt);
        mBtnSwitchMarketPre = (Button) mRootView.findViewById(R.id.tool_btn_market_switch_pre);
        mBtnMakeLog = (Button) mRootView.findViewById(R.id.tool_btn_make_log);
        View.OnClickListener myClick = new MyBtnClick();
        mBtnSwitch.setOnClickListener(myClick);
        mBtnAd.setOnClickListener(myClick);
        mBtnBt.setOnClickListener(myClick);
        mBtnBrowserDownloadList.setOnClickListener(myClick);
        mBtnMarketDownloadList.setOnClickListener(myClick);
        mBtnSwitchMarketPre.setOnClickListener(myClick);
        mBtnMakeLog.setOnClickListener(myClick);
        mBtnSlogConfig.setOnClickListener(myClick);
    }

    private enum ServiceApi {
        Test, Pre, Online
    }

    private ServiceApi getCurrentServiceApi() {
        ServiceApi temp;
        if (isServiceTest) {
            temp = ServiceApi.Test;
            return temp;
        } else if (isServicePre) {
            temp = ServiceApi.Pre;
            return temp;
        } else {
            temp = ServiceApi.Online;
            return temp;
        }
    }

    private class MyBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tool_btn_switch:
                    ServiceApi currentApi = getCurrentServiceApi();
                    Log.d(TAG, "rootPath = " + mRootPath);
                    File dlFile = new File(mRootPath + File.separator + SERVICE_DIR_NAME);
                    Log.d(TAG, ".dlprovider = " + dlFile);
                    if (dlFile.isDirectory() && dlFile.exists()) {
                        if (currentApi == ServiceApi.Test) {
                            if (createFile(dlFile.getPath(), SERVICE_PRE_NAME)) {
                                makeSnackBar(mBtnSwitch, "切换Pre环境成功").show();
                                isServicePre = true;
                                mTvShow.setText(mService_pre);
                                removeFile(dlFile.getPath(), SERVICE_TEST_NAME);
                                isServiceTest = false;
                            }
                        } else if (currentApi == ServiceApi.Pre) {
                            if (removeFile(dlFile.getPath(), SERVICE_PRE_NAME)) {
                                makeSnackBar(mBtnSwitch, "切换Online环境成功").show();
                                isServicePre = false;
                                isServiceTest = false;
                                mTvShow.setText(mService_online);
                            }
                        } else if (currentApi == ServiceApi.Online) {
                            if (createFile(dlFile.getPath(), SERVICE_TEST_NAME)) {
                                makeSnackBar(mBtnSwitch, "切换Test环境成功").show();
                                isServiceTest = true;
                                mTvShow.setText(mService_test);
                            }

                        }
                    } else {
                        Toast.makeText(mContext, ".dlprovider目录不存在", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.tool_btn_ad:
                    Log.d(TAG, "click------tool_btn_ad-----");
                    if (fileIsExists(mRootPath + File.separator + SERVICE_DIR_NAME)) {
                        File ad_show = new File(mRootPath + File.separator + SERVICE_DIR_NAME + File.separator + ".ad_show");
                        if (!ad_show.exists()) {
                            try {
                                if (ad_show.createNewFile()) {
                                    Toast.makeText(mContext, "创建成功", Toast.LENGTH_SHORT).show();
                                    mBtnAd.setText("删除.ad_show");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (ad_show.delete()) {
                                Toast.makeText(mContext, ".ad_show文件删除成功", Toast.LENGTH_SHORT).show();
                                mBtnAd.setText("添加.ad_show");
                            }
                        }
                    }
                    break;
                case R.id.tool_btn_bt:
                    mBtnBt.setClickable(false);
                    mBtnBt.setEnabled(false);
                    mProBarBt.setVisibility(View.VISIBLE);
                    new MyTask().execute(); //这里采用非ui线程进行copy行为，不然ui线程直接anr了
                    break;
                case R.id.tool_btn_browser_download_list:
                    Intent broIntent = new Intent();
                    broIntent.setAction("android.intent.action.VIEW_DOWNLOADS_LIST");
                    startActivity(broIntent);
                    break;
                case R.id.tool_btn_market_download_list:
                    Intent marIntent = new Intent();
                    marIntent.setAction("android.intent.action.VIEW_DOWNLOADS");
                    startActivity(marIntent);
                    break;
                case R.id.tool_btn_market_switch_pre:
                    if (createFile(mRootPath, "market_staging")) {
                        Toast.makeText(mContext, "创建成功", Toast.LENGTH_SHORT).show();
                        mBtnSwitchMarketPre.setText("应用商店自升级删除preView环境");
                    } else {
                        if (removeFile(mRootPath, "market_staging")) {
                            Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                        }
                        mBtnSwitchMarketPre.setText("应用商店自升级添加preView环境");
                    }
                    break;
                case R.id.tool_btn_make_log:
                    callPhoneMakeLog();
                    break;
                case R.id.tool_btn_slog_config:
                    createFile(mRootPath, "slog.config");
                    break;
                default:
                    break;
            }
        }
    }

    private void callPhoneMakeLog() {
        String phoneNumber = "*#*#284#*#*";
        try {
            phoneNumber = URLEncoder.encode(phoneNumber, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d(TAG, "不支持encode………………");
        }
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
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
            Toast.makeText(mContext, name + "已经存在", Toast.LENGTH_SHORT).show();
        } else {
            try {
                createSuccess = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return createSuccess;
    }

    private boolean removeFile(String path, String name) {
        boolean removeSuccess;
        File file = new File(path, name);
        if (fileIsExists(path + File.separator + name)) {
            removeSuccess = file.delete();
        } else {
            removeSuccess = false;
        }
        return removeSuccess;
    }

    private class MyTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object... params) {
            Log.d(TAG, " ******** doInBackground (Object[] params)*********");
            int count = copyBtToSdcard("测试bt种子文件");
            publishProgress(count);
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
            Log.d(TAG, "values = " + values[0]);
            Log.d(TAG, "******** onProgressUpdate(Object[]) values) *********");
            Toast.makeText(mContext, "拷贝" + values[0] + "个bt文件完成,请在SD卡根目录使用", Toast.LENGTH_LONG).show();
            mBtnBt.setClickable(true);
            mBtnBt.setEnabled(true);
        }
    }

    private Snackbar makeSnackBar(View view, String showMessage) {
        Snackbar temp = Snackbar.make(view, showMessage, Snackbar.LENGTH_SHORT);
        View bgView = temp.getView();
        bgView.setBackgroundColor(ActivityCompat.getColor(mContext, R.color.colorRed));
        return temp;
    }


    private int copyBtToSdcard(String dir) {
        AssetManager assetManager = mContext.getAssets();
        File btDir = new File(mRootPath + File.separator + dir);
        int successFileCount = 0;
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
                byte[] bytes = new byte[1024]; //每次读取到内存1kb 先整个byte数组对象(buffer)用来持有byte
                int length; //将输入字节流撸到byte数组对象（该数组的持有的每一个元素是byte）
                OutputStream outputStream = new FileOutputStream(outFile); //整个输出字节流到文件
                while ((length = inputStream.read(bytes)) > 0) { // 程序从输入字节流中读取到byte数组里（buffer），每次读取1024字节
                    outputStream.write(bytes, 0, length); //然后byte数组(buffer)写入到输出字节流里，直接再到文件里
                }
                inputStream.close();
                outputStream.flush();
                outputStream.close();
                successFileCount++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return successFileCount;
    }

}
