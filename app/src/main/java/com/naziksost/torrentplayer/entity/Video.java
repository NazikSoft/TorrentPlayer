package com.naziksost.torrentplayer.entity;

import com.naziksost.torrentplayer.utils.FilesUtils;

import java.io.File;


public class Video {

    private String parentDirPath;
    private String name;
    private String path;

    public Video(File file) {
        parentDirPath = file.getParentFile().getAbsolutePath();
        name = file.getName();
        path = file.getAbsolutePath();
    }

    public Video(String path) {
        this.path = path;
        name = FilesUtils.nameFromPath(path, true);
        parentDirPath = new File(path).getParentFile().getAbsolutePath();

    }

    public Video(File parentDir, String name) {
        this.parentDirPath = parentDir.getAbsolutePath();
        this.name = name;
        path = new File(parentDir, name).getAbsolutePath();
    }

    public boolean exists(){return new File(path).exists();
    }

    @Override
    public String toString() {
        return  "Name= " + name + '\n' +
                "Path= " + path + '\n';
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
