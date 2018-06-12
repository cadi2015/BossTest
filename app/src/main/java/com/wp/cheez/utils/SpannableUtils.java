package com.wp.cheez.utils;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

/**
 * Created by cadi on 2016/9/6.
 */
public class SpannableUtils {
    public static SpannableString setTextColorDefault(Context context, String content, int indexStart, int indexEnd, int colorId) {
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(context,colorId)), indexStart, indexEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
