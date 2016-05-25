package com.wp.bosstest.fragment;

import android.app.DownloadManager;
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
    private long tempId;
    DownloadManager downloadManager;
    DownloadManager.Request request;
    DownloadManager.Query query;
    private TextView mTVshow;
    private Button mBtnQuery;
    private Handler mHandler;
    private String mShow;
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
        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        request = new DownloadManager.Request(Uri.parse("http://f1.market.mi-img.com/download/AppStore/0cfcd48017925b291a1c1c29eb2f6022437417228/%E8%8A%92%E6%9E%9CTV_4.5.0_56.apk"));
        mHandler = new Myhandler();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "FragmentQuery onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState");
        View view = null;
        view = inflater.inflate(R.layout.fragment_query, null, false);
        return view;
    }


    //该方法会在所在的Activity onCreate方法后调用
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "FragmentQuery onActivityCreated(Bundle savedInstanceState)");
        request.setDestinationInExternalPublicDir("temp", "ceshi.apk");
        request.setVisibleInDownloadsUi(true);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Button insert = (Button) getActivity().findViewById(R.id.home_query_btn_insert);
//        EditText input = (EditText)getActivity().findViewById(R.id.home_query_input);
        mTVshow = (TextView) getActivity().findViewById(R.id.home_query_show);
        mBtnQuery = (Button) getActivity().findViewById(R.id.home_query_btn_query);

        View.OnClickListener myClick = new MyClickLis();
        insert.setOnClickListener(myClick);
        mBtnQuery.setOnClickListener(myClick);
    }

    class MyClickLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.home_query_btn_insert:
                    tempId = downloadManager.enqueue(request);
                    Log.d(TAG, "tempId = " + tempId);
                    break;
                case R.id.home_query_btn_query:
                    query = new DownloadManager.Query().setFilterById(tempId);
                    Cursor c = downloadManager.query(query);
                    c.moveToFirst();
                    String temp = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                    StringBuilder sb = new StringBuilder();
                    sb.append("local_name : " + temp + "\n");
                    sb.append("id : " + getColumnString(c, DownloadManager.COLUMN_ID));
                    sb.append("来源 : " + c.getString(c.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION)) + "\n");
                    mHandler.sendEmptyMessage(0x111);
                    mShow = sb.toString();
                    break;
                default:
                    break;
            }
        }
    }

    private String getColumnString(Cursor c, String tableColumn) {
        return (c.getString(c.getColumnIndex(tableColumn)) + "\n");
    }

    private class Myhandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
             if (msg.what == 0x111) {
                mTVshow.setText(mShow);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "FragmentQuery onResunme()");
    }
}
