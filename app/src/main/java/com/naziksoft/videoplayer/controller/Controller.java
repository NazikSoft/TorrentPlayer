package com.naziksoft.videoplayer.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;

import com.naziksoft.videoplayer.Const;
import com.naziksoft.videoplayer.activity.VideoPlayer;
import com.naziksoft.videoplayer.entity.Video;

import java.io.File;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;


public class Controller {
    private Context context;
    private SharedPreferences sp;

    public Controller(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(Const.SHARED_PREF_USER, Context.MODE_PRIVATE);
    }

    public void runPlayer(Video video) {
        Intent intent = new Intent(context, VideoPlayer.class);
        intent.putExtra(Const.EXTRA_PLAY_PATH, video.getPath());
        context.startActivity(intent);

    }

    public boolean isVideoFile(String path) {
        // check is file video
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

    public List<File> getDownloadsDirFiles() {
        File download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        return Arrays.asList(download.listFiles());
    }

    public List<File> getVideoDirFiles() {
        File video = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        return Arrays.asList(video.listFiles());
    }

    public List<File> getUserDirFiles(String filePath) {
        File userFile = new File(filePath).getParentFile();
        return Arrays.asList(userFile.listFiles());
    }

    public String getUserFromSharedPref(){
        return sp.getString(Const.SHARED_PREF_USER,"");
    }

    public void setUserFromSharedPref(String userEmail){
         sp.edit().putString(Const.SHARED_PREF_USER,userEmail).apply();
    }


}
