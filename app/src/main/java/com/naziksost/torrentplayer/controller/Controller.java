package com.naziksost.torrentplayer.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.naziksost.torrentplayer.Const;
import com.naziksost.torrentplayer.activity.VideoPlayer;
import com.naziksost.torrentplayer.entity.Video;

import java.io.File;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;


public class Controller {
    private Context context;

    public Controller(Context context) {
        this.context = context;
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


}
