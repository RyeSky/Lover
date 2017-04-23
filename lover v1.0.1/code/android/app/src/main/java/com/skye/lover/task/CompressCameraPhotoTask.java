package com.skye.lover.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Message;

import com.skye.lover.LoverApplication;
import com.skye.lover.util.CommonUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 压缩相机拍照图片异步任务
 */
public class CompressCameraPhotoTask extends AsyncTask<Void, Void, String> {
    private String filePath;// 源文件路径
    private Message message;

    /**
     * @param filePath 源文件路径
     * @param message  消息
     */
    public CompressCameraPhotoTask(String filePath, Message message) {
        this.filePath = filePath;
        this.message = message;
    }

    @Override
    protected void onPostExecute(String filePath) {
        super.onPostExecute(filePath);
        try {
            message.obj = filePath;
            message.sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        File f = new File(filePath);
        if (!f.exists()) return null;
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
            FileOutputStream fos = new FileOutputStream(filePath);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);// 降低照片质量，再写入sdcard中
            // 否则图片过大，内存使用不合理，而且ListView会很卡，
            // 所以不推荐将超过50K的图片放到ListView上展示
            // 20%效果，图片100~200K
            bos.close();
            fos.close();
            bitmap.recycle();// 回收图片所占内存
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }
}
