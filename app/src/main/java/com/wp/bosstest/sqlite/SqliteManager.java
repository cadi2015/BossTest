package com.wp.bosstest.sqlite;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.wp.bosstest.utils.LogHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by cadi on 2016/8/16.
 */
public class SqliteManager {
    public static final int TABLE_COUNT = 7;
    private static final String TAG = LogHelper.makeTag(SqliteManager.class);
    public static final String DATA_ROOT_PATH = Environment.getDataDirectory().getAbsolutePath();
    public static final String DB_NAME = "downloadUrl.db";
    public static final String PACKAGE_NAME = "com.wp.bosstest";
    public static final String DB_ROOT_PATH = File.separator + "data" + File.separator + PACKAGE_NAME + File.separator + "databases";
    public static final String DB_ASSETS_DIR = "db";

    public static class DatabaseConstant implements BaseColumns {
        public static final String TABLE_NAME_AD;
        public static final String TABLE_NAME_ED2K;
        public static final String TABLE_NAME_FTP;
        public static final String TABLE_NAME_HTTP;
        public static final String TABLE_NAME_HTTPS;
        public static final String TABLE_NAME_MAGNET;
        public static final String TABLE_NAME_MARKET;

        static {
            TABLE_NAME_AD = "AD";
            TABLE_NAME_ED2K = "ED2K";
            TABLE_NAME_FTP = "FTP";
            TABLE_NAME_HTTP = "HTTP";
            TABLE_NAME_HTTPS = "HTTPS";
            TABLE_NAME_MAGNET = "MAGNET";
            TABLE_NAME_MARKET = "MARKET";
        }
    }

    public void fromAssetsCopyDB(Context context) {
        try {
            Log.d(TAG, "DB_PATH = " + DATA_ROOT_PATH + DB_ROOT_PATH);
            String dbPath = DATA_ROOT_PATH + DB_ROOT_PATH;
            File dbDir = new File(dbPath);
            if (!dbDir.exists()) {
                boolean isSuccess = dbDir.mkdir(); //纯属狗屎运，app在初始化Application的时候创建了/data/data/<packageName>，不然我的databases绝对会黄掉
                Log.d(TAG, "File isSuccess = " + isSuccess);
            }
            InputStream inputFile = context.getAssets().open(DB_ASSETS_DIR + File.separator + DB_NAME);
            byte[] buffers = new byte[1024]; //把输入字节流读取内存缓冲区
            OutputStream outFile = new FileOutputStream(new File(dbPath, DB_NAME));
            int length;
            while ((length = inputFile.read(buffers)) > 0) {
                outFile.write(buffers, 0, length);
            }
            outFile.flush();
            outFile.close();
            inputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "^^^^^^^^^^^^^IOException , file is not exist^^^^^^^^^^^^^^^");
        }
    }

}
