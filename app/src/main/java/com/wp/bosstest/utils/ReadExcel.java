package com.wp.bosstest.utils;

import android.content.Context;
import android.util.Log;


import com.wp.bosstest.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Created by cadi on 2016/6/21.
 */
public class ReadExcel {
    private static final String TAG = "ReadExcel";
    private Workbook mWorkbook;
    private Sheet mSheet1;
    private InputStream mInputStream;
    private Context context;
    private List<String> mUrls;

    public ReadExcel(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        mUrls = new ArrayList<>();
    }

    public List<String> readFirstCell(int rawId) {
        try {
            mInputStream = context.getResources().openRawResource(rawId);
            mWorkbook = Workbook.getWorkbook(mInputStream);
            Log.d(TAG, "mInputStream = " + mInputStream);
            mSheet1 = mWorkbook.getSheet(0);
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                mInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < mSheet1.getRows(); i++) {
            Cell everyCell = mSheet1.getCell(0, i);
            String temp = everyCell.getContents();
            Log.d(TAG, "temp = " + temp);
            mUrls.add(temp);
        }
        return mUrls;
    }


}
