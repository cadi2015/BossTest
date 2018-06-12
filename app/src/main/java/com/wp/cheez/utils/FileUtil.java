package com.wp.cheez.utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by cadi on 2017/4/10.
 */

public class FileUtil {
    public static boolean createDir(String path, String name) {
        File file = new File(path, name);
        if (file.exists()) {
            return false;
        } else {
            file.mkdir();
            return true;
        }
    }


    public static boolean createFile(String path, String name) {
        boolean createSuccess = false;
        File file = new File(path, name);
        if (file.exists()) {
            return createSuccess;
        } else {
            try {
                createSuccess = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return createSuccess;
    }


    public static boolean removeFile(String path, String name) {
        boolean removeSuccess;
        File file = new File(path, name);
        if (fileIsExists(path + File.separator + name)) {
            removeSuccess = file.delete();
        } else {
            removeSuccess = false;
        }
        return removeSuccess;
    }

    public static boolean fileIsExists(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
