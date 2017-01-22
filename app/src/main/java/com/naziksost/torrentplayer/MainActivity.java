package com.naziksost.torrentplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import jBittorrentAPI.ConnectionListener;
import jBittorrentAPI.DownloadManager;
import jBittorrentAPI.PeerUpdater;
import jBittorrentAPI.TorrentFile;
import jBittorrentAPI.TorrentProcessor;
import jBittorrentAPI.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.bSelectFile)
    Button bSelectFile;
    @BindView(R.id.bPlay)
    Button bPlay;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.textView2)
    TextView textView2;

    private File movie = null;
    private String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        bPlay.setOnClickListener(this);
        bSelectFile.setOnClickListener(this);

        TorrentProcessor tp = new TorrentProcessor();
        Map map = tp.parseTorrent(getTorrent());
                TorrentFile s = tp.getTorrentFile(map);
        test(s);
//        DownloadManager dm = new DownloadManager(s, Utils.generateID());
////        dm.startListening(6881, 6889);
//
//        Map m = getMap(s, Utils.generateID(),dm);
        String st = s.announceURL;
//        textView2.setText(st);
    }

    void test (TorrentFile t){
        // Взять торрент-файл
        TorrentProcessor tp = new TorrentProcessor();

        TorrentFile tf = tp.getTorrentFile(tp.parseTorrent(getTorrent()));

        DownloadManager dm = new DownloadManager(tf, Utils.generateID());

        // Запуск закачки
        dm.startListening(6882, 6889);
        dm.startTrackerUpdate();

        while(true)
        {
            // Если загрузка завершена, то ожидание прерывается
            if(dm.isComplete())
            {
                break;
            }

            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException ex)
            {
                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // завершение закачки
        dm.stopTrackerUpdate();
        dm.closeTempFiles();

        // проверка, куда были сохранены скачанные данные (то поле, которое задается в TorrentProcessor.setName())
        String torrentSavedTo = tp.getTorrentFile(tp.parseTorrent(getTorrent())).saveAs;
    }

//    public Map getMap(TorrentFile tor, byte[] ids,DownloadManager dm ) {
//        PeerUpdater peerUpdater=new PeerUpdater(ids, tor);
//        ConnectionListener connectionListener = new ConnectionListener();
//
//        peerUpdater.addPeerUpdateListener(dm);
//        peerUpdater.setListeningPort(connectionListener.getConnectedPort());
//        peerUpdater.setLeft(tor.total_length);
//
//        return peerUpdater.contactTracker(ids, tor, peerUpdater.getDownloaded(), peerUpdater.getUploaded(), peerUpdater.getLeft(), peerUpdater.getEvent());
//    }



    private File getTorrent() {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.test);
        File dirDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File torrent = new File(dirDownload, "tor.torrent");

        return torrent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Const.REQUEST_GET_FILE_PATH:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    String filePath = data.getStringExtra(Const.EXTRA_FILE_PATH);
                    movie = new File(filePath);
                    Log.d(Const.TAG, "File Path: " + movie);

                    this.filePath = filePath;
                    textView.setText(filePath);

                    if (movie.exists())
                        Toast.makeText(this, filePath, Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(this, "file not exist", Toast.LENGTH_LONG).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSelectFile:
                startActivityForResult(new Intent(MainActivity.this, FileChooser.class), Const.REQUEST_GET_FILE_PATH);
                break;
            case R.id.bPlay:
                Intent intent = new Intent(MainActivity.this, VideoPlayer.class);
                intent.putExtra(Const.EXTRA_PLAY_PATH, filePath);
                startActivity(intent);
        }
    }
}

