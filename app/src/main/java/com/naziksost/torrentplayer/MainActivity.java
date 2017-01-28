package com.naziksost.torrentplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.naziksost.torrentplayer.controller.Controller;
import com.naziksost.torrentplayer.entity.OnRecyclerClickListener;
import com.naziksost.torrentplayer.entity.RecyclerAdapter;
import com.naziksost.torrentplayer.entity.Video;
import com.naziksost.torrentplayer.enums.LayoutManagers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private RecyclerAdapter recyclerAdapter;
    private List<File> listData = new ArrayList<>();
    private Controller c;
    private Video video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        c = new Controller(this, recyclerView);
        listData = c.getDownloadFiles();

        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerAdapter = new RecyclerAdapter(listData);
        setLayoutManager(LayoutManagers.LINEAR);

        recyclerAdapter.setOnRecyclerClickListener(new OnRecyclerClickListener() {
            @Override
            public void onClick(int position) {
                String path = listData.get(position).getPath();
                if (c.isVideoFile(path)) {
                    video = new Video(path);
                    c.runPlayer(video);
                }
            }
        });
        recyclerView.setAdapter(recyclerAdapter);

    }

    private void setLayoutManager(LayoutManagers enumLM) {
        if (enumLM == LayoutManagers.LINEAR) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        if (enumLM == LayoutManagers.GRID) {
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
                    Log.d(Const.TAG, "File Path: " + filePath);

                    if (!c.isVideoFile(filePath)) return;

                    video = new Video(filePath);
                    c.runPlayer(video);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

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

