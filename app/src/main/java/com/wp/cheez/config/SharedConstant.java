package com.wp.cheez.config;

/**
 * Created by cadi on 2016/8/10.
 */
public class SharedConstant {
    public static final String SHARED_BOSS_CONFIG_NAME;
    public static final String GUIDE_KEY_IS_FIRST;
    public static final String GUIDE_KEY_VERSION_CODE;
    public static final String DIALOG_KEY_CHECK_BOX_SELECTED_ROOT;
    public static final String DIALOG_KEY_CHECK_BOX_SELECTED_FLOAT_WINDOW;
    public static final String SELECT_FRIST_APP_PKG_KEY;
    public static final String SELECT_SECOND_APP_PKG_KEY;

    static {
        SHARED_BOSS_CONFIG_NAME = "boss_config";
        GUIDE_KEY_IS_FIRST = "is_first";
        GUIDE_KEY_VERSION_CODE = "version_code";
        DIALOG_KEY_CHECK_BOX_SELECTED_ROOT = "check_box_is_selected_root";
        DIALOG_KEY_CHECK_BOX_SELECTED_FLOAT_WINDOW = "check_box_is_selected_window";
        SELECT_FRIST_APP_PKG_KEY = "first_show_app_pkg";
        SELECT_SECOND_APP_PKG_KEY = "second_show_app_pkg";
    }
}
