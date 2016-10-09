package com.wp.bosstest.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.premnirmal.Magnet.Magnet;
import com.wp.bosstest.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cadi on 2016/5/31.
 */
public class FragmentPhone extends Fragment {
    private View mRootView;
    private ListView mListView;
    private TelephonyManager mTelManager;
    private Activity mActivity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_phone, container, false);
        setupViews();
        init();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), getData(), R.layout.layout_phone_lv_item, new String[]{"title", "content"},
                new int[]{R.id.layout_phone_title, R.id.layout_phone_content});
        mListView.setAdapter(simpleAdapter);
        mListView.setClickable(false);
        mListView.setEnabled(false);
    }

    private List<Map<String, String>> getData() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        String deviceId;
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            deviceId = "没有权限";
        } else {
            deviceId = mTelManager.getDeviceId();
        }

        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        float screenDensity = dm.density;
        int screenDensityDpi = dm.densityDpi;
        float screenScaledDensity = dm.scaledDensity;
        list.add(getMap("DeviceId(imei) :", deviceId));
        list.add(getMap("Android Version :", Build.VERSION.RELEASE));
        list.add(getMap("Api Level :", String.valueOf(Build.VERSION.SDK_INT)));
        list.add(getMap("Device :", Build.DEVICE));
        list.add(getMap("Model :", Build.MODEL));
        list.add(getMap("Screen Width :", String.valueOf(screenWidth)));
        list.add(getMap("Screen Height :", String.valueOf(screenHeight)));
        list.add(getMap("Screen Density :", String.valueOf(screenDensity)));
        list.add(getMap("Screen DensityDpi :", String.valueOf(screenDensityDpi)));
        list.add(getMap("Screen ScaledDensity :", String.valueOf(screenScaledDensity)));
        return list;
    }

    private Map<String, String> getMap(String title, String content) {
        Map<String, String> map = new HashMap<>();
        map.put("title", title);
        map.put("content", content);
        return map;
    }


    private void setupViews() {
        mListView = (ListView) mRootView.findViewById(R.id.phone_lv_show);
    }


    private void init() {
        mTelManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        mActivity = getActivity();
    }


}
