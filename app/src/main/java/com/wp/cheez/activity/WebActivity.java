package com.wp.cheez.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.wp.cheez.R;
import com.wp.cheez.fragment.FragmentWeb;
import com.wp.cheez.utils.LogHelper;

/**
 * Created by cadi on 2016/8/23.
 */
public class WebActivity extends BaseActivity {
    private final static String TAG = LogHelper.makeTag(WebActivity.class);
    @Override
    public boolean isDisplayHomeAsUp() {
        return true;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_web;
    }

    @Override
    public boolean isDisplayToolBar() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentWeb fragmentWeb =  new FragmentWeb();
        Intent intent = getIntent();
        String url = intent.getStringExtra("jumpUrl");
        Log.d(TAG, "url == " + url);
        Bundle bundle = new Bundle();
        bundle.putString("webUrl", url);
        fragmentWeb.setArguments(bundle);
        fragmentManager.beginTransaction().add(R.id.web_frame_layout, fragmentWeb).commit();
    }

    @Override
    public String getToolBarTitle() {
        return "网页展示";
    }
}
