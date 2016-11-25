package com.wp.bosstest.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wp.bosstest.R;
import com.wp.bosstest.activity.ProcessDetailActivity;
import com.wp.bosstest.config.SharedConstant;
import com.wp.bosstest.receiver.DownloadTaskReceiver;
import com.wp.bosstest.service.FpsService;
import com.wp.bosstest.service.UninstallAppService;
import com.wp.bosstest.sqlite.SQLiteManager;
import com.wp.bosstest.utils.LogHelper;
import com.wp.bosstest.utils.PermissionUtil;
import com.wp.bosstest.utils.ProcessUtil;
import com.wp.bosstest.utils.ShellUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by cadi on 2016/8/13.
 */
public class FragmentPerformance extends Fragment {
    private final static String TAG = LogHelper.makeTag(FragmentPerformance.class);
    private View mRootView;
    private TextView mTvInsert;
    private ImageView mIvYouliao;
    private static HandlerThread mWorkingThread;
    private Handler mWorkingHandler;
    private DownloadManager mDownloadManger;
    private Context mContext;
    private Handler mMainHandler;
    private SeekBar mSeekBar;
    private ProgressBar mProgressBar;
    private BroadcastReceiver mReceiverDownload;
    private BroadcastReceiver mReceiverUninstall;
    private TextView mTvRemoveFailed;
    private Button mBtnProcessMsg;
    private Button mBtnFpsStart;
    private Button mBtnFpsStop;
    private Button mBtnLogLib;
    private Button mBtnCleanData;
    private Button mBtnKillDp;
    private Button mBtnKillUi;
    private Button mBtnUninstallApp;
    private Button mBtnMonkey;
    private Intent mIntentFps;
    private TextView mTvShowLog;
    private Activity mActivity;


