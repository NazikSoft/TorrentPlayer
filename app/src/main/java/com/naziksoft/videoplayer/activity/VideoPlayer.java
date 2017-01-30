package com.naziksoft.videoplayer.activity;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.naziksoft.videoplayer.DB.HelperFactory;
import com.naziksoft.videoplayer.DB.VideoDAO;
import com.naziksoft.videoplayer.R;
import com.naziksoft.videoplayer.consts.Const;
import com.naziksoft.videoplayer.entity.Video;

import java.sql.SQLException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayer extends AppCompatActivity {

    // Butterknife lib
    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.bFullscreen)
    com.rey.material.widget.Button bFullscreen;
    @BindView(R.id.videoViewLayout)
    RelativeLayout relativeLayout;

    private boolean isFullScreen = true;
    private Video video;
    private RelativeLayout.LayoutParams layoutParams;
    private VideoDAO videoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide panels for fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_player);

        // init LayoutParams for controls fullscreen mode
        layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        // init UI
        ButterKnife.bind(this);
        enterFullScreen();

        // connect to DB
        connectToDB();

        // prepare Video file and VideoView
        initVideo();
        initVideoView();

        // play the video
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                int duration = videoView.getDuration();
                int currentPosition = video.getCurrentPosition();
                video.setDuration(duration);
                videoView.seekTo(currentPosition);
                videoView.start();
            }
        });

        // button on fullscreen
        bFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterFullScreen();
            }
        });
    }

    private void initVideo() {
        String path = getIntent().getStringExtra(Const.EXTRA_PLAY_PATH);
        try {
            if (videoDAO.idExists(path))
                video = videoDAO.queryForId(path);
            else {
                video = new Video(path);
                videoDAO.create(video);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initVideoView() {
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoPath(video.getPath());
        videoView.requestFocus();
    }

    private void saveCurrentPosition() {
        try {
            String path = video.getPath();
            int currentPosition = videoView.getCurrentPosition();
            video.setCurrentPosition(currentPosition);
            if (videoDAO.idExists(path)) {
                videoDAO.update(video);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void loadCurrentPosition() {
        try {
            String path = video.getPath();
            if (videoDAO.idExists(path)) {
                Video v = videoDAO.queryForId(path);
                video.setCurrentPosition(v.getCurrentPosition());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void connectToDB() {
        // connect DB
        HelperFactory.setDbHelper(this);
        try {
            videoDAO = HelperFactory.getDbHelper().getVideoDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void enterFullScreen() {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        bFullscreen.setVisibility(View.INVISIBLE);
        isFullScreen = true;
        relativeLayout.setBackgroundResource(android.R.color.black);
        videoView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        videoView.setLayoutParams(layoutParams);
    }

    private void exitFullScreen() {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        videoView.setSystemUiVisibility(0);
        isFullScreen = false;
        relativeLayout.setBackgroundResource(R.color.colorTextLight);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        }
        bFullscreen.setVisibility(View.VISIBLE);

        videoView.setLayoutParams(layoutParams);
    }

    @Override
    public void onBackPressed() {
        if (isFullScreen)
            exitFullScreen();
        else
            super.onBackPressed();
    }

    @Override
    protected void onResume() {
        loadCurrentPosition();
        super.onResume();
    }

    @Override
    protected void onPause() {
        saveCurrentPosition();
        super.onPause();
    }

    @Override
    protected void onStart() {
        // reconnect to DB if activity stopped
        connectToDB();
        super.onStart();
    }

    @Override
    protected void onStop() {
        // disconnect DB
        HelperFactory.releaseHelper();
        super.onStop();
    }
}
