package com.wp.cheez.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wp.cheez.R;
import com.wp.cheez.config.IntentActionConstant;
import com.wp.cheez.utils.ReadExcel;

import java.util.List;

/**
 * Created by cadi on 2016/6/22.
 * 不用协议的下载链接
 *  http
 *  ed2k
 *  thunder
 *  ftp
 *  magnet
 *  various
 */
public class FragmentTaskDetail extends Fragment {
    private static final String TAG = "FragmentTaskDetail";
    private View mRootView;
    private ListView mLvShow;
    private Context mContext;
    private List<String> mListOfExcel;
    private int mRawRes;
    private int mRawType;  // 0 http 1 ed2k 2 thunder 3 ftp 4 magnet 5 various
    private ClipboardManager mClipboardManager;
    private SharedPreferences mSharedPreference;
    private String mLvPositionKey;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_task_detail, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated(View view, Bundle savedInstanceState)");
        init();
        setUpViews();
    }

    private void init() {
        mContext = getActivity();
        ReadExcel readExcel = new ReadExcel(mContext);
        mRawRes = selectRaw(((Activity) mContext).getIntent()); //我巧妙的使用了Intent，当年，真青涩……
        mListOfExcel = readExcel.readFirstCell(mRawRes);
        mClipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        mLvPositionKey = getListViewKey(mRawType);
        mSharedPreference = mContext.getSharedPreferences("boss_config", Context.MODE_PRIVATE);
    }

    private int selectRaw(Intent intent) {
        if (intent != null) {
            switch (intent.getAction()) {
                case IntentActionConstant.TASK_DETAIL_ACTION_HTTP:
                    mRawType = 0;
                    return R.raw.http;
                case IntentActionConstant.TASK_DETAIL_ACTION_ED2K:
                    mRawType = 1;
                    return R.raw.ed2k;
                case IntentActionConstant.TASK_DETAIL_ACTION_THUNDER:
                    mRawType = 2;
                    return R.raw.thunder;
                case IntentActionConstant.TASK_DETAIL_ACTION_FTP:
                    mRawType = 3;
                    return R.raw.ftp;
                case IntentActionConstant.TASK_DETAIL_ACTION_MAGNET:
                    mRawType = 4;
                    return R.raw.magnet;
                case IntentActionConstant.TASK_DETAIL_ACTION_VARIOUS:
                    mRawType = 5;
                    return R.raw.various;
                default:
                    break;
            }
        }
        return 0;
    }

    private void setUpViews() {
        mLvShow = mRootView.findViewById(R.id.task_detail_lv);
        mLvShow.setAdapter(new MyListAdapter(mListOfExcel));
        mLvShow.setOnItemClickListener(new MyOnItemClickLis());
        mLvShow.setOnScrollListener(new MyOnScrLis());
        mLvShow.setSelection(mSharedPreference.getInt(mLvPositionKey, 0));
    }

    private class MyOnScrLis implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            Log.d(TAG, "scrollState = " + scrollState);
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            Log.d(TAG, "firstVisibleItem = " + firstVisibleItem + ",visibleItemCount = " + visibleItemCount + ",totalItemCount = " + totalItemCount);
        }
    }

    private class MyOnItemClickLis implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView tv = (TextView) view.findViewById(R.id.task_detail_lv_item_tv_show);
            Log.d(TAG, "view = " + view);
            ClipData clipData = ClipData.newPlainText("simple", tv.getText().toString());
            mClipboardManager.setPrimaryClip(clipData);
            Toast.makeText(mContext, R.string.copy_success, Toast.LENGTH_LONG).show();
        }
    }

    private class MyListAdapter extends BaseAdapter {
        private List<String> mAllUrls;
        private LayoutInflater mLayoutInflater;

        public MyListAdapter(List<String> list) {
            mAllUrls = list;
            mLayoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mAllUrls.size();
        }

        @Override
        public Object getItem(int position) {
            return mAllUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.layout_task_detail_lv_item, null);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.task_detail_lv_item_tv_show);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(mAllUrls.get(position));
            return convertView;
        }
    }

    private static class ViewHolder {
        public TextView textView;
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "listView position = " + mLvShow.getFirstVisiblePosition());
        int listViewLastPosition = mLvShow.getFirstVisiblePosition();
        putListViewPosition(listViewLastPosition);
    }

    private String getListViewKey(int rawType){
        String putKey;
        switch (rawType) {
            case 0:
                putKey = "lv_position_http_num0";
                break;
            case 1:
                putKey = "lv_position_ed2k_num1";
                break;
            case 2:
                putKey = "lv_position_thunder_num2";
                break;
            case 3:
                putKey = "lv_position_ftp_num3";
                break;
            case 4:
                putKey = "lv_position_magnet_num4";
                break;
            case 5:
                putKey = "lv_position_various_num5";
                break;
            default:
                putKey = null;
                break;
        }
        return putKey;
    }

    // 0 http 1 ed2k 2 thunder 3 ftp 4 magnet 5 various
    private void putListViewPosition(int lastPosition) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        if (mLvPositionKey != null) {
            editor.putInt(mLvPositionKey, lastPosition);
            editor.apply();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }
}
