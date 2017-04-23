package com.skye.lover.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 相片实体类
 */
public class Photo implements Parcelable, Comparable<Photo> {
    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
    /**
     * 相片id
     */
    private int imageId;
    /**
     * 用于显示的图片路径，图片的绝对路径
     */
    private String displayPath, absolutePath;
    /**
     * 是否被选中
     */
    private boolean checked;
    /**
     * 被选中的时间
     */
    private long checkedTime;

    public Photo() {
    }

    protected Photo(Parcel in) {
        this.imageId = in.readInt();
        this.displayPath = in.readString();
        this.absolutePath = in.readString();
        this.checked = in.readByte() != 0;
        this.checkedTime = in.readLong();
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getDisplayPath() {
        return displayPath;
    }

    public void setDisplayPath(String displayPath) {
        this.displayPath = displayPath;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public long getCheckedTime() {
        return checkedTime;
    }

    public void setCheckedTime(long checkedTime) {
        this.checkedTime = checkedTime;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        if (checked) checkedTime = System.currentTimeMillis();
    }

    @Override
    public int compareTo(Photo another) {
        long difference = checkedTime - another.getCheckedTime();
        if (difference > 0l)
            return 1;
        else if (difference < 0l)
            return -1;
        else
            return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.imageId);
        dest.writeString(this.displayPath);
        dest.writeString(this.absolutePath);
        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
        dest.writeLong(this.checkedTime);
    }

    @Override
    public String toString() {
        return "Photo{" +
                "imageId=" + imageId +
                ", displayPath='" + displayPath + '\'' +
                ", absolutePath='" + absolutePath + '\'' +
                ", checked=" + checked +
                ", checkedTime=" + checkedTime +
                '}';
    }
}
