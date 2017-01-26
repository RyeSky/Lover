package com.skye.lover.util;

import java.io.File;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.skye.lover.ConstantUtil;
import com.skye.lover.R;

import android.content.Context;
import android.graphics.Bitmap;

public class ImageLoaderUtil {

    @SuppressWarnings("deprecation")
    public static void init(Context context) {
        ImageLoaderConfiguration config;
        if (CommonUtil.isExistSdcard()) {
            DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.shape_empty) // 设置图片在下载期间显示的图片
                    .showImageForEmptyUri(R.drawable.shape_empty)// 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.drawable.shape_empty) // 设置图片加载/解码过程中错误时候显示的图片
                    .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                    .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
                    .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                    // .delayBeforeLoading(int delayInMillis)//int
                    // delayInMillis为你设置的下载前的延迟时间
                    // 设置图片加入缓存前，对bitmap进行设置
                    // .preProcessor(BitmapProcessor preProcessor)
                    .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                    .displayer(new RoundedBitmapDisplayer(120))// 是否设置为圆角，弧度为多少
                    .displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
                    .build();// 构建完成
            File pictureDir = new File(ConstantUtil.PICTURE_PATH);
            if (!pictureDir.exists())
                pictureDir.mkdirs();
            if (ConstantUtil.DEBUG)
                config = new ImageLoaderConfiguration.Builder(context).memoryCacheExtraOptions(480, 800)
                        // max width, max height，即保存的每个缓存文件的最大长宽
                        .discCacheExtraOptions(480, 800, null)
                        // Can slow ImageLoader, use it carefully (Better don't
                        // use it)/设置缓存的详细信息，最好不要设置这个
                        .threadPoolSize(3)
                        // 线程池内加载的数量
                        .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                        .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                        // You can pass your own memory cache
                        // implementation/你可以通过自己的内存缓存实现
                        .memoryCacheSize(2 * 1024 * 1024).discCacheSize(50 * 1024 * 1024)
                        .discCacheFileNameGenerator(new Md5FileNameGenerator())
                        // 将保存的时候的URI名称用MD5 加密
                        .tasksProcessingOrder(QueueProcessingType.LIFO).discCacheFileCount(100)
                        // 缓存的文件数量
                        .discCache(new UnlimitedDiscCache(pictureDir))
                        // 自定义缓存路径
                        .defaultDisplayImageOptions(options)
                        .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)).writeDebugLogs()
                        .build();// 开始构建
            else
                config = new ImageLoaderConfiguration.Builder(context).memoryCacheExtraOptions(480, 800)
                        // max width, max height，即保存的每个缓存文件的最大长宽
                        .discCacheExtraOptions(480, 800, null)
                        // Can slow ImageLoader, use it carefully (Better don't
                        // use
                        // it)/设置缓存的详细信息，最好不要设置这个
                        .threadPoolSize(3)
                        // 线程池内加载的数量
                        .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                        .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                        // You can pass your own memory cache
                        // implementation/你可以通过自己的内存缓存实现
                        .memoryCacheSize(2 * 1024 * 1024).discCacheSize(50 * 1024 * 1024)
                        .discCacheFileNameGenerator(new Md5FileNameGenerator())
                        // 将保存的时候的URI名称用MD5 加密
                        .tasksProcessingOrder(QueueProcessingType.LIFO).discCacheFileCount(100)
                        // 缓存的文件数量
                        .discCache(new UnlimitedDiscCache(pictureDir))
                        // 自定义缓存路径
                        .defaultDisplayImageOptions(options)
                        .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)).build();// 开始构建
        } else {
            DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.shape_empty) // 设置图片在下载期间显示的图片
                    .showImageForEmptyUri(R.drawable.shape_empty)// 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.drawable.shape_empty) // 设置图片加载/解码过程中错误时候显示的图片
                    .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                    .cacheOnDisc(false)// 设置下载的图片是否缓存在SD卡中
                    .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                    // .delayBeforeLoading(int delayInMillis)//int
                    // delayInMillis为你设置的下载前的延迟时间
                    // 设置图片加入缓存前，对bitmap进行设置
                    // .preProcessor(BitmapProcessor preProcessor)
                    .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                    .displayer(new RoundedBitmapDisplayer(120))// 是否设置为圆角，弧度为多少
                    .displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
                    .build();// 构建完成
            if (ConstantUtil.DEBUG)
                config = new ImageLoaderConfiguration.Builder(context).memoryCacheExtraOptions(480, 800)
                        // Can slow ImageLoader, use it carefully (Better don't
                        // use
                        // it)/设置缓存的详细信息，最好不要设置这个
                        .threadPoolSize(3)
                        // 线程池内加载的数量
                        .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                        .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                        // You can pass your own memory cache
                        // implementation/你可以通过自己的内存缓存实现
                        .memoryCacheSize(2 * 1024 * 1024)
                        // 将保存的时候的URI名称用MD5 加密
                        .tasksProcessingOrder(QueueProcessingType.LIFO)
                        // 自定义缓存路径
                        .defaultDisplayImageOptions(options)
                        .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)).writeDebugLogs()
                        .build();// 开始构建
            else
                config = new ImageLoaderConfiguration.Builder(context).memoryCacheExtraOptions(480, 800)
                        // Can slow ImageLoader, use it carefully (Better don't
                        // use
                        // it)/设置缓存的详细信息，最好不要设置这个
                        .threadPoolSize(3)
                        // 线程池内加载的数量
                        .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                        .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                        // You can pass your own memory cache
                        // implementation/你可以通过自己的内存缓存实现
                        .memoryCacheSize(2 * 1024 * 1024)
                        // 将保存的时候的URI名称用MD5 加密
                        .tasksProcessingOrder(QueueProcessingType.LIFO)
                        .defaultDisplayImageOptions(options)
                        .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)).build();// 开始构建

        }
        ImageLoader.getInstance().init(config);
    }

}
