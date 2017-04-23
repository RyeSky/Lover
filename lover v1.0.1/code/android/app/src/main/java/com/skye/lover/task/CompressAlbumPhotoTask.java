package com.skye.lover.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Message;
import android.text.TextUtils;

import com.skye.lover.LoverApplication;
import com.skye.lover.util.CommonUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.skye.lover.ConstantUtil.PICTURE_PATH;

/**
 * 压缩相册图片异步任务
 */
public class CompressAlbumPhotoTask extends AsyncTask<Void, Void, List<String>> {
    private List<String> filePaths;// 源文件路径
    private Message message;

    /**
     * @param filePaths 源文件路径
     * @param message   消息
     */
    public CompressAlbumPhotoTask(List<String> filePaths, Message message) {
        this.filePaths = filePaths;
        this.message = message;
    }


    @Override
    protected void onPostExecute(List<String> list) {
        super.onPostExecute(list);
        try {
            message.obj = list;
            message.sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        if (filePaths == null || filePaths.isEmpty()) {
            return null;
        }
        List<String> list = new ArrayList<>();
        String fileName, destFilePath;
        File file = new File(PICTURE_PATH);
        if (!file.exists())
            file.mkdirs();
        for (String filePath : filePaths) {
            if (TextUtils.isEmpty(filePath))
                continue;
            fileName = CommonUtil.getFileNameFromUri(filePath);
            if (TextUtils.isEmpty(fileName))
                continue;
            destFilePath = PICTURE_PATH + fileName;
            file = new File(filePath);
            if (!file.exists()) {
                continue;
            }
            Bitmap bitmap;
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                // 这个isjustdecodebounds很重要
                opt.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath, opt);
                // 获取到这个图片的原始宽度和高度
                int pictureWidth = opt.outWidth;
                int pictureHeight = opt.outHeight;
                // 获取屏的宽度和高度
                int screenWidth = CommonUtil.getWindowWidth(LoverApplication.getInstance());
                // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
                opt.inSampleSize = 1;
                // 根据屏的大小和图片大小计算出缩放比例
                if (pictureWidth > pictureHeight) {
                    if (pictureWidth > screenWidth)
                        opt.inSampleSize = pictureWidth / screenWidth;
                } else {
                    if (pictureHeight > screenWidth)
                        opt.inSampleSize = pictureHeight / screenWidth;
                }
                // 这次再真正地生成一个有像素的，经过缩放了的bitmap
                opt.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(filePath, opt);
                // 解决照片旋转角度
                bitmap = CommonUtil.rotaingImage(CommonUtil.readPictureDegree(filePath), bitmap);
                FileOutputStream fos = new FileOutputStream(destFilePath);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                /*
                 * 降低照片质量，再写入sdcard中
				 * 30 是压缩率，表示压缩70%; 如果不压缩是100，表示压缩率为0
				 * 否则图片过大，内存使用不合理，而且ListView会很卡，所以不推荐将超过50K的图片放到ListView上展示20
				 * %效果，图片100~200K
				 */
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);
                bos.close();
                fos.close();
                bitmap.recycle();// 回收图片所占内存
                list.add(destFilePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
