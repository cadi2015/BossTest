package com.wp.cheez.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.wp.cheez.R;

public class LocationActivity extends BaseActivity implements View.OnClickListener {
    private Button mBtnStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();
    }

    private void setupViews(){
        mBtnStart = (Button) findViewById(R.id.btn_start_simulating_location);
        mBtnStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start_simulating_location:

                break;
        }
    }

    @Override
    public boolean isDisplayHomeAsUp() {
        return true;
    }



    @Override
    public int getContentViewId() {
        return R.layout.activity_location;
    }

    @Override
    public boolean isDisplayToolBar() {
        return true;
    }

    @Override
    public String getToolBarTitle() {
        return "模拟经纬度";
    }
}
