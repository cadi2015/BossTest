package com.wp.bosstest.fragment;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wp.bosstest.R;
import com.wp.bosstest.activity.ProcessDetailActivity;
import com.wp.bosstest.receiver.DownloadTaskReceiver;
import com.wp.bosstest.sqlite.SqliteManager;
import com.wp.bosstest.utils.LogHelper;

import java.util.Random;

/**
 * Created by cadi on 2016/8/13.
 */
public class FragmentPerformance extends Fragment {
    private final static String TAG = LogHelper.makeTag(FragmentPerformance.class);
    private View mRootView;
    private TextView mTvInsert;
    private static HandlerThread mWorkingThread;
    private Handler mWorkingHandler;
    private DownloadManager mDownloadManger;
    private Context mContext;
    private Handler mMainHandler;
    private SeekBar mSeekBar;
    private ProgressBar mProgressBar;
    private BroadcastReceiver mReceiver;
    private TextView mTvRemoveFailed;
    private Button mBtnProcessMsg;
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
        mDownloadManger = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        mMainHandler = new MainHandler();
        mReceiver = new DownloadTaskReceiver();
    }

    private void setupViews() {
        mSeekBar = (SeekBar) mRootView.findViewById(R.id.performance_seek_bar);
        mTvRemoveFailed = (TextView) mRootView.findViewById(R.id.performance_tv_remove_failed);
        mBtnProcessMsg = (Button) mRootView.findViewById(R.id.performance_btn_process_msg);
        mSeekBar.setProgress(20);
        mTvInsert = (TextView) mRootView.findViewById(R.id.performance_tv_insert);
        Log.d(TAG, "SeekBar current progress  = " + mSeekBar.getProgress());
        mTvInsert.setText(getString(R.string.performance_tv_task, mSeekBar.getProgress()));
        mProgressBar = (ProgressBar) mRootView.findViewById(R.id.performance_progress_bar);
        MyBtnCikLis myBtnCikLis = new MyBtnCikLis();
        mTvInsert.setOnClickListener(new MyClickLis());
        mSeekBar.setOnSeekBarChangeListener(new MySeekBarLis());
        mTvRemoveFailed.setOnClickListener(myBtnCikLis);
        mBtnProcessMsg.setOnClickListener(myBtnCikLis);
        Log.d(TAG, "mRootView.getParent() = " + mSeekBar.getParent());
    }

    private class MyBtnCikLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.performance_tv_remove_failed:
                    removeFailedDownloadTask();
                    break;
                case R.id.performance_btn_process_msg:
                    Log.d(TAG, "MyBtnCik, btn_process_msg");
                    Intent intent = new Intent(mContext, ProcessDetailActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated(Bundle savedInstanceState)");
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        mContext.registerReceiver(mReceiver, intentFilter);
    }

    private void unregisterReceiver() {
        mContext.unregisterReceiver(mReceiver);
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
            if (!fromUser) {
                int index = seekBar.getProgress();
                mTvInsert.setText(getString(R.string.performance_tv_task, index));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Log.d(TAG, "onStartTrackingTouch(SeekBar seekBar)");
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int index = seekBar.getProgress();
            mTvInsert.setText(getString(R.string.performance_tv_task, index));
        }
    }

    private class MyClickLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int index = mSeekBar.getProgress();
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
            if (msg.what == 0x111) {
                Log.d(TAG, "MainHandler handleMessage(Message msg) at Thread  = " + Thread.currentThread().getName());
                mTvInsert.setTextColor(ContextCompat.getColor(mContext, R.color.color_guide_txt_white));
                mTvInsert.setEnabled(true);
                mTvInsert.setClickable(true);
                mProgressBar.setVisibility(View.GONE);
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
        SQLiteDatabase database = mContext.openOrCreateDatabase(SqliteManager.DB_NAME, Context.MODE_PRIVATE, null);
        String tableName = getRandomTable();
        Log.d(TAG, "tableName = " + tableName);
        Cursor c = database.query(
                tableName,
                new String[]{SqliteManager.DatabaseConstant.COLUMN_ID, SqliteManager.DatabaseConstant.COLUMN_URL},
                null,
                null,
                null,
                null,
                null);
        Log.d(TAG, "Cursor, position = " + c.getPosition());
        final int urlIndex = c.getColumnIndex(SqliteManager.DatabaseConstant.COLUMN_URL);
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
                table = SqliteManager.DatabaseConstant.TABLE_NAME_AD;
                break;
            case 1:
                table = SqliteManager.DatabaseConstant.TABLE_NAME_ED2K;
                break;
            case 2:
                table = SqliteManager.DatabaseConstant.TABLE_NAME_FTP;
                break;
            case 3:
                table = SqliteManager.DatabaseConstant.TABLE_NAME_HTTP;
                break;
            case 4:
                table = SqliteManager.DatabaseConstant.TABLE_NAME_HTTPS;
                break;
            case 5:
                table = SqliteManager.DatabaseConstant.TABLE_NAME_MAGNET;
                break;
            case 6:
                table = SqliteManager.DatabaseConstant.TABLE_NAME_MARKET;
                break;
            default:
                table = null;
                break;
        }
        return table;
    }

    private Snackbar makeSnackBar(View view, String text, int duration) {
        Snackbar temp = Snackbar.make(view, text, duration);
        View snackBarParent = temp.getView();
        snackBarParent.setBackgroundColor(ActivityCompat.getColor(mContext, R.color.colorRed));
        return temp;
    }

}
