package com.naziksost.torrentplayer.utils;

import android.os.Environment;
import android.util.Log;

import com.naziksost.torrentplayer.Const;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FilesUtils {

    public static boolean isExists(String path) {
        return new File(path).exists();
    }


    public static String nameFromPath(String path, boolean withExtension) {
        if (path == null || path.equals("")) return "";

        String[] parts = path.split("/");
        String name = parts[parts.length - 1];

        if (withExtension)
            return name;
        else {
            int index = name.lastIndexOf(".");
            return name.substring(0, index - 1);
        }
    }


    public static void sortAZ(List<File> list) {
        Comparator<File> comparator = new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
        Collections.sort(list, comparator);
    }


    public static void sortZA(List<File> list) {
        Comparator<File> comparator = new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o2.getName().compareTo(o1.getName());
            }
        };
        Collections.sort(list, comparator);
    }


    public static boolean isMountSD() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(Const.TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return false;
        }
        return true;
    }
}
