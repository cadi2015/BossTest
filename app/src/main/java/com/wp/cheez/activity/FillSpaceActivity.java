package com.wp.cheez.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

/**
 * 填充空间功能（必须回归项目之一，so库会经常更新）
 */
public class FillSpaceActivity extends BaseActivity {
    private static String TAG = LogHelper.makeTag(FillSpaceActivity.class);
    private Button mBtnStart;
    private Button mBtnStop;
    private Button mBtnClean;
    private TextView mTvSpaceSize;
    private ProgressBar mPb;
    private String fillFileName = "temp";
    private int delayTime = 3000;
    private MyHandler mHandler;
    private boolean mLoopStop = false;
    private MyFileTask myFileTask;

    /**
     * 静态内部类，不去隐式的持有外部类对象的引用
     */
    private static class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();
    }

    private void setupViews(){
        mBtnStart = (Button) findViewById(R.id.btn_start);
        mBtnStop = (Button) findViewById(R.id.btn_stop);
        mBtnClean = (Button)findViewById(R.id.btn_clean);
        mPb = (ProgressBar) findViewById(R.id.p_bar_file_write);
        MyBtnClickLis myBtnClickLis = new MyBtnClickLis();
        mTvSpaceSize = (TextView)findViewById(R.id.tv_space_size);
        updateSpaceSize();
        mBtnStart.setOnClickListener(myBtnClickLis);
        mBtnStop.setOnClickListener(myBtnClickLis);
        mBtnClean.setOnClickListener(myBtnClickLis);
    }

    private void updateSpaceSize(){
        mTvSpaceSize.setText(DeviceUtil.spaceSizeWithUnit(this, DeviceUtil.getFreeExternalMemory()));
    }

    private class MyRunnable implements Runnable{
        @Override
        public void run() {
            updateSpaceSize();
            if(!mLoopStop) {
                mHandler.postDelayed(this, delayTime);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(myFileTask != null ) {
            myFileTask.cancel(true);
            mBtnClean.setEnabled(true);
        }
        mLoopStop = true;
    }

    /**
     * 我这里一开犯的错误就是，我用同一个AsyncTask对象想执行多次
     */
    private class MyBtnClickLis implements View.OnClickListener{
         //AsyncTask的一个对象，就只能执行一次，Google牛逼，想多次执行，只能再new一个AsyncTask出来了
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_start:
                    myFileTask = new MyFileTask();

                    Log.d(TAG, "AsyncTask getStatus() == " + myFileTask.getStatus());
                    if(myFileTask.getStatus() == AsyncTask.Status.PENDING){
                        myFileTask.execute();
                    }
                    mHandler = new MyHandler();
                    mHandler.postDelayed(new MyRunnable(), delayTime);
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
                    boolean success = deleteFile(fillFileName);
                    if(success){
                        updateSpaceSize();
                    }
                    break;
            }
        }
    }




    private class MyFileTask extends AsyncTask<Void,Void,Boolean>{
        //工作线程开始前，先调用的方法
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBtnStart.setVisibility(View.GONE);
            mBtnStop.setVisibility(View.VISIBLE);
        }

        //工作线程执行完后，调用的方法
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mBtnStop.setVisibility(View.GONE);
            mBtnStart.setVisibility(View.VISIBLE);
            mBtnClean.setEnabled(true);
            mPb.setVisibility(View.GONE);
        }

        //工作线程发出的更新，该方法在ui线程执行
        @Override
        protected void onProgressUpdate(Void... values) {
        }

        //在工作线程中执行的方法，哈哈，写入文件就在这里
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                int baseLimitWithByte = 500;
                FileOutputStream fileOutputStream = openFileOutput(fillFileName, Context.MODE_APPEND);
                int base = 4096;
                int variable = 1;
                byte[] bytes = new byte[base];
                while (!isCancelled() && DeviceUtil.getFreeExternalMemory() > baseLimitWithByte){
                    if(isCancelled()){
                        break;
                    }
                    fileOutputStream.write(bytes);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateSpaceSize();
                        }
                    });
                }
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }

        //这个肯定是调用将AsyncTask的onCancelled后，回调的方法
        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);
            mBtnStop.setVisibility(View.GONE);
            mBtnStart.setVisibility(View.VISIBLE);
            mPb.setVisibility(View.GONE);
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
