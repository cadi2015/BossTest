package com.wp.bosstest.config;

/**
 * Created by cadi on 2016/8/10.
 */
public class SharedConstant {
    public static final String SHARED_BOSS_CONFIG_NAME;
    public static final String GUIDE_KEY_IS_FIRST;
    public static final String GUIDE_KEY_VERSION_CODE;
    public static final String DIALOG_KEY_CHECK_BOX_SELECTED_ROOT;
    public static final String DIALOG_KEY_CHECK_BOX_SELECTED_FLOAT_WINDOW;
    static {
        SHARED_BOSS_CONFIG_NAME = "boss_config";
        GUIDE_KEY_IS_FIRST = "is_first";
        GUIDE_KEY_VERSION_CODE = "version_code";
        DIALOG_KEY_CHECK_BOX_SELECTED_ROOT = "check_box_is_selected_root";
        DIALOG_KEY_CHECK_BOX_SELECTED_FLOAT_WINDOW = "check_box_is_selected_window";
    }
}
