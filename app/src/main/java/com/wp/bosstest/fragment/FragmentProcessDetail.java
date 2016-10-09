package com.wp.bosstest.fragment;

import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.wp.bosstest.R;
import com.wp.bosstest.adapter.ProcessMsgAdapter;
import com.wp.bosstest.utils.LogHelper;

import java.text.DecimalFormat;
import java.util.List;


/**
 * Created by cadi on 2016/9/22.
 */

public class FragmentProcessDetail extends Fragment {
    private String TAG = LogHelper.makeTag(FragmentProcessDetail.class);
    private static final String UI_PROCESS_NAME = "android.process.mediaUI";
    private static final String DP_PROCESS_NAME = "android.process.media";
    private View mRootView;
    private Context mContext;
    private TextView mTvSystemMemMsg;
    private TextView mTvMemTitle;
    private ActivityManager mActivityManager;
    private ListView mLvDpMsg;
    private ListView mLvUiMsg;
    private LayoutInflater mLayoutInflater;
    private boolean isAdded = true;
    private View  mUiLvHeader;
    private View mDpLvHeader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_process_detail, null);
        Log.d(TAG, "onCreateView(………………）");
        init();
        setupViews();
        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        standAlone();
    }

    /**
     * @param processName
     * @return uid, pid, totalPss Of Array
     */
    private String[] getRunningProcessInfo(String processName) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = mActivityManager.getRunningAppProcesses();
        Log.d(TAG, "runningProcessList size = " + runningAppProcessInfoList.size());
        int temp = 0;
        int pid = 0;
        int uid = 0;
        int totalPss;
        int processType = 0;
        String[] packageList = null;
        for (ActivityManager.RunningAppProcessInfo item : runningAppProcessInfoList) {
            Log.d(TAG, (temp++) + "、processName = " + item.processName + ", uid = " + item.uid + ", pid = " + item.pid);
            if (item.processName.equals(processName)) {
                uid = item.uid;
                pid = item.pid;
                processType = item.importance;
                packageList = item.pkgList;
                Log.d(TAG, "current importance = " + item.importance);
                Log.d(TAG, "current pid = " + pid);
                Log.d(TAG, "current packageList Size = " + packageList.length);
                break;
            }
        }
        int[] pids = {pid};
        Debug.MemoryInfo[] memoryInfo = mActivityManager.getProcessMemoryInfo(pids);
        Debug.MemoryInfo currentMemInfo = memoryInfo[0];
        Log.d(TAG, "TotalPss = " + currentMemInfo.getTotalPss() + "KB");
        totalPss = currentMemInfo.getTotalPss();
        float totalPssFloat = (float) totalPss;
        DecimalFormat df = new DecimalFormat("0.00"); //保留两位小数 嘿嘿
        String processTypeStr = getProcessType(processType);
        StringBuilder packageNames = new StringBuilder();
        if (packageList != null) {
            for (int i = 0; i < packageList.length; i++) {
                packageNames.append(packageList[i]);
                if (i != packageList.length - 1) {
                    packageNames.append("\n");
                }
            }
        } else {
            packageNames.append("无");
        }
        String[] currentProcessStr = {"" + uid, "" + pid, df.format(totalPssFloat / 1024) + "MB", processTypeStr, packageNames.toString()};
        Log.d(TAG, "TotalPrivateDirty = " + currentMemInfo.getTotalPrivateDirty() + "KB");
        Log.d(TAG, "TotalPrivteClean = " + currentMemInfo.getTotalPrivateClean() + "KB");
        Log.d(TAG, "TotalSharedDirty = " + currentMemInfo.getTotalSharedDirty() + "KB");
        Log.d(TAG, "TotalSharedClean = " + currentMemInfo.getTotalSharedClean() + "KB");
        return currentProcessStr;
    }

    private String getProcessType(int processImportance) {
        String typeStr;
        switch (processImportance) {
            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND:
                typeStr = "前台进程";
                break;
            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND:
                typeStr = "后台进程";
                break;
            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE:
                typeStr = "可见进程";
                break;
            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE:
                typeStr = "服务进程";
                break;
            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_EMPTY:
                typeStr = "空进程";
                break;
            default:
                typeStr = "无";
                break;
        }
        return typeStr;
    }


    private String getSystemMemMsg() {
        String memString;
        ActivityManager.MemoryInfo saveMemoryInfo = new ActivityManager.MemoryInfo();
        mActivityManager.getMemoryInfo(saveMemoryInfo);
        StringBuilder sbSystemMemMsg = new StringBuilder();
        long availMemSize = saveMemoryInfo.availMem;
        boolean isLowMem = saveMemoryInfo.lowMemory;
        long totalMemSize = saveMemoryInfo.totalMem;
        long thresholdSize = saveMemoryInfo.threshold;
        String availMemStr = formatFileSize(availMemSize);
        String totalMemStr = formatFileSize(totalMemSize);
        String thresholdStr = formatFileSize(thresholdSize);
        sbSystemMemMsg.append(availMemStr + "\n");
        sbSystemMemMsg.append(totalMemStr + "\n");
        sbSystemMemMsg.append(thresholdStr + "\n");
        sbSystemMemMsg.append(isLowMem);
        memString = sbSystemMemMsg.toString();
        return memString;
    }

    private void standAlone() { //为了home键的问题，做到实时刷新，装个13
        mTvSystemMemMsg.setText(getSystemMemMsg());
        if (isAdded) {
            mUiLvHeader = mLayoutInflater.inflate(R.layout.layout_process_detail_lv_header, null);
            TextView uiHeaderTitle = (TextView) mUiLvHeader.findViewById(R.id.layout_process_detail_lv_header_tv_title);
            uiHeaderTitle.setText("UI进程信息");
            mLvUiMsg.addHeaderView(mUiLvHeader, null, false);
        }

        TextView uiHeaderState  = (TextView) mUiLvHeader.findViewById(R.id.layout_process_detail_lv_header_tv_state);

        if (isAdded) {
            mDpLvHeader = mLayoutInflater.inflate(R.layout.layout_process_detail_lv_header, null);
            TextView dpHeaderTitle = (TextView) mDpLvHeader.findViewById(R.id.layout_process_detail_lv_header_tv_title);
            dpHeaderTitle.setText("DP进程信息");
            mLvDpMsg.addHeaderView(mDpLvHeader, null, false);
        }

        TextView dpHeaderState = (TextView) mDpLvHeader.findViewById(R.id.layout_process_detail_lv_header_tv_state);

        String[] uiProcessMsg = getRunningProcessInfo(UI_PROCESS_NAME);
        String[] dpProcessMsg = getRunningProcessInfo(DP_PROCESS_NAME);
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
    }

    private String formatFileSize(long bytes) {
        return Formatter.formatFileSize(mContext, bytes);
    }

    private void init() {
        mContext = getActivity();
        mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        mLayoutInflater = LayoutInflater.from(mContext);
    }

}
