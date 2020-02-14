package com.wp.cheez.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.wp.cheez.R;

import androidx.core.app.ActivityCompat;

/**
 * Created by cadi on 2017/4/10.
 */

public class SnackbarUtil {

    public static Snackbar createSnackBar(Context context, View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        View viewGroup = snackbar.getView();
        TextView message = (TextView) viewGroup.findViewById(R.id.snackbar_text);
        viewGroup.setBackgroundColor(ActivityCompat.getColor(context, R.color.colorRed));
        message.setTextColor(ActivityCompat.getColor(context, R.color.color_guide_txt_white));
        snackbar.setActionTextColor(ActivityCompat.getColor(context, R.color.color_guide_txt_white));
        return snackbar;
    }
}
