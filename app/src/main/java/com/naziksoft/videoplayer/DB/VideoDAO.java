package com.naziksoft.videoplayer.DB;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.naziksoft.videoplayer.entity.Video;

import java.sql.SQLException;

public class VideoDAO extends BaseDaoImpl<Video,String>{

    public VideoDAO(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Video.class);
    }
}