    private class UninstallBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "UninstallBroadcastReceiver ^^^^^^ onReceiver(Context context, Intent intent)");
            boolean isSuccess = intent.getBooleanExtra("isSuccess", false);
            MyUninstallRunnable myUninstallRunnable = new MyUninstallRunnable();
            if (isSuccess == true) {
                mMainHandler.postDelayed(myUninstallRunnable, 1000);
            } else {
                mMainHandler.postDelayed(myUninstallRunnable, 500);
            }
        }
    }

    private class MyUninstallRunnable implements Runnable {
        @Override
        public void run() {
            Log.d(TAG, "MyUninstall Runnable ^^^^^^^ run()");
            Log.d(TAG, "MyUninstall Runnable ^^^^^^^ thread = " + Thread.currentThread().getName());
            mIvYouliao.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach(Context context)");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate( Bundle savedInstanceState)");
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isRunning = ProcessUtil.isServiceRunning(mContext.getPackageName() + ".service.FpsService");
        Log.d(TAG, "isRunning = " + isRunning);
        if (isRunning) {
            mBtnFpsStop.setVisibility(View.VISIBLE);
        } else {
            mBtnFpsStart.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_performance, null);
        Log.d(TAG, "onCreateView at Thread " + Thread.currentThread().getName());
        Log.d(TAG, "mRootView = " + mRootView);
        init();
        initWorking();
        setupViews();
        return mRootView;
    }

    private void init() {
        mContext = getActivity();
        mActivity = getActivity();
        mDownloadManger = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        mMainHandler = new MainHandler();
        mReceiverDownload = new DownloadTaskReceiver();
        mReceiverUninstall = new UninstallBroadcastReceiver();
        mIntentFps = new Intent();
        mIntentFps.setClass(mContext, FpsService.class);
    }

    private void setupViews() {
        mSeekBar = (SeekBar) mRootView.findViewById(R.id.performance_seek_bar);
        mTvRemoveFailed = (TextView) mRootView.findViewById(R.id.performance_tv_remove_failed);
        mBtnProcessMsg = (Button) mRootView.findViewById(R.id.performance_btn_process_msg);
        mBtnFpsStart = (Button) mRootView.findViewById(R.id.performance_btn_fps_start);
        mBtnFpsStop = (Button) mRootView.findViewById(R.id.performance_btn_fps_stop);
        mBtnLogLib = (Button) mRootView.findViewById(R.id.performance_btn_log_lib);
        mBtnCleanData = (Button) mRootView.findViewById(R.id.performance_btn_clean_data);
        mBtnKillDp = (Button) mRootView.findViewById(R.id.performance_btn_kill_dp);
        mBtnKillUi = (Button) mRootView.findViewById(R.id.performance_btn_kill_ui);
        mBtnUninstallApp = (Button) mRootView.findViewById(R.id.performance_btn_uninstall_app);
        mBtnMonkey = (Button) mRootView.findViewById(R.id.performance_btn_monkey);
        mTvInsert = (TextView) mRootView.findViewById(R.id.performance_tv_insert);
        mTvShowLog = (TextView) mRootView.findViewById(R.id.performance_tv_show_log);
        mIvYouliao = (ImageView) mRootView.findViewById(R.id.performance_iv_youliao);
        mTvShowLog.setMovementMethod(ScrollingMovementMethod.getInstance());
        Log.d(TAG, "SeekBar current progress  = " + mSeekBar.getProgress());
        mTvInsert.setText(getString(R.string.performance_tv_task, mSeekBar.getProgress() + 1));
        mProgressBar = (ProgressBar) mRootView.findViewById(R.id.performance_progress_bar);
        MyBtnCikLis myBtnCikLis = new MyBtnCikLis();
        mTvInsert.setOnClickListener(new MyClickLis());
        mSeekBar.setOnSeekBarChangeListener(new MySeekBarLis());
        mTvRemoveFailed.setOnClickListener(myBtnCikLis);
        mBtnProcessMsg.setOnClickListener(myBtnCikLis);
        mBtnFpsStart.setOnClickListener(myBtnCikLis);
        mBtnFpsStop.setOnClickListener(myBtnCikLis);
        mBtnLogLib.setOnClickListener(myBtnCikLis);
        mBtnCleanData.setOnClickListener(myBtnCikLis);
        mBtnKillDp.setOnClickListener(myBtnCikLis);
        mBtnKillUi.setOnClickListener(myBtnCikLis);
        mBtnUninstallApp.setOnClickListener(myBtnCikLis);
        mBtnMonkey.setOnClickListener(myBtnCikLis);
        AnimationDrawable animationDrawable = getYouliaoAnimation();
        mIvYouliao.setBackground(animationDrawable);
        animationDrawable.start();
        Log.d(TAG, "mRootView.getParent() = " + mSeekBar.getParent());
    }

    private AnimationDrawable getYouliaoAnimation() {
        AnimationDrawable animationDrawable = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.frame_by_frame_youliao, null);
        } else {
            animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.frame_by_frame_youliao);
        }
        return animationDrawable;
    }

    private class MyBtnCikLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final SharedPreferences sp = mContext.getSharedPreferences(SharedConstant.SHARED_BOSS_CONFIG_NAME, Context.MODE_PRIVATE);
            switch (v.getId()) {
                case R.id.performance_tv_remove_failed:
                    removeFailedDownloadTask();
                    break;
                case R.id.performance_btn_process_msg:
                    Log.d(TAG, "MyBtnCik, btn_process_msg");
                    Intent intent = new Intent(mContext, ProcessDetailActivity.class);
                    startActivity(intent);
                    break;
                case R.id.performance_btn_fps_start:
                    if (!(sp.getBoolean(SharedConstant.DIALOG_KEY_CHECK_BOX_SELECTED_ROOT, false)) || !(sp.getBoolean(SharedConstant.DIALOG_KEY_CHECK_BOX_SELECTED_FLOAT_WINDOW, false))) {
                        createDiaLog(sp, "需要前往安全中心授予BossTest，Root权限与悬浮窗权限，必须手工开启", 0);
                    } else {
                        ComponentName componentName;
                        componentName = mContext.startService(mIntentFps);
                        Log.d(TAG, "ComponentName = " + componentName.getClassName());
                        mBtnFpsStart.setVisibility(View.GONE);
                        mBtnFpsStop.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.performance_btn_fps_stop:
                    mContext.stopService(mIntentFps);
                    mBtnFpsStop.setVisibility(View.GONE);
                    mBtnFpsStart.setVisibility(View.VISIBLE);
                    break;
                case R.id.performance_btn_log_lib:
                    if (!(sp.getBoolean(SharedConstant.DIALOG_KEY_CHECK_BOX_SELECTED_ROOT, false))) {
                        createDiaLog(sp, "需要前往安全中心授予BossTest的Root权限，必须手工开启", 1);
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "ShellUtils is root = " + ShellUtils.checkRootPermission());
                                List<String> command = new ArrayList<>();
                                command.add("logcat -d -s DownloadManager | grep libVersion");
                                ShellUtils.CommandResult result = ShellUtils.execCommand(command, true);
                                String resultStr = result.successMsg;
                                Message message = new Message();
                                Bundle bundle = new Bundle();
                                bundle.putString("content", resultStr);
                                message.setData(bundle);
                                mMainHandler.sendMessage(message);
                            }
                        }
                        ).start();
                    }
                    break;
                case R.id.performance_btn_clean_data:
                    if (!(sp.getBoolean(SharedConstant.DIALOG_KEY_CHECK_BOX_SELECTED_ROOT, false))) {
                        createDiaLog(sp, "需要前往安全中心授予BossTest的Root权限，必须手工开启", 1);
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ShellUtils.CommandResult resultUi = ShellUtils.execCommand("pm clear com.android.providers.downloads.ui", true);
                                ShellUtils.CommandResult resultDp = ShellUtils.execCommand("pm clear com.android.providers.downloads", true);
                                Log.d(TAG, "clear data result successMsg = " + resultUi.successMsg);
                                Log.d(TAG, "clear data result errorMsg= " + resultUi.errorMsg);
                                if (resultUi.result == 0 && resultDp.result == 0) {
                                    mMainHandler.sendEmptyMessage(0x222);
                                } else {
                                    mMainHandler.sendEmptyMessage(0x909);
                                }
                            }
                        }).start();
                    }
                    break;
                case R.id.performance_btn_kill_dp:
                    if (!(sp.getBoolean(SharedConstant.DIALOG_KEY_CHECK_BOX_SELECTED_ROOT, false))) {
                        createDiaLog(sp, "需要前往安全中心授予BossTest的Root权限，必须手工开启", 1);
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ShellUtils.CommandResult resultKillDp = ShellUtils.execCommand("am force-stop com.android.providers.downloads", true);
                                Log.d(TAG, "killDpResultCode = " + resultKillDp.result);
                                if (resultKillDp != null && resultKillDp.result == 0) {
                                    mMainHandler.sendEmptyMessage(0x333);
                                } else {
                                    mMainHandler.sendEmptyMessage(0x909);
                                }
                            }
                        }).start();
                    }
                    break;
                case R.id.performance_btn_kill_ui:
                    if (!(sp.getBoolean(SharedConstant.DIALOG_KEY_CHECK_BOX_SELECTED_ROOT, false))) {
                        createDiaLog(sp, "需要前往安全中心授予BossTest的Root权限，必须手工开启", 1);
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ShellUtils.CommandResult resultKillUi = ShellUtils.execCommand("am force-stop com.android.providers.downloads.ui", true);
                                Log.d(TAG, "killUiResultCode = " + resultKillUi.result);
                                if (resultKillUi != null && resultKillUi.result == 0) {
                                    mMainHandler.sendEmptyMessage(0x444);
                                } else {
                                    mMainHandler.sendEmptyMessage(0x909);
                                }
                            }
                        }).start();
                    }
                    break;
                case R.id.performance_btn_uninstall_app:
                    if (!(sp.getBoolean(SharedConstant.DIALOG_KEY_CHECK_BOX_SELECTED_ROOT, false))) {
                        createDiaLog(sp, "需要前往安全中心授予BossTest的Root权限，必须手工开启", 1);
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(mContext).setTitle("警告").setMessage("该操作会删除所有第三方App(不含BossTest、手雷、有料、QQ、微信)，慎重决定哦").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("卸载", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent uninstallAppIntent = new Intent(mContext, UninstallAppService.class);
                                mContext.startService(uninstallAppIntent);
                                mIvYouliao.setVisibility(View.VISIBLE);
                            }
                        }).create();
                        alertDialog.show();
                    }
                    break;
                case R.id.performance_btn_monkey:
                    if (!(sp.getBoolean(SharedConstant.DIALOG_KEY_CHECK_BOX_SELECTED_ROOT, false))) {
                        createDiaLog(sp, "需要前往安全中心授予BossTest的Root权限，必须手工开启", 1);
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(mContext).setTitle("警告").setMessage("确定要启动Monkey吗？").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("启动", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ShellUtils.execCommand("monkey -p com.android.providers.downloads.ui -p com.android.providers.downloads -v -v --throttle 50 --ignore-crashes --ignore-timeouts --pct-touch 30 --pct-motion 20 --pct-nav 20 --pct-majornav 15 --pct-appswitch 5 --pct-anyevent 5 --pct-trackball 0 --pct-syskeys 0 --bugreport 500000 >/mnt/sdcard/log.txt", true);
                                mActivity.finish();
                            }
                        }).create();
                        alertDialog.show();
                    }
                    break;
                default:
                    break;
            }
        }
    }


    private void createDiaLog(final SharedPreferences sp, String tips, final int type) {
        AlertDialog.Builder diaBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.dialog_layout_tips, null);
        TextView tipsContent = (TextView) view.findViewById(R.id.dialog_tv_content);
        tipsContent.setText(tips);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.dialog_cb_select);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sp.edit();
                Log.d(TAG, "is Checked = " + isChecked);
                if (isChecked == true) {
                    editor.putBoolean(SharedConstant.DIALOG_KEY_CHECK_BOX_SELECTED_ROOT, true);
                    if (type == 0) {
                        editor.putBoolean(SharedConstant.DIALOG_KEY_CHECK_BOX_SELECTED_FLOAT_WINDOW, true);
                    }
                    editor.apply();
                } else {
                    editor.putBoolean(SharedConstant.DIALOG_KEY_CHECK_BOX_SELECTED_ROOT, false);
                    if (type == 0) {
                        editor.putBoolean(SharedConstant.DIALOG_KEY_CHECK_BOX_SELECTED_FLOAT_WINDOW, false);
                    }
                    editor.apply();
                }
            }
        });
        Log.d(TAG, "is root = " + PermissionUtil.upgradeRootPermission(mContext.getPackageCodePath()));
        boolean currentRootState = PermissionUtil.upgradeRootPermission(mContext.getPackageCodePath());
        diaBuilder.setView(view).setTitle("温馨提示");
        diaBuilder.create().show();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated(Bundle savedInstanceState)");
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter intentFilterDownload = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        IntentFilter intentFilterUninstall = new IntentFilter("bossTest.intent.action.UNINSTALL_FINISH");
        mContext.registerReceiver(mReceiverDownload, intentFilterDownload);
        mContext.registerReceiver(mReceiverUninstall, intentFilterUninstall);
    }

    private void unregisterReceiver() {
        mContext.unregisterReceiver(mReceiverDownload);
        mContext.unregisterReceiver(mReceiverUninstall);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
        Log.d(TAG, "onDestroy()");
    }

    private class MySeekBarLis implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Log.d(TAG, "onPregressChanged(SeekBar seekBar, int progress, boolean fromUser)");
            Log.d(TAG, "onPregressChanged(SeekBar seekBar, int progress, boolean fromUser) fromUser = " + fromUser);
            Log.d(TAG, "onPregressChanged(…………）, int progress = " + progress);
            int index = progress + 1;
            mTvInsert.setText(getString(R.string.performance_tv_task, index));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Log.d(TAG, "onStartTrackingTouch(SeekBar seekBar)");
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int index = seekBar.getProgress() + 1;
            mTvInsert.setText(getString(R.string.performance_tv_task, index));
        }
    }

    private class MyClickLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int index = mSeekBar.getProgress() + 1;
            Log.d(TAG, "seekBar progress = " + index);
            if (index != 0) {
                mTvInsert.setTextColor(ContextCompat.getColor(mContext, R.color.color_txt_enable_gray));
                mTvInsert.setEnabled(false);
                mTvInsert.setClickable(false);
                mProgressBar.setVisibility(View.VISIBLE);
                postTask(index);
            } else {
                makeSnackBar(mTvInsert, "任务不能为0", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void initWorking() {
        if (mWorkingThread == null) {
            mWorkingThread = new HandlerThread("taskWorking"); //工作线程
            mWorkingThread.start();
        }
        mWorkingHandler = new WorkingHandler(mWorkingThread.getLooper());//Handler的post方法总结，将Runnable发送给绑定的线程运行（如果在ui线程，就发给ui线程，如果在工作线程，就发给工作线程） //其实就是将handler与线程绑定,HandlerThread,一个Looper的线程实例对象
    }

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "MainHandler ^^^^^  handleMessage(Message msg)");
            Log.d(TAG, "first msg.what = " + msg.what);
            if (msg.what == 0x111) {
                Log.d(TAG, "msg.getData() = " + msg.getData());
                Log.d(TAG, "MainHandler handleMessage(Message msg) at Thread  = " + Thread.currentThread().getName());
                mTvInsert.setTextColor(ContextCompat.getColor(mContext, R.color.color_guide_txt_white));
                mTvInsert.setEnabled(true);
                mTvInsert.setClickable(true);
                mProgressBar.setVisibility(View.GONE);
            } else if (msg.what == 0x222) {
                Toast.makeText(mContext, "Ui清空:" + "success" + "\nDp清空:" + "success", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 0x333) {
                Toast.makeText(mContext, "Dp杀死成功", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 0x444) {
                Toast.makeText(mContext, "Ui杀死成功", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 0x909) {
                Toast.makeText(mContext, "发生了错误，任务没有完成,请去安全中心查看BossTest是否成功获得Root权限", Toast.LENGTH_SHORT).show();
            } else {
                if (msg.getData() != null && msg.what == 0) {
                    Log.d(TAG, "msg = " + msg.getData());
                    Log.d(TAG, "msg.what = " + msg.what);
                    Bundle bundle = msg.getData();
                    String result = bundle.getString("content");
                    if (result == null || result.isEmpty()) {
                        Toast.makeText(mContext, "试着把Dp的进程杀死，然后再打开Ui，保证有你想要的", Toast.LENGTH_LONG).show();
                    } else {
                        int startIndex = result.lastIndexOf("libVersion");
                        String subStr = result.substring(startIndex);
                        mTvShowLog.setText(subStr);
                        mTvShowLog.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    private class WorkingHandler extends Handler {

        public WorkingHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "workingHandler at Thread" + Thread.currentThread().getName());
            Bundle bundle = msg.getData();
            int taskCount = bundle.getInt("TaskCount");
            makeSnackBar(mTvInsert, "插入" + taskCount + "条任务" + "完成", Snackbar.LENGTH_SHORT).show();
            mMainHandler.sendEmptyMessage(0x111);
        }
    }

    private void postTask(final int downloadCount) {
        mWorkingHandler.post(new Runnable() {
            @Override
            public void run() {
                Thread working = Thread.currentThread();
                Log.d(TAG, "threadName = " + working.getName());
                boolean isSuccess = insertDownloadTask(downloadCount);
                if (isSuccess) {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putInt("TaskCount", downloadCount);
                    message.setData(bundle);
                    mWorkingHandler.sendMessage(message);
                }
            }
        });
    }

    private boolean insertDownloadTask(int count) {
        boolean success = false;
        int alreadyInsertCount = 0;
        SQLiteDatabase database = mContext.openOrCreateDatabase(SQLiteManager.DB_NAME, Context.MODE_PRIVATE, null);
        String tableName = getRandomTable();
        Log.d(TAG, "tableName = " + tableName);
        Cursor c = database.query(
                tableName,
                new String[]{SQLiteManager.DatabaseConstant.COLUMN_ID, SQLiteManager.DatabaseConstant.COLUMN_URL},
                null,
                null,
                null,
                null,
                null);
        Log.d(TAG, "Cursor, position = " + c.getPosition());
        final int urlIndex = c.getColumnIndex(SQLiteManager.DatabaseConstant.COLUMN_URL);
        String url;
        Uri uri;
        DownloadManager.Request request;
        int totalTask = c.getCount();

        if (totalTask < count) {
            count = totalTask;
        } else {
            Random random = new Random();
            int randomPosition = random.nextInt(totalTask - count);
            c.moveToPosition(randomPosition);
            Log.d(TAG, "c.moveToPostion = " + randomPosition);
        }

        while (c.moveToNext()) {
            url = c.getString(urlIndex);
            uri = Uri.parse(url);
            request = new DownloadManager.Request(uri);
            mDownloadManger.enqueue(request);
            alreadyInsertCount++;
            if (alreadyInsertCount == count) {
                success = true;
                break;
            }
        }
        Log.d(TAG, "Cursor position = " + c.getPosition());
        Log.d(TAG, "cursor count = " + c.getCount());
        c.close();
        return success;
    }

    private void removeFailedDownloadTask() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(DownloadManager.STATUS_FAILED);
        Cursor cursorFailed = mDownloadManger.query(query);
        int failedCount = cursorFailed.getCount();
        long[] ids = new long[failedCount];
        int index = 0;
        while (cursorFailed.moveToNext()) {
            int downloadId = cursorFailed.getInt(cursorFailed.getColumnIndex(DownloadManager.COLUMN_ID));
            ids[index] = downloadId;
            index++;
        }
        Log.d(TAG, "cursorFailed count = " + cursorFailed.getCount());
        if (failedCount != 0) {
            int temp = mDownloadManger.remove(ids);
            Log.d(TAG, "temp = " + temp);
            makeSnackBar(mTvRemoveFailed, "成功删除" + temp + "条失败任务", Snackbar.LENGTH_SHORT).show();
        } else {
            makeSnackBar(mTvRemoveFailed, "不好意思，现在没有失败的任务可供删除", Snackbar.LENGTH_LONG).show();
        }
    }


    private String getRandomTable() {
        String table;
        Random random = new Random();
        int randomIndex = random.nextInt(7);
        switch (randomIndex) {
            case 0:
                table = SQLiteManager.DatabaseConstant.TABLE_NAME_AD;
                break;
            case 1:
                table = SQLiteManager.DatabaseConstant.TABLE_NAME_ED2K;
                break;
            case 2:
                table = SQLiteManager.DatabaseConstant.TABLE_NAME_FTP;
                break;
            case 3:
                table = SQLiteManager.DatabaseConstant.TABLE_NAME_HTTP;
                break;
            case 4:
                table = SQLiteManager.DatabaseConstant.TABLE_NAME_HTTPS;
                break;
            case 5:
                table = SQLiteManager.DatabaseConstant.TABLE_NAME_MAGNET;
                break;
            case 6:
                table = SQLiteManager.DatabaseConstant.TABLE_NAME_MARKET;
                break;
            default:
                table = null;
                break;
        }
        return table;
    }

    private Snackbar makeSnackBar(View view, String text, int duration) {
        Snackbar temp = Snackbar.make(view, text, duration);
        View snackBarParent = temp.getView(); //SnackBar的View
        TextView message = (TextView) snackBarParent.findViewById(R.id.snackbar_text);
        message.setTextColor(ActivityCompat.getColor(mContext, R.color.color_guide_txt_white));
        snackBarParent.setBackgroundColor(ActivityCompat.getColor(mContext, R.color.colorRed));
        return temp;
    }

}
