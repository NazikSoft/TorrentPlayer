package com.naziksost.torrentplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.naziksost.torrentplayer.entity.RecyclerAdapter;
import com.naziksost.torrentplayer.entity.Video;
import com.naziksost.torrentplayer.enums.LayoutManagers;
import com.naziksost.torrentplayer.utils.FilesUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import jBittorrentAPI.DownloadManager;
import jBittorrentAPI.TorrentFile;
import jBittorrentAPI.TorrentProcessor;
import jBittorrentAPI.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<File> listData = new ArrayList<>();
    private RecyclerAdapter ra;
    private Video movie = null;
    private String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        listData = FilesUtils.getDownloadFiles();
        ra = new RecyclerAdapter(listData);
        setLayoutManager(LayoutManagers.LINEAR);
        recyclerView.setAdapter(ra);

    }

    private void setLayoutManager(LayoutManagers enumLM) {
        if (enumLM == LayoutManagers.LINEAR){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        if (enumLM == LayoutManagers.GRID){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            recyclerView.setLayoutManager(gridLayoutManager);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Const.REQUEST_GET_FILE_PATH:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    String filePath = data.getStringExtra(Const.EXTRA_FILE_PATH);
                    movie = new Video(filePath);
                    Log.d(Const.TAG, "File Path: " + movie);

                    this.filePath = filePath;

                    if (movie.exists())
                        Toast.makeText(this, "Movie loaded", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(this, "file not exist", Toast.LENGTH_LONG).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.bSelectFile:
//                startActivityForResult(new Intent(MainActivity.this, FileChooser.class), Const.REQUEST_GET_FILE_PATH);
//                break;
//            case R.id.bPlay:
//                Intent intent = new Intent(MainActivity.this, VideoPlayer.class);
//                intent.putExtra(Const.EXTRA_PLAY_PATH, filePath);
//                startActivity(intent);
//        }
    }
}

