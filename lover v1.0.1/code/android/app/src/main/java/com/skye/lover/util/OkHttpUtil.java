package com.skye.lover.util;

import android.content.Context;
import android.text.TextUtils;

import com.skye.lover.ConstantUtil;
import com.skye.lover.LoverApplication;
import com.skye.lover.R;
import com.skye.lover.exception.NetworkRequestException;
import com.skye.lover.model.Cross;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;

/**
 * 网络请求工具类
 */
public class OkHttpUtil {
    private static final OkHttpClient client;
    private static final ACache cache;

    static {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.connectTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS);
        File sdcache = LoverApplication.getInstance().getExternalCacheDir();
        if (sdcache != null && sdcache.exists())
            builder.cache(new Cache(sdcache.getAbsoluteFile(), ConstantUtil.NETWORK_REQUEST_CACHE_SIZE));//设置请求缓存
        client = builder.build();
        cache = ACache.get(LoverApplication.getInstance());
    }

    /**
     * 构建post请求
     *
     * @param action                  请求动作地址
     * @param params                  请求的参数,如果没有可为空
     * @param isNeedCacheResponseData 是否需要缓存返回数据
     * @param isNeedShowErrorMessage  是否需要显示错误信息
     * @return 请求实体
     */
    public static Cross buildPostCall(String action, Map<String, String> params, boolean isNeedCacheResponseData, boolean isNeedShowErrorMessage) {
        String jsonParams = "";
        if (params != null) {
            jsonParams = CommonUtil.parseToJsonStr(params);
            CommonUtil.log(URLConfig.SERVER_HOST + action + "\n" + jsonParams);
        }
        boolean networkConnected = CommonUtil.isNetworkConnected(LoverApplication.getInstance());
        if (isNeedCacheResponseData && !networkConnected) {
            String body = cache.getAsString(action + "---" + jsonParams);
            return new Cross(action, params, isNeedShowErrorMessage, body).setNetworkConnected(false);
        }
        RequestBody body = new FormBody.Builder().add(ConstantUtil.PARAMETER, jsonParams).build();
        Request request = new Request.Builder()
                .url(URLConfig.SERVER_HOST + action)
                .post(body)
                .build();
        return new Cross(action, params, isNeedCacheResponseData, isNeedShowErrorMessage, client.newCall(request)).setNetworkConnected(networkConnected);
    }

    /**
     * 上传文件
     *
     * @param action 上传路径后缀
     * @param files  多个上传文件
     * @return 请求实体
     */
    public static Cross buildPostCall(String action, List<File> files) {
        MultipartBody.Builder multipartBodybuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody fileBody;
        String fileName;
        for (File file : files) {
            fileName = file.getName();
            fileBody = RequestBody.create(guessMimeType(fileName), file);
            multipartBodybuilder.addFormDataPart("fileImg", fileName, fileBody);
        }
        RequestBody requestBody = multipartBodybuilder.build();
        Request request = new Request.Builder().post(requestBody).url(URLConfig.SERVER_HOST + action).build();
        return new Cross(action, client.newCall(request)).setNetworkConnected(CommonUtil.isNetworkConnected(LoverApplication.getInstance()));
    }

    /**
     * 执行post请求
     *
     * @param cross 穿越
     */
    public static Observable<Cross> executePostCall(final Cross cross) {
        if (!cross.isNetworkConnected || cross.call == null) {
            return Observable.unsafeCreate((subscriber) -> {
                if (!TextUtils.isEmpty(cross.body)) {
                    subscriber.onNext(cross);
                    subscriber.onCompleted();
                } else subscriber.onError(new NetworkRequestException(cross, ""));
            });
        }
        return Observable.unsafeCreate((subscriber) ->
                cross.call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        NetworkRequestException nre = new NetworkRequestException(cross, "network request fail");
                        nre.initCause(e);
                        subscriber.onError(nre);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String body = response.body().string();
                            if (!TextUtils.isEmpty(body)) {
                                CommonUtil.log(body);
                                if (cross.isNeedCacheResponseData)
                                    cache.put(cross.action + "---" + CommonUtil.parseToJsonStr(cross.params), body);
                                cross.body = body;
                                subscriber.onNext(cross);
                            } else
                                subscriber.onError(new NetworkRequestException(cross, response.toString()));
                        } else
                            subscriber.onError(new NetworkRequestException(cross, response.toString()));
                        response.close();
                        subscriber.onCompleted();
                    }
                })
        );
    }

    /**
     * 根据文件名称猜测文件MediaType
     *
     * @param fileName 文件名称
     */
    private static MediaType guessMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentType = fileNameMap.getContentTypeFor(fileName);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return MediaType.parse(contentType);
    }

    /**
     * 下载文件
     *
     * @param context  上下文对象
     * @param url      文件url
     * @param listener 文件下载事件监听
     */
    public static void downloadFile(Context context, String url, final OnDownloadFileListener listener) {
        if (context == null || TextUtils.isEmpty(url) || !CommonUtil.isExistSdcard()) return;
        final String fileName = url.substring(url.lastIndexOf("/") + 1);
        if (TextUtils.isEmpty(fileName)) return;
        if (!CommonUtil.isNetworkConnected(context)) {
            CommonUtil.toast(context, context.getString(R.string.net_not_ok));
            return;
        }
        Request request = new Request.Builder().url(url).build();
        if (listener != null) listener.onStart();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    File file = new File(ConstantUtil.PICTURE_PATH);
                    if (!file.exists()) file.mkdirs();
                    String path = ConstantUtil.PICTURE_PATH + fileName;
                    file = new File(path);
                    byte[] buf = new byte[2048];
                    int len;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    if (listener != null) listener.onCompleted("文件保存在" + path);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) listener.onCompleted("文件保存失败");
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
                if (listener != null) listener.onCompleted("文件保存失败");
            }
        });

    }

    /**
     * 文件下载事件监听
     */
    public interface OnDownloadFileListener {
        /**
         * 开始下载
         */
        void onStart();

        /**
         * 下载完成
         */
        void onCompleted(String message);
    }
}
