package com.wp.bosstest.fragment;


import android.app.DownloadManager;
import android.content.Context;
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
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wp.bosstest.R;
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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_performance, null);
        init();
        initWorking();
        setupViews();
        Log.d(TAG, "onCreateView at Thread " + Thread.currentThread().getName());
        return mRootView;
    }

    private void init() {
        mContext = getActivity();
        mDownloadManger = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        mMainHandler = new MainHandler();
    }

    private void setupViews() {
        mSeekBar = (SeekBar) mRootView.findViewById(R.id.performance_seek_bar);
        mTvInsert = (TextView) mRootView.findViewById(R.id.performance_tv_insert);
        mTvInsert.setText(getString(R.string.performance_tv_task, mSeekBar.getProgress()));
        mSeekBar.setFocusable(true);
        mProgressBar = (ProgressBar) mRootView.findViewById(R.id.performance_progress_bar);
        mTvInsert.setOnClickListener(new MyClickLis());
        mSeekBar.setOnSeekBarChangeListener(new MySeekBarLis());

    }

    private class MySeekBarLis implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

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
                Snackbar.make(mTvInsert, "任务不能为0", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void initWorking() {
        if (mWorkingThread == null) {
            mWorkingThread = new HandlerThread("taskWorking"); //工作线程
            mWorkingThread.start();
        }
        mWorkingHandler = new WorkingHandler(mWorkingThread.getLooper());//Handler的post方法总结，将Runnable发送给绑定的线程运行（如果在ui线程，就发给ui线程，如果在工作线程，就发给工作线程）
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
            Snackbar.make(mTvInsert, "插入" + taskCount + "条任务" + "完成", Snackbar.LENGTH_SHORT).show();
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
        }

        while (c.moveToNext()) {
            url = c.getString(urlIndex);
            uri = Uri.parse(url);
            request = new DownloadManager.Request(uri);
            mDownloadManger.enqueue(request);
            if (c.getPosition() == count - 1) {
                success = true;
                break;
            }
        }
        Log.d(TAG, "Cursor position = " + c.getPosition());
        Log.d(TAG, "cursor count = " + c.getCount());
        c.close();
        return success;
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
}
