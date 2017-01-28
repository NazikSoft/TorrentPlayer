package com.naziksost.torrentplayer.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.naziksost.torrentplayer.Const;
import com.naziksost.torrentplayer.R;
import com.naziksost.torrentplayer.activity.VideoPlayer;
import com.naziksost.torrentplayer.entity.Video;

import java.io.File;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;


public class Controller {
    private Context context;
    private View view;

    public Controller(Context context, View forSnackbar) {
        this.context = context;
        view = forSnackbar;
    }

    public void runPlayer(Video video) {
        Intent intent = new Intent(context, VideoPlayer.class);
        intent.putExtra(Const.EXTRA_PLAY_PATH, video.getPath());
        context.startActivity(intent);
    }

    public boolean isVideoFile(String path) {
        // check is file real
        if (!new File(path).exists()) {
            Snackbar.make(view, R.string.controller_file_not_exists, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        // check is file video
        String mimeType = URLConnection.guessContentTypeFromName(path);
        if (mimeType != null && mimeType.startsWith("video")) return true;
        else {
            Snackbar.make(view, R.string.controller_not_video_file, Snackbar.LENGTH_LONG).show();
            return false;
        }
    }

    public List<File> getDownloadFiles() {
        File download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        return Arrays.asList(download.listFiles());
    }

}
