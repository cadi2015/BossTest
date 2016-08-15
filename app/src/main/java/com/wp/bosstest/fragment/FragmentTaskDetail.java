package com.wp.bosstest.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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

import com.wp.bosstest.R;
import com.wp.bosstest.config.IntentActionConstant;
import com.wp.bosstest.utils.ReadExcel;

import java.util.List;

/**
 * Created by cadi on 2016/6/22.
 */
public class FragmentTaskDetail extends Fragment {
    private static final String TAG = "FragmentTaskDetail";
    private View mRootView;
    private ListView mLvShow;
    private Context mContext;
    private List<String> mListOfExcel;
    private int mRawRes;
    private ClipboardManager mClipboardManager;
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
        mRawRes = selectRaw(((Activity)mContext).getIntent());
        mListOfExcel = readExcel.readFirstCell(mRawRes);
        mClipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    private int selectRaw(Intent intent) {
        if (intent != null) {
            switch (intent.getAction()) {
                case IntentActionConstant.TASK_DETAIL_ACTION_HTTP:
                    return R.raw.http;
                case IntentActionConstant.TASK_DETAIL_ACTION_ED2K:
                    return R.raw.ed2k;
                case IntentActionConstant.TASK_DETAIL_ACTION_THUNDER:
                    return R.raw.thunder;
                case IntentActionConstant.TASK_DETAIL_ACTION_FTP:
                    return R.raw.ftp;
                case IntentActionConstant.TASK_DETAIL_ACTION_MAGNET:
                    return R.raw.magnet;
                default:
                    break;
            }
        }
        return 0;
    }

    private void setUpViews() {
        mLvShow = (ListView) mRootView.findViewById(R.id.task_detail_lv);
        mLvShow.setAdapter(new MyListAdapter(mListOfExcel));
        mLvShow.setOnItemClickListener(new MyOnItemClickLis());
        mLvShow.setOnScrollListener(new MyOnScrLis());
    }

    private class MyOnScrLis implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            Log.d(TAG, "scrollState = " + scrollState);
        }
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            Log.d(TAG, "firstViewsibleItem = " + firstVisibleItem + ",visibleItemCount = " + visibleItemCount + ",totalItemCount = " + totalItemCount);
        }
    }

    private class MyOnItemClickLis implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView tv = (TextView)view.findViewById(R.id.task_detail_lv_item_tv_show);
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
            if(convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.layout_task_detail_lv_item, null);
                holder = new ViewHolder();
                holder.textView = (TextView)convertView.findViewById(R.id.task_detail_lv_item_tv_show);
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
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }
}
