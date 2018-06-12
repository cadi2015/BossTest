package com.wp.cheez.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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
        setupViews();
        return mRootView;
    }

    private void setupViews() {
        mWebView = (WebView) mRootView.findViewById(R.id.web_web_view);
        mWebView.loadUrl(mJumpUrl);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
    }

}
