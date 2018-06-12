package com.wp.cheez.config;

import android.os.Environment;
import android.util.Log;

import com.wp.cheez.utils.LogHelper;

import java.io.File;

/**
 * Created by cadi on 2016/8/13.
 */
public class FileConstant {
    public static final String TAG = LogHelper.makeTag(FileConstant.class);
    public static final String ROOT_PATH;
    public static final String FILE_EXPLORER_SERVER_DIR_NAME;
    public static final String SERVER_TEST_FILE_NAME;
    public static final String SERVER_PRE_FILE_NAME;
    public static final String SERVER_LOG_FILE_NAME;
    private static final String SEP;
    public static final String FILE_EXPLORER_SERVER_DIR_PATH;
    public static final String FILE_EXPLORER_SERVER_TEST_PATH;
    public static final String FILE_EXPLORER_SERVER_PRE_PATH;
    public static final String FILE_EXPLORER_LOG_PATH;
    public static final String SHORT_VIDEO_SERVER_DIR_NAME;
    public static final String SHORT_VIDEO_SERVER_DIR_PATH;
    public static final String SHORT_VIDEO_SERVER_TEST_PATH;
    public static final String SHORT_VIDEO_SERVER_PRE_PATH;
    public static final String SHORT_VIDEO_LOG_PATH;
    static {
        ROOT_PATH = Environment.getExternalStorageDirectory().getPath();
        Log.d(TAG, "ROOT_PATH = " + ROOT_PATH);
        SEP = File.separator;
        FILE_EXPLORER_SERVER_DIR_NAME = ".fileexplorer";
        SHORT_VIDEO_SERVER_DIR_NAME = ".com.xunlei.shortvideo";
        SERVER_TEST_FILE_NAME = ".ra2servertest";
        SERVER_PRE_FILE_NAME = ".ra2serverpre";
        SERVER_LOG_FILE_NAME = ".log";

        FILE_EXPLORER_SERVER_DIR_PATH = ROOT_PATH + SEP + FILE_EXPLORER_SERVER_DIR_NAME;
        FILE_EXPLORER_SERVER_TEST_PATH = FILE_EXPLORER_SERVER_DIR_PATH + SEP + SERVER_TEST_FILE_NAME;
        FILE_EXPLORER_SERVER_PRE_PATH = FILE_EXPLORER_SERVER_DIR_PATH + SEP + SERVER_PRE_FILE_NAME;
        FILE_EXPLORER_LOG_PATH = FILE_EXPLORER_SERVER_DIR_PATH + SEP + SERVER_LOG_FILE_NAME;

        SHORT_VIDEO_SERVER_DIR_PATH = ROOT_PATH + SEP + SHORT_VIDEO_SERVER_DIR_NAME;
        SHORT_VIDEO_SERVER_TEST_PATH = SHORT_VIDEO_SERVER_DIR_PATH + SEP + SERVER_TEST_FILE_NAME;
        SHORT_VIDEO_SERVER_PRE_PATH = SHORT_VIDEO_SERVER_DIR_PATH + SEP + SERVER_PRE_FILE_NAME;
        SHORT_VIDEO_LOG_PATH = FILE_EXPLORER_SERVER_DIR_PATH + SEP + SERVER_LOG_FILE_NAME;

    }
}
