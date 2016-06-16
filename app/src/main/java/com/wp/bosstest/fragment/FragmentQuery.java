package com.wp.bosstest.fragment;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wp.bosstest.R;

/**
 * Created by wp on 2016/1/18.
 */
public class FragmentQuery extends Fragment {
    private static final String TAG = "FragmentQuery";
    private View mRootView;
    private Button mBtnQuery;
    private Activity mActivity;
    public static Fragment mFrament;

    public static Fragment newInstance() {
        mFrament = new FragmentQuery();
        return mFrament;
    }

    private FragmentQuery() {
        super();
        Log.d(TAG, "FragmentQuery(int index)");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "FragmentQuery onCreate(Bundle savedInstanceState)");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "FragmentQuery onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState");
        mRootView = inflater.inflate(R.layout.fragment_query, null, false);
        return mRootView;
    }


    //该方法会在所在的Activity onCreate方法后调用
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "FragmentQuery onActivityCreated(Bundle savedInstanceState)");
        setupViews();
        init();
    }

    private void setupViews() {
        mBtnQuery = (Button) mRootView.findViewById(R.id.home_query_btn_query);

        MyClickLis myClickLis = new MyClickLis();
        mBtnQuery.setOnClickListener(myClickLis);
    }

    private void init() {
        mActivity = getActivity();
    }

    class MyClickLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.home_query_btn_query:
                    String temp = "ed2k://|file| \n" +
                            "\n" +
                            "[迅雷下载www.DY123.cc]真心英雄.BD1280高清国粤双语中字.mp4|1191888666|8D05E0620D46BF13289AB32DF1821F28|h=FFRMNFIUREQRDY6ONW3HZM3TCXRT4RKT|/";
                    ClipboardManager clipboardManager = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData  = ClipData.newPlainText("simple", temp);
                    clipboardManager.setPrimaryClip(clipData);
                    break;
                default:
                    break;
            }
        }
    }

    private String getColumnString(Cursor c, String tableColumn) {
        return (c.getString(c.getColumnIndex(tableColumn)) + "\n");
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "FragmentQuery onResunme()");
    }
}
