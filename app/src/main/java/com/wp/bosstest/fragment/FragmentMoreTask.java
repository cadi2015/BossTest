package com.wp.bosstest.fragment;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wp.bosstest.R;
import com.wp.bosstest.activity.WebActivity;
import com.wp.bosstest.config.IntentActionConstant;

/**
 * Created by cadi on 2016/6/20.
 */
public class FragmentMoreTask extends Fragment {
    private static final String TAG = "FragmentMoreTask";
    private View mRootView;
    private TextView mTvHttp;
    private TextView mTvEd2k;
    private TextView mTvThunder;
    private TextView mTvFtp;
    private TextView mTvMagnet;
    private RelativeLayout mRlMp4;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState");
        mRootView = inflater.inflate(R.layout.fragment_more_task, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews();
    }

    private void setUpViews() {
        MyBtnLis lis = new MyBtnLis();
        mTvHttp = (TextView) mRootView.findViewById(R.id.more_task_tv_http);
        mTvEd2k = (TextView) mRootView.findViewById(R.id.more_task_tv_ed2k);
        mTvThunder = (TextView) mRootView.findViewById(R.id.more_task_tv_thunder);
        mTvFtp = (TextView) mRootView.findViewById(R.id.more_task_tv_ftp);
        mTvMagnet = (TextView) mRootView.findViewById(R.id.more_task_tv_magnet);
        mRlMp4 = (RelativeLayout) mRootView.findViewById(R.id.more_task_rl_mp4);
        mTvHttp.setOnClickListener(lis);
        mTvEd2k.setOnClickListener(lis);
        mTvThunder.setOnClickListener(lis);
        mTvFtp.setOnClickListener(lis);
        mTvMagnet.setOnClickListener(lis);
        mRlMp4.setOnClickListener(lis);
    }

    private class MyBtnLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.more_task_tv_http:
                    Intent intentHttp = new Intent();
                    intentHttp.setAction(IntentActionConstant.TASK_DETAIL_ACTION_HTTP);
                    startActivity(intentHttp);
                    break;
                case R.id.more_task_tv_ed2k:
                    Intent intentEd2k = new Intent();
                    intentEd2k.setAction(IntentActionConstant.TASK_DETAIL_ACTION_ED2K);
                    startActivity(intentEd2k);
                    break;
                case R.id.more_task_tv_thunder:
                    Intent intentThunder = new Intent();
                    intentThunder.setAction(IntentActionConstant.TASK_DETAIL_ACTION_THUNDER);
                    startActivity(intentThunder);
                    break;
                case R.id.more_task_tv_ftp:
                    Intent intentFtp = new Intent();
                    intentFtp.setAction(IntentActionConstant.TASK_DETAIL_ACTION_FTP);
                    startActivity(intentFtp);
                    break;
                case R.id.more_task_tv_magnet:
                    Intent intentMagnet = new Intent();
                    intentMagnet.setAction(IntentActionConstant.TASK_DETAIL_ACTION_MAGNET);
                    startActivity(intentMagnet);
                    break;
                case R.id.more_task_rl_mp4:
                    Intent intent = new Intent();
                    ComponentName name = new ComponentName(getActivity(), WebActivity.class);
                    intent.setComponent(name);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }
}
