package com.wp.cheez.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.wp.cheez.R;
import com.wp.cheez.utils.LogHelper;

/**
 * Created by cadi on 2016/8/23.
 */
public class FragmentWeb extends Fragment {
    private final static String TAG = LogHelper.makeTag(FragmentWeb.class);
    private View mRootView;
    private WebView mWebView;
    private String mJumpUrl;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_web, null);
        Bundle bundle = getArguments();
        mJumpUrl = bundle.getString("webUrl");
        Log.d(TAG, "mJumpUrl == " + mJumpUrl);
        setupViews();
        return mRootView;
    }

    private void setupViews() {
        mWebView = (WebView) mRootView.findViewById(R.id.web_web_view);
        mWebView.requestFocus();
        WebSettings settings = mWebView.getSettings();
        settings.setSupportZoom(true);          //支持缩放
        settings.setBuiltInZoomControls(false);  //启用内置缩放装置
        settings.setJavaScriptEnabled(true);    //启用JS脚本
        mWebView.loadUrl(mJumpUrl);
    }

}
