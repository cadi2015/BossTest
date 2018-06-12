package com.wp.cheez.utils;

/**
 * Created by cadi on 2016/8/11.
 */
public class LogHelper {
    public static String makeTag(Class className) {
        return makeTag(className.getSimpleName());
    }

    public static String makeTag(String className) {
        return className;
    }
}
