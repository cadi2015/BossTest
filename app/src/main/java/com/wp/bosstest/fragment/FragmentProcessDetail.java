package com.wp.bosstest.fragment;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wp.bosstest.R;
import com.wp.bosstest.adapter.ProcessMsgAdapter;
import com.wp.bosstest.service.MonitorService;
import com.wp.bosstest.utils.LogHelper;
import com.wp.bosstest.utils.PermissionUtil;
import com.wp.bosstest.utils.ProcessUtil;
import com.wp.bosstest.utils.RootUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


/**
 * Created by cadi on 2016/9/22.
 */

public class FragmentProcessDetail extends Fragment {
    private String TAG = LogHelper.makeTag(FragmentProcessDetail.class);
    private static final int REQUEST_CODE_PERMISSION = 110;
    private View mRootView;
    private Context mContext;
    private TextView mTvSystemMemMsg;
    private TextView mTvMemTitle;
    private ListView mLvDpMsg;
    private ListView mLvUiMsg;
    private LayoutInflater mLayoutInflater;
    private boolean isAdded = true;
    private View mUiLvHeader;
    private View mDpLvHeader;
    private Button mBtnStartMonitor;
    private Button mBtnStopMonitor;
    private Intent intentMonitor;
    private TextView mTvPermissionHint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_process_detail, null);
        Log.d(TAG, "onCreateView(………………）");
        init();
        setupViews();
        return mRootView;
    }

    //普通内部类的，可以称作为对象内部类的，实例对象的内部类，与静态内部类的区别当然是是否需要外部类的实例对象了，呵呵，静态的当然不用了，类成员用鸡巴毛实例对象
    private class BtnStartClickLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            PackageManager pm = mContext.getPackageManager();
            boolean alertPermission = PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", mContext.getPackageName());
            Log.d(TAG, "alertWindowPermission = " + alertPermission);

            Log.d(TAG, "BtnStartClickLis, onClick(View v)");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(mContext)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getActivity().getPackageName()));
                startActivityForResult(intent, REQUEST_CODE_PERMISSION);
                Toast.makeText(mContext, getString(R.string.permission_err), Toast.LENGTH_LONG).show();
            } else {
                mContext.startService(intentMonitor);
                getActivity().finish();
            }
        }
    }

    private class BtnStopClickLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "BtnStopClickLis, onClick(View v)");
            mContext.stopService(intentMonitor);
            getActivity().finish();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "FragmentProcessDetail, onActivityResult(int requestCode, int resultCode, Intent data)");
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (!Settings.canDrawOverlays(mContext)) {
                Toast.makeText(mContext, "您还没开启权限啊，必须手动开启权限啊", Toast.LENGTH_LONG).show();
            } else {
                mContext.startService(intentMonitor);
                getActivity().finish();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        standAlone();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        boolean isRunning = isServiceRunning(mContext.getPackageName() + ".service.MonitorService");
        Log.d(TAG, "service is Running = " + isRunning);
        mBtnStartMonitor.setVisibility(isRunning ? View.GONE : View.VISIBLE);
        mBtnStopMonitor.setVisibility(isRunning ? View.VISIBLE : View.GONE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mTvPermissionHint.setVisibility(View.VISIBLE);
            mTvPermissionHint.setText(getString(R.string.permission_warning, Build.VERSION.RELEASE));
        } else {
            mTvPermissionHint.setVisibility(View.VISIBLE);
            mTvPermissionHint.setText(getString(R.string.permission_six_error, Build.VERSION.RELEASE));
        }
    }


    private boolean isServiceRunning(String serviceClassName) {
        boolean isRunning = false;
        int maxNum = 100;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(maxNum);

        if (serviceList == null || serviceList.size() <= 0) {
            return false;  //加入容错
        }

        for (ActivityManager.RunningServiceInfo serviceInfo : serviceList) {
            String serviceName = serviceInfo.service.getClassName();
            if (serviceName.equals(serviceClassName)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    private void standAlone() { //为了home键的问题，做到实时刷新，装个13
        mTvSystemMemMsg.setText(ProcessUtil.getSystemMemMsg());
        if (isAdded) {
            mUiLvHeader = mLayoutInflater.inflate(R.layout.layout_process_detail_lv_header, null);
            TextView uiHeaderTitle = (TextView) mUiLvHeader.findViewById(R.id.layout_process_detail_lv_header_tv_title);
            uiHeaderTitle.setText("UI进程信息");
            mLvUiMsg.addHeaderView(mUiLvHeader, null, false);
        }

        TextView uiHeaderState = (TextView) mUiLvHeader.findViewById(R.id.layout_process_detail_lv_header_tv_state);

        if (isAdded) {
            mDpLvHeader = mLayoutInflater.inflate(R.layout.layout_process_detail_lv_header, null);
            TextView dpHeaderTitle = (TextView) mDpLvHeader.findViewById(R.id.layout_process_detail_lv_header_tv_title);
            dpHeaderTitle.setText("DP进程信息");
            mLvDpMsg.addHeaderView(mDpLvHeader, null, false);
        }

        TextView dpHeaderState = (TextView) mDpLvHeader.findViewById(R.id.layout_process_detail_lv_header_tv_state);

        String[] uiProcessMsg = ProcessUtil.getRunningProcessInfo(ProcessUtil.UI_PROCESS_NAME);
        String[] dpProcessMsg = ProcessUtil.getRunningProcessInfo(ProcessUtil.DP_PROCESS_NAME);
        if (Integer.valueOf(uiProcessMsg[0]) == 0) {
            uiHeaderState.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "GONE GONE GONE");
            uiHeaderState.setVisibility(View.GONE);
        }
        if (Integer.valueOf(dpProcessMsg[0]) == 0) {
            dpHeaderState.setVisibility(View.VISIBLE);
        } else {
            dpHeaderState.setVisibility(View.GONE);
        }
        ProcessMsgAdapter uiProcessAdapter = new ProcessMsgAdapter(uiProcessMsg, mContext);
        ProcessMsgAdapter dpProcessAdapter = new ProcessMsgAdapter(dpProcessMsg, mContext);
        mLvDpMsg.setAdapter(dpProcessAdapter);
        mLvUiMsg.setAdapter(uiProcessAdapter);
        isAdded = false;
    }

    private void setupViews() {
        mTvSystemMemMsg = (TextView) mRootView.findViewById(R.id.process_detail_tv_system_mem_msg);
        mTvMemTitle = (TextView) mRootView.findViewById(R.id.process_detail_tv_mem_title);
        mTvMemTitle.setText(Build.MODEL + "内存信息");
        mLvUiMsg = (ListView) mRootView.findViewById(R.id.process_detail_lv_ui);
        mLvDpMsg = (ListView) mRootView.findViewById(R.id.process_detail_lv_dp);
        mBtnStartMonitor = (Button) mRootView.findViewById(R.id.process_detail_btn_start_monitor);
        mBtnStopMonitor = (Button) mRootView.findViewById(R.id.process_detail_btn_stop_monitor);
        mTvPermissionHint = (TextView) mRootView.findViewById(R.id.process_detail_tv_hint);
        mBtnStartMonitor.setOnClickListener(new BtnStartClickLis());
        mBtnStopMonitor.setOnClickListener(new BtnStopClickLis());
    }

    private void init() {
        mContext = getActivity();
        mLayoutInflater = LayoutInflater.from(mContext);
        intentMonitor = new Intent(mContext, MonitorService.class);
        PermissionUtil.verifyStoragePermissions(getActivity());
//        if (RootUtil.getRootAhth() == false) {    //root权限拿了也没用，真他妈的醉了
//            RootUtil.upgradeRootPermission(getActivity().getPackageCodePath());
//        }
    }

}
