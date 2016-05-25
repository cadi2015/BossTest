package com.wp.bosstest.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wp.bosstest.R;

/**
 * Created by cadi on 2016/5/20.
 */
public class FragmentSplash extends Fragment {
    private View mRootView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_splash, container, false);
        return  mRootView;
    }
}
