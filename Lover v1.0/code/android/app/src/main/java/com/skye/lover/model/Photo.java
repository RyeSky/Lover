package com.skye.lover.model;

import java.io.Serializable;

/**
 * 相片实体类
 */
public class Photo implements Serializable, Comparable<Photo> {
    private int imageId;//相片id
    private String displayPath, absolutePath;//用于显示的图片路径，图片的绝对路径
    private boolean checked;//是否被选中
    private long checkedTime;//被选中的时间

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
