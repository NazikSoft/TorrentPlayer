package com.naziksoft.videoplayer.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.naziksoft.videoplayer.DB.HelperFactory;
import com.naziksoft.videoplayer.DB.VideoDAO;
import com.naziksoft.videoplayer.R;
import com.naziksoft.videoplayer.controller.Controller;
import com.naziksoft.videoplayer.entity.OnRecyclerClickListener;
import com.naziksoft.videoplayer.entity.RecyclerAdapter;
import com.naziksoft.videoplayer.entity.Video;
import com.naziksoft.videoplayer.utils.FilesUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends AppCompatActivity {

    @BindView(R.id.recycleHistory)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar_hystory_activity)
    Toolbar toolbar;

    private RecyclerAdapter recyclerAdapter;
    private List<Video> listData  =  new ArrayList<>();
    private VideoDAO videoDAO;
    private Controller c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // init UI
        ButterKnife.bind(this);
        // prepare tool bar
        setSupportActionBar(toolbar);
        // connect to DB
        connectToDB();

        loadDataFromDB();
        c = new Controller(this);
        initRecyclerView();
    }

    private void loadDataFromDB() {
        try {
            List<Video> temp = videoDAO.queryForAll();
            Collections.reverse(temp);
            listData = temp;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerAdapter = new RecyclerAdapter(this, R.layout.layout_item_line, FilesUtils.videosToFiles(listData));
        recyclerAdapter.setOnRecyclerClickListener(new OnRecyclerClickListener() {
            @Override
            public void onClick(int position) {
                onRecyclerClickLogic(position);
            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void onRecyclerClickLogic(int position) {
        Video video = listData.get(position);
        // check is file real and video
        if (!video.exists() || !c.isVideoFile(video.getPath())) {
            Snackbar.make(recyclerView, R.string.controller_file_not_exists, Snackbar.LENGTH_SHORT).show();
            return;
        }
        c.runPlayer(video);
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
