package com.naziksost.torrentplayer.entity;

import com.naziksost.torrentplayer.utils.FilesUtils;

import java.io.File;


public class Video {

    private File parentDir;
    private File file;
    private String name;
    private String path;

    public Video(File file) {
        this.file = file;
        parentDir = file.getParentFile();
        name = file.getName();
        path = file.getAbsolutePath();
    }

    public Video(String path) {
        this.path = path;
        file = new File(path);
        name = FilesUtils.nameFromPath(path, true);
        parentDir = file.getParentFile();

    }

    public Video(File parentDir, String name) {
        this.parentDir = parentDir;
        this.name = name;
        file = new File(parentDir, name);
        path = file.getAbsolutePath();
    }

    public boolean exists(){
        return file.exists();
    }

    @Override
    public String toString() {
        return  "Name= " + name + '\n' +
                "Path= " + path + '\n';
    }

    public File getParentDir() {
        return parentDir;
    }

    public void setParentDir(File parentDir) {
        this.parentDir = parentDir;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
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
