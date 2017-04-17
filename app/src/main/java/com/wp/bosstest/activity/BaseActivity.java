package com.wp.bosstest.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.wp.bosstest.R;

/**
 * Created by cadi on 2017/2/26.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
        initActionBar();
    }

    public abstract boolean isDisplayHomeAsUp();

    public abstract int getContentViewId();

    public abstract boolean isDisplayToolBar();

    private void initContentView() {
        int contentViewId = getContentViewId();
        boolean isShowToolBar = isDisplayToolBar();
        boolean haveContent = (contentViewId != 0);
        if (isShowToolBar) {
            setContentView(R.layout.activity_base);
            Toolbar toolbar = (Toolbar) findViewById(R.id.with_tool_bar);
            setSupportActionBar(toolbar);

            if (haveContent) {
                FrameLayout contentView = (FrameLayout) findViewById(R.id.base_fl_content_view);
                getLayoutInflater().inflate(contentViewId, contentView);
            }

            return;
        }
        if (haveContent) {
            setContentView(contentViewId);
        }

    }


    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(isDisplayHomeAsUp());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
