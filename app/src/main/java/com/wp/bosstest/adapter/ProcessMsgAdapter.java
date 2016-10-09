package com.wp.bosstest.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wp.bosstest.R;
import com.wp.bosstest.utils.LogHelper;

/**
 * Created by cadi on 2016/9/26.
 */

public class ProcessMsgAdapter extends BaseAdapter {
    private final static String TAG = LogHelper.makeTag(ProcessMsgAdapter.class);
    private String[] processMsg;
    private String[] titles = {"UserId", "ProcessId", "占用内存", "进程类型", "运行包"};
    private LayoutInflater layoutInflater;

    public ProcessMsgAdapter(String[] processMsg, Context context) {
        super();
        this.processMsg = processMsg;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return processMsg.length;
    }

    @Override
    public Object getItem(int position) {
        return processMsg[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView title;
        TextView content;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_process_detail_lv_item, null);
            title = (TextView) convertView.findViewById(R.id.layout_process_detail_tv_title);
            content = (TextView) convertView.findViewById(R.id.layout_process_detail_tv_content);
            ViewCache viewCache = new ViewCache();
            viewCache.title = title;
            viewCache.content = content;
            convertView.setTag(viewCache);
        } else {
            ViewCache viewCache = (ViewCache) convertView.getTag();
            title = viewCache.title;
            content = viewCache.content;
        }
        title.setText(titles[position]);
        content.setText(processMsg[position]);
        Log.d(TAG, "position = " + position);
        return convertView;
    }

    private final class ViewCache {
        public TextView title;
        public TextView content;
    }

}
