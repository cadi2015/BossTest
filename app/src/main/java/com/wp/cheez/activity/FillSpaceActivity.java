package com.wp.cheez.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wp.cheez.R;
import com.wp.cheez.utils.DeviceUtil;
import com.wp.cheez.utils.LogHelper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * 填充空间功能（必须回归项目之一，so库会经常更新）
 */
public class FillSpaceActivity extends BaseActivity {
    private static final String TAG = LogHelper.makeTag(FillSpaceActivity.class);
    private Button mBtnStart;
    private Button mBtnStop;
    private Button mBtnClean;
    private TextView mTvSpaceSize;
    private ProgressBar mPb;
    private static final String FILL_FILE_NAME = "temp";
    private static final int DELAY_TIME = 3000;
    private Handler mHandler;
    private boolean mLoopStop = false;
    private MyFileTask myFileTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();
        mHandler = new Handler(Looper.getMainLooper());
    }

    private void setupViews() {
        mBtnStart = (Button) findViewById(R.id.btn_start);
        mBtnStop = (Button) findViewById(R.id.btn_stop);
        mBtnClean = (Button) findViewById(R.id.btn_clean);
        mPb = (ProgressBar) findViewById(R.id.p_bar_file_write);
        MyBtnClickLis myBtnClickLis = new MyBtnClickLis();
        mTvSpaceSize = (TextView) findViewById(R.id.tv_space_size);
        updateSpaceSize();
        mBtnStart.setOnClickListener(myBtnClickLis);
        mBtnStop.setOnClickListener(myBtnClickLis);
        mBtnClean.setOnClickListener(myBtnClickLis);
    }

    private void updateSpaceSize() {
        mTvSpaceSize.setText(DeviceUtil.spaceSizeWithUnit(this, DeviceUtil.getFreeExternalMemory()));
    }

    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            updateSpaceSize();
            if (!mLoopStop) {
                mHandler.postDelayed(this, DELAY_TIME);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 我这里一开犯的错误就是，我用同一个AsyncTask对象想执行多次
     */
    private class MyBtnClickLis implements View.OnClickListener {

        //AsyncTask的一个对象，就只能执行一次，Google牛逼，想多次执行，只能再new一个AsyncTask出来了
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_start:
                    myFileTask = new MyFileTask(mBtnStart, mBtnStop, mBtnClean, mPb, FillSpaceActivity.this);
                    Log.d(TAG, "AsyncTask getStatus() == " + myFileTask.getStatus());
                    if (myFileTask.getStatus() == AsyncTask.Status.PENDING) {
                        myFileTask.execute();
                    }
                    mHandler.postDelayed(new MyRunnable(), DELAY_TIME);
                    mPb.setVisibility(View.VISIBLE);
                    mBtnClean.setEnabled(false);
                    break;
                case R.id.btn_stop:
                    myFileTask.cancel(true);
                    mPb.setVisibility(View.GONE);
                    mBtnClean.setEnabled(true);
                    mLoopStop = true;
                    break;
                case R.id.btn_clean:
                    boolean success = deleteFile(FILL_FILE_NAME);
                    if (success) {
                        updateSpaceSize();
                    }
                    break;
            }
        }
    }


    private static class MyFileTask extends AsyncTask<Void, Void, Boolean> {
        //工作线程开始前，先调用的方法
        private final WeakReference<Button> btnStartReference;
        private final WeakReference<Button> btnStopReference;
        private final WeakReference<Button> btnCleanReference;
        private final WeakReference<ProgressBar> pbReference;
        private final WeakReference<FillSpaceActivity> activityReference;

        public MyFileTask(Button btnStart, Button btnStop, Button btnClean, ProgressBar pb, FillSpaceActivity activity) {
            btnStartReference = new WeakReference<>(btnStart);
            btnStopReference = new WeakReference<>(btnStop);
            btnCleanReference = new WeakReference<>(btnClean);
            pbReference = new WeakReference<>(pb);
            activityReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btnStartReference.get().setVisibility(View.GONE);
            btnStopReference.get().setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            btnStopReference.get().setVisibility(View.GONE);
            btnStartReference.get().setVisibility(View.VISIBLE);
            btnCleanReference.get().setEnabled(true);
            pbReference.get().setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            FileOutputStream fileOutputStream;
            try {
                int baseLimitWithByte = 100000;
                fileOutputStream = activityReference.get().openFileOutput(FILL_FILE_NAME, Context.MODE_APPEND);
                int base = 4800;
                int i = 0;
                byte[] bytes = new byte[base];
                while (true) {
                    if (isCancelled()) {
                        break;
                    }
                    fileOutputStream.write(bytes);
                    if (i == 10000000) {
                        activityReference.get().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activityReference.get().updateSpaceSize();
                            }
                        });
                        i = 0;
                        if (DeviceUtil.getFreeExternalMemory() < baseLimitWithByte) {
                            break;
                        }
                    }
                    i++;
                }
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            return true;
        }

        //这个肯定是调用将AsyncTask的onCancelled后，回调的方法
        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);
            btnStopReference.get().setVisibility(View.GONE);
            btnStartReference.get().setVisibility(View.VISIBLE);
            pbReference.get().setVisibility(View.GONE);
        }
    }


    @Override
    public boolean isDisplayHomeAsUp() {
        return true;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_fill_space;
    }

    @Override
    public boolean isDisplayToolBar() {
        return true;
    }

    @Override
    public String getToolBarTitle() {
        return "填充空间";
    }

}
