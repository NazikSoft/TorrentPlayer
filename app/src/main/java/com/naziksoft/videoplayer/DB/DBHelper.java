package com.naziksoft.videoplayer.DB;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.naziksoft.videoplayer.consts.Const;
import com.naziksoft.videoplayer.entity.Video;

import java.sql.SQLException;


public class DBHelper extends OrmLiteSqliteOpenHelper {

    private VideoDAO videoDAO = null;

    public DBHelper(Context context) {
        super(context, Const.DATABASE_NAME, null, Const.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Video.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, Video.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // singleton for VideoDAO
    public VideoDAO getVideoDAO() throws SQLException {
        if (videoDAO == null)
            videoDAO = new VideoDAO(getConnectionSource());
        return videoDAO;
    }

    public void resetDB(){
        try {
            TableUtils.dropTable(connectionSource, Video.class, true);
            TableUtils.createTable(connectionSource, Video.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() {
        super.close();
        videoDAO = null;
    }
}
