package com.skye.lover.util;

import android.content.Context;
import android.text.TextUtils;

import com.skye.lover.ConstantUtil;
import com.skye.lover.R;
import com.skye.lover.exception.NoNetworkConnectException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 网络请求工具类
 */
public class OkHttpUtil {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient mOkHttpClient = new OkHttpClient().newBuilder().connectTimeout(15, TimeUnit.SECONDS).build();

    /**
     * post请求
     *
     * @param context  上下文对象
     * @param action   请求动作
     * @param params   请求的参数,如果没有可为空
     * @param callback 回调函数
     */
    public static void doPost(Context context, String action, Map<String, String> params, Callback callback) {
        if (context == null) return;
        if (!CommonUtil.isNetworkConnected(context)) {
            CommonUtil.toast(context, context.getString(R.string.net_not_ok));
            if (callback != null)
                callback.onFailure(null, new NoNetworkConnectException());
            return;
        }
        if (params != null)
            CommonUtil.log(URLConfig.SERVER_HOST + action + "\n" + CommonUtil.parseToJsonStr(params));
        RequestBody body = new FormBody.Builder().add(ConstantUtil.PARAMETER, CommonUtil.parseToJsonStr(params)).build();
        Request request = new Request.Builder()
                .url(URLConfig.SERVER_HOST + action)
                .post(body)
                .build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }


    /**
     * 这里使用了HttpClinet的API。只是为了方便
     *
     * @param params 请求参数
     */
    private static String formatParams(Map<String, String> params) {
        if (params == null || params.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            if (sb.length() > 0) sb.append("&");
            sb.append(key);
            sb.append("=");
            sb.append(params.get(key));
        }
        return sb.toString();
    }

    /**
     * 为HttpGet 的 url 方便的添加多个name value 参数。
     *
     * @param url    接口路径
     * @param params 请求参数
     */
    private static String attachHttpGetParams(String url, Map<String, String> params) {
        return url + "?" + formatParams(params);
    }

    /**
     * get请求
     *
     * @param context  上下文对象
     * @param action   请求动作
     * @param params   请求的参数,如果没有可为空
     * @param callback 回调函数
     */
    public static void doGet(Context context, String action, Map<String, String> params, Callback callback) throws IOException {
        if (context == null) return;
        if (!CommonUtil.isNetworkConnected(context)) {
            CommonUtil.toast(context, context.getString(R.string.net_not_ok));
            if (callback != null)
                callback.onFailure(null, new NoNetworkConnectException());
            return;
        }
        Request request = new Request.Builder().url(attachHttpGetParams(URLConfig.SERVER_HOST + action, params)).get().build();
        mOkHttpClient.newCall(request).enqueue(callback);
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
     * 上传单个图片
     *
     * @param context  上下文对象
     * @param file     上传文件
     * @param action   上传路径后缀
     * @param callback 回调函数
     */
    public static void uploadFile(Context context, File file, String action, Callback callback) {
        if (context == null || file == null) return;
        if (!CommonUtil.isNetworkConnected(context)) {
            CommonUtil.toast(context, context.getString(R.string.net_not_ok));
            if (callback != null)
                callback.onFailure(null, new NoNetworkConnectException());
            return;
        }
        MultipartBody.Builder multipartBodybuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        String fileName = file.getName();
        RequestBody fileBody = RequestBody.create(guessMimeType(fileName), file);
        multipartBodybuilder.addFormDataPart("fileImg", fileName, fileBody);
        RequestBody requestBody = multipartBodybuilder.build();
        Request request = new Request.Builder().post(requestBody).url(URLConfig.SERVER_HOST + action).build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 上传单个图片
     *
     * @param context  上下文对象
     * @param files    多个上传文件
     * @param action   上传路径后缀
     * @param callback 回调函数
     */
    public static void uploadFiles(Context context, List<File> files, String action,
                                   Callback callback) {
        if (context == null || files == null || files.isEmpty()) return;
        if (!CommonUtil.isNetworkConnected(context)) {
            CommonUtil.toast(context, context.getString(R.string.net_not_ok));
            if (callback != null)
                callback.onFailure(null, new NoNetworkConnectException());
            return;
        }
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
        mOkHttpClient.newCall(request).enqueue(callback);
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
        mOkHttpClient.newCall(request).enqueue(new Callback() {
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
                    int len ;
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
