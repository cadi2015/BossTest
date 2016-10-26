package com.wp.bosstest.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.text.format.Formatter;
import android.util.Log;

import com.wp.bosstest.application.App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cadi on 2016/10/9.
 */

public class ProcessUtil {
    public static final String UI_PROCESS_NAME = "android.process.mediaUI";
    public static final String DP_PROCESS_NAME = "android.process.media";
    private static ActivityManager mActivityManager;
    private static final String TAG = LogHelper.makeTag(ProcessUtil.class);
    private static final Context mContext;

    static {
        mContext = App.getAppContext();
        mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
    }

    /**
     * @param processName
     * @return uid, pid, totalPss Of Array
     */
    public static String[] getRunningProcessInfo(String processName) {
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

    private static String getProcessType(int processImportance) {
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

    /**
     * @return 本机内存情况 可用内存、 总内存、低内存阀值、 处于低内存
     */
    public static String getSystemMemMsg() {
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


    private static String formatFileSize(long bytes) {
        return Formatter.formatFileSize(mContext, bytes);
    }


    public static String getProcessCpuRate(int pid) {

        float totalCpuTime1 = getTotalCpuTime();
        float processCpuTime1 = getAppCpuTime(pid);
        try {
            Thread.sleep(360); //哥啊，你是让当前线程休眠360毫秒，实现间隔计算啊？还行吧
        } catch (Exception e) {
            e.printStackTrace();
        }

        float totalCpuTime2 = getTotalCpuTime();
        float processCpuTime2 = getAppCpuTime(pid);

        float cpuRate = 100 * (processCpuTime2 - processCpuTime1)
                / (totalCpuTime2 - totalCpuTime1);

        if(cpuRate < 0){
            cpuRate = 0;
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String result = decimalFormat.format((double)cpuRate);
        return result;
    }

    // 获取系统总CPU使用时间
    private static long getTotalCpuTime() {
        String[] cpuInfos = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/stat")), 1000);
            String load = reader.readLine();
            reader.close();
            cpuInfos = load.split(" ");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        long totalCpu = Long.parseLong(cpuInfos[2])
                + Long.parseLong(cpuInfos[3]) + Long.parseLong(cpuInfos[4])
                + Long.parseLong(cpuInfos[6]) + Long.parseLong(cpuInfos[5])
                + Long.parseLong(cpuInfos[7]) + Long.parseLong(cpuInfos[8]);
        return totalCpu;
    }


    // 获取进程占用的CPU时间 int pid = android.os.Process.myPid();  //这是本应用的pid
    private static long getAppCpuTime(int pid) {
        Log.d(TAG, "getAppCpuTime(int pid)   pid = " + pid);
        String[] cpuInfo = null;
        String path = File.separator + "proc" + File.separator  + pid + File.separator + "stat";
        long appCpuTime = 0;
        if (pid == 0) {
            return 0;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(path)), 1000);
            String load = reader.readLine();
            reader.close();
            cpuInfo = load.split(" ");
            appCpuTime = Long.parseLong(cpuInfo[13])
                    + Long.parseLong(cpuInfo[14]) + Long.parseLong(cpuInfo[15])
                    + Long.parseLong(cpuInfo[16]);
            Log.d(TAG, "getAppCupTime(int pid), cpuInfo = " + Arrays.toString(cpuInfo));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return appCpuTime;
    }

    public static String getCpuRate(int pid) {
        String cpuResult = "0";
        try {
            String eachLine;
            Process p = Runtime.getRuntime().exec("top -m 10 -n 1");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((eachLine = bufferedReader.readLine()) != null) {
                Log.d(TAG, "eachLine = " + eachLine);
                eachLine.trim();
                if (eachLine.length() <= 5) {
                    continue;
                }
                String[] baseLine = eachLine.split("%");
                Log.d(TAG, "temp = " + Arrays.toString(baseLine));
                String[] filterStr = baseLine[0].trim().split(" ");
                if (filterStr[0].equals(String.valueOf(pid))) {
                    cpuResult = filterStr[filterStr.length - 1];
                    break;
                }
                Log.d(TAG, "temp[0]'s Array = " + Arrays.toString(baseLine[0].trim().split(" ")));
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "top command is a error……………………………………");
        }
        Log.d(TAG, "cpuResult = " + cpuResult);
        return cpuResult;
    }



    public static String readCpuStatNetEasy(int pid) {
        String result = "0";
        String processPid = Integer.toString(pid);
        String cpuStatPath = "/proc/" + processPid + "/stat";
        try {
            // monitor cpu stat of certain process
            RandomAccessFile processCpuInfo = new RandomAccessFile(cpuStatPath, "r");
            String line = "";
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.setLength(0);
            while ((line = processCpuInfo.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }
            String[] tok = stringBuffer.toString().split(" ");
            result = String.valueOf(Long.parseLong(tok[13]) + Long.parseLong(tok[14]));
            processCpuInfo.close();
        } catch (FileNotFoundException e) {
            Log.w(TAG, "FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        readTotalCpuStat();
        return result;
    }


    public static  boolean isServiceRunning(String serviceClassName) {
        boolean isRunning = false;
        int maxNum = 100;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(maxNum);

        if (serviceList == null || serviceList.size() <= 0) {
            return false;  //加入容错
        }

        for (ActivityManager.RunningServiceInfo serviceInfo : serviceList) {
            String serviceName = serviceInfo.service.getClassName();
            Log.d(TAG, "serviceName = " + serviceName);
            if (serviceName.equals(serviceClassName)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

}

