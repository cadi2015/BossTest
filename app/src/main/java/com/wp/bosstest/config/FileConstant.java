package com.wp.bosstest.config;

import android.os.Environment;

import java.io.File;

/**
 * Created by cadi on 2016/8/13.
 */
public class FileConstant {
    public static final String ROOT_PATH;
    public static final String FILE_SERVER_DIR_NAME;
    public static final String FILE_SERVER_TEST_NAME;
    public static final String FILE_SERVER_PRE_NAME;
    public static final String FILE_LOG_NAME;
    private static final String SEP;
    public static final String FILE_SERVER_DIR_PATH;
    public static final String FILE_SERVER_TEST_PATH;
    public static final String FILE_SERVER_PRE_PATH;
    public static final String FILE_LOG_PATH;
    static {
        ROOT_PATH = Environment.getExternalStorageDirectory().getPath();
        SEP = File.separator;
        FILE_SERVER_DIR_NAME = ".fileexplorer";
        FILE_SERVER_TEST_NAME = ".ra2servertest";
        FILE_SERVER_PRE_NAME = ".ra2serverpre";
        FILE_LOG_NAME = ".log";
        FILE_SERVER_DIR_PATH = ROOT_PATH + SEP + FILE_SERVER_DIR_NAME;
        FILE_SERVER_TEST_PATH = FILE_SERVER_DIR_PATH + SEP + FILE_SERVER_TEST_NAME;
        FILE_SERVER_PRE_PATH = FILE_SERVER_DIR_PATH+ SEP + FILE_SERVER_PRE_NAME;
        FILE_LOG_PATH = FILE_SERVER_DIR_PATH + SEP + FILE_LOG_NAME;
    }
}
