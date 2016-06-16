package com.wp.bosstest.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wp.bosstest.R;

/**
 * Created by wp on 2016/1/18.
 */
public class FragmentTask extends Fragment {
    private static final String TAG = "FragmentTask";
    private View mRootView;
    private Activity mActivity;
    private ListView mlvShowUrl;
    ClipboardManager mClipboardManager;
    public static Fragment mFrament;

    public static Fragment newInstance() {
        if (mFrament == null) {
            mFrament = new FragmentTask();
        }
        return mFrament;
    }

    private FragmentTask() {
        super();
        Log.d(TAG, "FragmentTask(int index)");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "FragmentTask onCreate(Bundle savedInstanceState)");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "FragmentTask onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState");
        mRootView = inflater.inflate(R.layout.fragment_task, null, false);
        return mRootView;
    }


    //该方法会在所在的Activity onCreate方法后调用
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "FragmentTask onActivityCreated(Bundle savedInstanceState)");
        setupViews();
        init();
    }

    private void setupViews() {
        mlvShowUrl = (ListView)mRootView.findViewById(R.id.task_lv_show_url);
        String[] temp = {"http://bt.2tu.cc/5EE49B5834F061E86754764810EA899202E62EE2/[迅雷下载www.2tu.cc]致美丽的你EP04.rmvb", "ftp://dygod3:dygod3@y069.dydytt.net:6261/美女的诞生/[阳光电影-www.ygdy8.com]美女的诞生-01.rmvb",
        "http://bt.box.n0808.com/FB/FE/FB5260D4444945B4DC604D09A640B6AE6DF5BFFE.torrent", "ed2k://|file|[迅雷下载www.DY123.cc]真心英雄.BD1280高清国粤双语中字.mp4|1191888666|8D05E0620D46BF13289AB32DF1821F28|h=FFRMNFIUREQRDY6ONW3HZM3TCXRT4RKT|/"
        , "thunder://QUFmdHA6Ly81NToxMjM0QGY0LmdibC56dWliZW4uY29tOjY4MjEvMjAxNS8wNS8xMi96dWliZW7K1rv6tefTsHp1aWJlbi5jb23Rp9CjMjAxNS0wNVtoZDQ4MHBdLm1wNFpa"
        ,"magnet:?xt=urn:btih:E8507F9F4CB0BDD2A3D236326F063C7131B0344B"
        ,"https://app.233.com/android/examda_150718.26_2.1.4.apk"};
        mlvShowUrl.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.layout_task_lv_item, temp));
        mlvShowUrl.setOnItemClickListener(new MyLvOnItemLis());
    }

    private class MyLvOnItemLis implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView tv = (TextView) view;
            ClipData clipData = ClipData.newPlainText("simple", tv.getText().toString());
            mClipboardManager.setPrimaryClip(clipData);
            Toast.makeText(mActivity, "复制成功，请去下载管理新建中使用吧", Toast.LENGTH_LONG).show();
        }
    }

    private void init() {
        mActivity = getActivity();
        mClipboardManager = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "FragmentTask onResunme()");
    }
}
