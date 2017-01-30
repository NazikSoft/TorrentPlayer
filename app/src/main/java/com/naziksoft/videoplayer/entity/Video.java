package com.naziksoft.videoplayer.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.naziksoft.videoplayer.DB.VideoDAO;
import com.naziksoft.videoplayer.consts.Const;
import com.naziksoft.videoplayer.utils.FilesUtils;

import java.io.File;

@DatabaseTable(tableName = Const.TABLE_NAME, daoClass = VideoDAO.class)
public class Video {

    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String parentDirPath;
    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String name;
    @DatabaseField(canBeNull = false, dataType = DataType.STRING, id = true)
    private String path;
    @DatabaseField(dataType = DataType.INTEGER)
    private int duration;
    @DatabaseField(dataType = DataType.INTEGER)
    private int currentPosition = 0;

    public Video(String path) {
        this.path = path;
        name = FilesUtils.nameFromPath(path, true);
        parentDirPath = new File(path).getParentFile().getAbsolutePath();

    }

    // constructor for future
    public Video(File file) {
        parentDirPath = file.getParentFile().getAbsolutePath();
        name = file.getName();
        path = file.getAbsolutePath();
    }

    // constructor for future
    public Video(File parentDir, String name) {
        this.parentDirPath = parentDir.getAbsolutePath();
        this.name = name;
        path = new File(parentDir, name).getAbsolutePath();
    }

    // constructor fom ormLite
    public Video() {
    }

    public boolean exists() {
        return new File(path).exists();
    }

    @Override
    public String toString() {
        String result = "Name= " + name + '\n' +
                "Path= " + path + '\n';
        if (duration > 0)
            result += "Duration= " + duration / 60000 + "min" + '\n';

        return result;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }


    public String getParentDirPath() {
        return parentDirPath;
    }

    public void setParentDirPath(String parentDirPath) {
        this.parentDirPath = parentDirPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Video video = (Video) o;

        return path.equals(video.path);

    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }
}
