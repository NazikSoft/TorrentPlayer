package com.naziksost.torrentplayer;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.bController)
    ToggleButton bController;

    private Uri movie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


//        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() +"/"+R.raw.test));
        videoView.requestFocus();
        videoView.start();

//        lock / unlock interface
        setMediaController();

        bController.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setMediaController();
            }
        });
    }

    private void setMediaController() {
        if (bController.isChecked()) {
            videoView.setMediaController(new MediaController(this));
        } else {
            videoView.setMediaController(null);
        }
    }

//    private String getViewSrc() {
//        File extStorageDirectory = Environment.getExternalStorageDirectory();
//        String s = extStorageDirectory.getAbsolutePath() + "/test.mp4";
//        showFileChooser();
//        if (file != null)
//            return file.getAbsolutePath();
//        else return "";
//        return ;
//    }


    private void showFileChooser() {
        Log.d(Const.TAG, "showFileChooser");

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a Video"), Const.REQUEST_FILE_SELECT);
        } catch (android.content.ActivityNotFoundException ex) {
            // if not install any file manager
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Const.REQUEST_FILE_SELECT:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    movie = data.getData();
                    Log.d(Const.TAG, "File Uri: " + movie.toString());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isValidFile() {

        if (filePath == null || filePath.equals("")) return false;
        int startSuffix = filePath.lastIndexOf(".");
        int endSuffix = filePath.length();
        String suffix = filePath.substring(startSuffix, endSuffix);

        switch (suffix) {
            case ".avi":
                return true;
            case ".mp4":
                return true;
            case ".mkv":
                return true;
            case ".3gp":
                return true;
            case ".webm":
                return true;
            case ".ts":
                return true;
            default:
                return false;
        }
    }

//    private String getPath(Uri uri) {
//        if ("content".equalsIgnoreCase(uri.getScheme())) {
//            String[] projection = {"_data"};
//            Cursor cursor = null;
//
//            try {
//                cursor = this.getContentResolver().query(uri, projection, null, null, null);
//                int column_index = cursor.getColumnIndexOrThrow("_data");
//                if (cursor.moveToFirst()) {
//                    return cursor.getString(column_index);
//                }
//            } catch (Exception e) {
//
//            }
//        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//
//        return null;
//
//
//    }
}

