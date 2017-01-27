package com.naziksost.torrentplayer.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.naziksost.torrentplayer.Const;
import com.naziksost.torrentplayer.R;

import java.io.File;

public class FileChooser extends AppCompatActivity {
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chooser);

        showFileChooser();
    }


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
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Const.REQUEST_FILE_SELECT:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(Const.TAG, "File Uri: " + uri.toString());
                    // Get the path
                    filePath = getPath(uri);

                    if (isValidFile()) {
                        Intent intent = new Intent();
                        intent.putExtra(Const.EXTRA_FILE_PATH, filePath);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else {
                        Toast.makeText(this,"Wrong file's type. Please select another file",Toast.LENGTH_LONG).show();
                        showFileChooser();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getPath(Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = this.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {

            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
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


}
