package com.wp.cheez.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.wp.cheez.R;
import com.wp.cheez.utils.DeviceUtil;
import com.wp.cheez.utils.ProcessUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * Created by cadi on 2016/5/31.
 */
public class FragmentPhone extends Fragment {
    private View mRootView;
    private ListView mListView;
    private TelephonyManager mTelManager;
    private TextView mTvShowMem;


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
    }

    private List<Map<String, String>> getData() {
        List<Map<String, String>> list = new ArrayList<>();
        String imei;
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.READ_PHONE_STATE},1);
        }
        imei = DeviceUtil.getImei(mActivity);
        int screenWidth = DeviceUtil.getScreenWidthPx(mActivity);
        int screenHeight = DeviceUtil.getScreenHeightPx(mActivity);
        float screenDensity = DeviceUtil.getScreenDensity(mActivity);
        int screenDensityDpi = DeviceUtil.getScreenDensityDpi(mActivity);
        float screenScaledDensity = DeviceUtil.getScreenScaledDensity(mActivity);
        list.add(getMap("Imei :", imei));
        list.add(getMap("Mcc :" , DeviceUtil.getMccByTM(mActivity,false)));
        list.add(getMap("Mcc Full:" , DeviceUtil.getMccByTM(mActivity,true)));
        list.add(getMap("Serial Number :",DeviceUtil.getSerialNumber(mActivity)));
        list.add(getMap("Android Device Id :", DeviceUtil.getAndroidDeviceId(mActivity)));
        list.add(getMap("Android Version :", Build.VERSION.RELEASE));
        list.add(getMap("Api Level :", String.valueOf(Build.VERSION.SDK_INT)));
        list.add(getMap("Device :", Build.DEVICE));
        list.add(getMap("Model :", Build.MODEL));
        list.add(getMap("Screen Width :", String.valueOf(screenWidth)));
        list.add(getMap("Screen Height :", String.valueOf(screenHeight)));
        list.add(getMap("Screen Density :", String.valueOf(screenDensity)));
        list.add(getMap("Screen DensityDpi :", String.valueOf(screenDensityDpi)));
        list.add(getMap("Screen ScaledDensity :", String.valueOf(screenScaledDensity)));
        list.add(getMap("Screen xdpi : " , String.valueOf(DeviceUtil.getScreenXdpi(mActivity))));
        list.add(getMap("Screen ydpi : " , String.valueOf(DeviceUtil.getScreenYdpi(mActivity))));
        list.add(getMap("Sdcard Free Space : ", DeviceUtil.spaceSizeWithUnit(getContext(),DeviceUtil.getFreeExternalMemory())));
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
        mTvShowMem = (TextView) mRootView.findViewById(R.id.tv_system_mem_msg);
        mTvShowMem.setText(ProcessUtil.getSystemMemMsg());
    }


    private void init() {
        mTelManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        mActivity = getActivity();
    }






}
