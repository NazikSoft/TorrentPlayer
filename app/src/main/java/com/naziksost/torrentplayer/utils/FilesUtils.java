package com.naziksost.torrentplayer.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.naziksost.torrentplayer.Const;

import java.io.File;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Nazar on 25.01.2017.
 */

public class FilesUtils {



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


//    public  static File getExternalVideoDir(){
//        if (!isMountSD()) return null;
//
//        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
//        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//    }

    public static boolean isMountSD() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(Const.TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return false;
        }
        return true;
    }
}
