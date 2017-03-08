package com.wp.bosstest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by cadi on 2017/2/28.
 */

public class ListViewSupportScroll extends ListView {
    public ListViewSupportScroll(Context context) {
        super(context);
    }

    public ListViewSupportScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewSupportScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
