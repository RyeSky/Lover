package com.skye.lover.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 相册文件夹实体类
 */
public class PhotoFolder implements Parcelable {
    public static final Parcelable.Creator<PhotoFolder> CREATOR = new Parcelable.Creator<PhotoFolder>() {
        @Override
        public PhotoFolder createFromParcel(Parcel source) {
            return new PhotoFolder(source);
        }

        @Override
        public PhotoFolder[] newArray(int size) {
            return new PhotoFolder[size];
        }
    };
    /**
     * 用于显示的相册名称
     */
    private String displayName;
    /**
     * 文件下的相片集合
     */
    private List<Photo> photos;

    public PhotoFolder() {
        photos = new ArrayList<>();
    }

    protected PhotoFolder(Parcel in) {
        this.displayName = in.readString();
        this.photos = new ArrayList<>();
        in.readList(this.photos, Photo.class.getClassLoader());
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.displayName);
        dest.writeList(this.photos);
    }

    @Override
    public String toString() {
        return "PhotoFolder{" +
                "displayName='" + displayName + '\'' +
                ", photos=" + photos +
                '}';
    }
}
