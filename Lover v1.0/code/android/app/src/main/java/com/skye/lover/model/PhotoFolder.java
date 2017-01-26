package com.skye.lover.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 相册文件夹实体类
 */
public class PhotoFolder implements Serializable{
    private String displayName;//用于显示的相册名称
    private List<Photo> photos;//文件下的相片集合

    public PhotoFolder() {
        photos = new ArrayList<>();
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    /**
     * 添加图片
     */
    public void add(Photo photo) {
        photos.add(photo);
    }

    /**
     * 文件夹下的相片张数
     */
    public int size() {
        return photos.size();
    }

    //利用显示名称比较两个文件夹是否是同一个

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotoFolder that = (PhotoFolder) o;
        return displayName.equals(that.displayName);
    }

    @Override
    public int hashCode() {
        return displayName.hashCode();
    }

    @Override
    public String toString() {
        return "PhotoFolder{" +
                "displayName='" + displayName + '\'' +
                ", photos=" + photos +
                '}';
    }
}
