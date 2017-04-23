package com.skye.lover.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Message;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;

import com.skye.lover.model.Photo;
import com.skye.lover.model.PhotoFolder;
import com.skye.lover.util.ThumbnailsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加载本地图片异步任务
 */
public class LoadPhotosTask extends AsyncTask<Void, Void, List<PhotoFolder>> {
    private final List<PhotoFolder> EMPTY_LIST = new ArrayList<>();//空列表
    private Context context;
    private Message message;

    /**
     * @param context 上下文
     * @param message 消息
     */
    public LoadPhotosTask(Context context, Message message) {
        this.context = context;
        this.message = message;
    }

    @Override
    protected void onPostExecute(List<PhotoFolder> list) {
        super.onPostExecute(list);
        try {
            message.obj = list;
            message.sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected List<PhotoFolder> doInBackground(Void... params) {
        final String prefix = "file://";//文件路径前缀
        //获取所有图片的缩略图
        ContentResolver cr = context.getContentResolver();
        ThumbnailsUtil.clear();
        String[] projection = {Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA};
        Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
        if (cursor != null) {
            int columnIndexImageId = cursor.getColumnIndex(Thumbnails.IMAGE_ID);
            int columnIndexData = cursor.getColumnIndex(Thumbnails.DATA);
            while (cursor.moveToNext()) {
                ThumbnailsUtil.put(cursor.getInt(columnIndexImageId), prefix + cursor.getString(columnIndexData));
            }
            cursor.close();
        }

        //获取原图信息
        cursor = cr.query(Media.EXTERNAL_CONTENT_URI, null, null, null, "date_modified DESC");
        Map<String, PhotoFolder> map = new HashMap<>();
        if (cursor != null) {
            int columnIndexId = cursor.getColumnIndex(ImageColumns._ID);
            int columnIndexPath = cursor.getColumnIndex(ImageColumns.DATA);
            int columnIndexDisplayName = cursor.getColumnIndex(ImageColumns.BUCKET_DISPLAY_NAME);
            Photo photo;
            PhotoFolder pf;
            int imageId;
            String path;
            String displayName;
            while (cursor.moveToNext()) {
                imageId = cursor.getInt(columnIndexId);
                path = cursor.getString(columnIndexPath);
                displayName = cursor.getString(columnIndexDisplayName);
                if (!map.containsKey(displayName)) {//不包括该文件夹，需要新建
                    //文件夹信息
                    pf = new PhotoFolder();
                    pf.setDisplayName(displayName);
                    //图片信息
                    photo = new Photo();
                    photo.setImageId(imageId);
                    photo.setDisplayPath(prefix + path);
                    photo.setAbsolutePath(path);
                    pf.add(photo);
                    map.put(displayName, pf);
                } else {//已经包含该文件夹
                    //图片信息
                    photo = new Photo();
                    photo.setImageId(imageId);
                    photo.setDisplayPath(prefix + path);
                    photo.setAbsolutePath(path);
                    map.get(displayName).add(photo);
                }
            }
            cursor.close();
        }
        if (map.isEmpty()) return EMPTY_LIST;
        return new ArrayList<>(map.values());
    }
}
