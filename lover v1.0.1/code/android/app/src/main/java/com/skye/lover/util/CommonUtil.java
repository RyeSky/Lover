package com.skye.lover.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.skye.lover.ConstantUtil;
import com.skye.lover.LoverApplication;
import com.skye.lover.R;
import com.skye.lover.exception.NetworkRequestException;
import com.skye.lover.model.AppVersion;
import com.skye.lover.model.User;
import com.skye.lover.view.LoadingDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import me.drakeet.materialdialog.MaterialDialog;
import rx.Observable;

public class CommonUtil {
    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值
    public static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    public static final Drawable emptyFailureImage;
    private static final String customTagPrefix = ConstantUtil.APPNAME + "_log";// tag前缀
    private static final Drawable secretFailureImage, maleFailureImage, femaleFailureImage;//头像加载失败时的占位图

    static {
        Resources resources = LoverApplication.getInstance().getResources();
        secretFailureImage = resources.getDrawable(R.drawable.shape_secret);
        maleFailureImage = resources.getDrawable(R.drawable.shape_male);
        femaleFailureImage = resources.getDrawable(R.drawable.shape_female);
        emptyFailureImage = resources.getDrawable(R.drawable.shape_empty);
    }

    /**
     * 是否有网络连接
     *
     * @param context 上下文
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }

    /**
     * 发现了app新版本
     *
     * @param activity 界面
     * @param version  版本信息
     * @return 是否有新版本
     */
    public static boolean foundNewAppVersion(Activity activity, AppVersion version) {
        if (version == null || versionNameCompare(getVersionName(activity), version.getAppVersion()) >= 0
                || TextUtils.isEmpty(version.getDownloadUrl())) return false;
        MaterialDialog dialog = new MaterialDialog(activity);
        dialog.setTitle(version.getTitle()).setMessage("新版本：" + version.getAppVersion() + "\n\n" + version.getContent())
                .setPositiveButton(activity.getString(R.string.ok), (v) -> {
                    dialog.dismiss();
                    openUrl(activity, version.getDownloadUrl());
                })
                .setNegativeButton(activity.getString(R.string.cancel), (v) -> {
                    dialog.dismiss();
                    if (version.isForceUpdate()) {
                        activity.finish();
                        exit();
                    }
                });
        dialog.show();
        return true;
    }

    /**
     * 头像加载失败时的站位图
     *
     * @param gender 性别
     */
    public static Drawable getFailureImage(int gender) {
        if (gender == User.GENDER_MALE) return maleFailureImage;
        else if (gender == User.GENDER_FEMALE) return femaleFailureImage;
        else return secretFailureImage;
    }

    /**
     * 显示默认头像
     *
     * @param gender 性别
     */
    public static int getDefaultImageResource(int gender) {
        if (gender == User.GENDER_MALE) return R.drawable.shape_male;
        else if (gender == User.GENDER_FEMALE) return R.drawable.shape_female;
        else return R.drawable.shape_secret;
    }

    /**
     * 显示文字颜色
     *
     * @param gender 性别
     */
    public static int getTextColor(int gender) {
        if (gender == 1) return R.color.male;
        else if (gender == 2) return R.color.female;
        else return R.color.secret;
    }

    /**
     * 退出应用
     */
    public static void exit() {
        Observable.timer(1, TimeUnit.SECONDS).subscribe((Void) ->
                android.os.Process.killProcess(android.os.Process.myPid()));
    }

    /**
     * 显示进度对话框
     *
     * @param context 上下文
     */
    public static LoadingDialog showLoadingDialog(Context context) {
        return showLoadingDialog(context, "");
    }

    /**
     * 显示进度对话框
     *
     * @param context     上下文
     * @param loadingText 加载框显示文字
     */
    public static LoadingDialog showLoadingDialog(Context context, String loadingText) {
        if (context == null)
            return null;
        try {
            LoadingDialog dialog = new LoadingDialog(context, loadingText);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            return dialog;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 显示进度对话框
     *
     * @param context     上下文
     * @param loadingText 加载框显示文字
     */
    public static LoadingDialog showLoadingDialog(Context context, int loadingText) {
        if (context == null)
            return null;
        try {
            LoadingDialog dialog = new LoadingDialog(context, context.getString(loadingText));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            return dialog;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关闭进度对话框
     */
    public static void closeLoadingDialog(Dialog pd) {
        if (pd != null && pd.isShowing()) {
            try {
                pd.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 显示吐司
     *
     * @param context 上下文
     * @param text    吐司内容
     */
    public static void toast(Context context, String text) {
        if (context != null && !TextUtils.isEmpty(text)) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            if (ConstantUtil.DEBUG)
                Log.i(generateTag(), text);
        }
    }

    /**
     * 显示吐司
     *
     * @param context 上下文
     * @param resId   吐司内容字符串资源id
     */
    public static void toast(Context context, int resId) {
        if (context != null) {
            Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
            if (ConstantUtil.DEBUG)
                Log.i(generateTag(), context.getString(resId));
        }
    }

    /**
     * 生成日志tag
     */
    private static String generateTag() {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }

    /**
     * 处理网络请求异常
     *
     * @param e 异常
     */
    public static void handleNetworkRequestException(Throwable e) {
        if (e == null) return;
        e.printStackTrace();
        if (e instanceof NetworkRequestException) {
            NetworkRequestException nre = (NetworkRequestException) e;
            Throwable oe = nre.getCause();
            if (oe != null) oe.printStackTrace();
        }
    }

    /**
     * 打印log日志
     */
    public static void log(String msg) {
        if (ConstantUtil.DEBUG)
            Log.i(generateTag(), msg);
    }

    /**
     * json字符串转为对象
     *
     * @param param 要转换的字符串
     * @param clazz 要被转换的类型
     * @return T 指定类型的对象
     */
    public static <T> T parseToObject(String param, Class<T> clazz) {
        T obj = null;
        try {
            obj = gson.fromJson(param, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * json字符串转为对象
     *
     * @param param 要转换的对象
     * @param clazz 要被转换的类型
     * @return T 指定类型的对象
     */
    public static <T> T parseToObject(JsonElement param, Class<T> clazz) {
        T obj = null;
        try {
            obj = gson.fromJson(param, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 对象转为json字符串
     *
     * @param obj 要被转换的对象
     * @return String json字符串
     */
    public static String parseToJsonStr(Object obj) {
        String json = "";
        try {
            json = gson.toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 获取应用版本
     *
     * @param context 上下文
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取屏幕宽度
     *
     * @param context 上下文
     */
    @SuppressWarnings("deprecation")
    public static int getWindowWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕高度
     *
     * @param context 上下文
     */
    @SuppressWarnings("deprecation")
    public static int getWindowHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * dp to px
     *
     * @param context 上下文
     * @param dp      dp值
     */
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 检测Sdcard是否存在
     */
    public static boolean isExistSdcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取输入框里的内容
     *
     * @param edtTxt 输入框
     * @param trim   是否去掉前后空格
     */
    public static String getEditTextString(EditText edtTxt, boolean trim) {
        if (edtTxt == null)
            return "";
        if (trim)
            return edtTxt.getText().toString().trim();
        else
            return edtTxt.getText().toString();
    }

    /**
     * 获取输入框里的内容
     *
     * @param edtTxt 输入框
     */
    public static String getEditTextString(EditText edtTxt) {
        return getEditTextString(edtTxt, true);
    }

    /**
     * 从网络路径里获取文件名称
     *
     * @param param uri
     * @return 文件名称
     */
    public static String getFileNameFromUri(String param) {
        return param.substring(param.lastIndexOf("/") + 1);
    }

    /**
     * 版本字符串比较
     *
     * @param v1
     * @param v2
     * @return v1大于v2返回1;v1等于v2返回0;v1小于v2返回-1
     */
    public static int versionNameCompare(String v1, String v2) {
        try {
            String[] tmp1 = v1.split("\\.");
            String[] tmp2 = v2.split("\\.");
            if (tmp1.length != tmp2.length) {
                if (tmp1.length > tmp2.length) {
                    tmp2 = fixStringArray(tmp2, tmp1.length);
                } else {
                    tmp1 = fixStringArray(tmp1, tmp2.length);
                }
            }
            int len = tmp1.length;
            for (int i = 0; i < len; i++) {
                int a = Integer.parseInt(tmp1[i]);
                int b = Integer.parseInt(tmp2[i]);
                if (a == b) {
                    continue;
                } else {
                    if (a > b) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static String[] fixStringArray(String[] arr, int length) {
        String[] tmp = Arrays.copyOf(arr, length);
        for (int i = 0; i < tmp.length; i++) {
            if (TextUtils.isEmpty(tmp[i])) {
                tmp[i] = "0";
            }
        }
        return tmp;
    }

    /**
     * 安装应用
     *
     * @param context
     * @param uri     应用apk的uri
     */
    public static void installAPK(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 比较两个list是否相等
     */
    public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a.size() != b.size())
            return false;
        Collections.sort(a);
        Collections.sort(b);
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }

//	/**
//	 * 显示通知栏消息
//	 *
//	 * @param context
//	 *            上下文
//	 * @param icon
//	 *            图标
//	 * @param tickerText
//	 *            通知消息在状态栏显示的文字
//	 * @param contentTitle
//	 *            通知标题
//	 * @param contentText
//	 *            通知内容
//	 * @param flags
//	 *            通知flag
//	 * @param clazz
//	 *            用户点击通知后要跳转到的界面
//	 * @param map
//	 *            新界面需要的参数
//	 */
//	@SuppressLint("NewApi")
//	public static void showNotification(Context context, int icon, String tickerText, String contentTitle,
//			String contentText, int flags, Class<?> clazz, Map<String, Object> map) {
//		if (context == null)
//			return;
//		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//		if (manager == null)
//			return;
//		Intent intent = new Intent(context, clazz);
//		if (map != null && !map.isEmpty())
//			for (String key : map.keySet())
//				intent.putExtra(key, (Serializable) map.get(key));
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//		Notification notification;
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//			notification = new Notification.Builder(context).setLights(-16711936, 300, 1000).setSmallIcon(icon)
//					.setTicker(tickerText).setContentTitle(contentTitle).setContentText(contentText)
//					.setContentIntent(pendingIntent).build();
//		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//			notification = new Notification.Builder(context).setLights(-16711936, 300, 1000).setSmallIcon(icon)
//					.setTicker(tickerText).setContentTitle(contentTitle).setContentText(contentText)
//					.setContentIntent(pendingIntent).getNotification();
//		} else {
//			notification = new Notification();
//			notification.ledARGB = -16711936;
//			notification.ledOnMS = 300;
//			notification.ledOffMS = 1000;
//			notification.icon = icon;
//			notification.tickerText = tickerText;
//			notification.when = System.currentTimeMillis();
//			notification.setLatestEventInfo(context, contentTitle, contentText, pendingIntent);
//		}
//		notification.flags = flags | Notification.FLAG_AUTO_CANCEL;
//		manager.notify(0, notification);
//	}
//
//	/**
//	 * 清除所有通知栏消息
//	 *
//	 * @param context
//	 *            上下文
//	 */
//	public static void clearNotification(Context context) {
//		if (context == null)
//			return;
//		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//		if (manager == null)
//			return;
//		manager.cancelAll();
//	}

    /**
     * 使用系统中的浏览器打开指定url </br>
     * uc浏览器"："com.uc.browser", "com.uc.browser.ActivityUpdate"
     * opera："com.opera.mini.android", "com.opera.mini.android.Browser"
     * qq浏览器："com.tencent.mtt", "com.tencent.mtt.MainActivity" android 自带浏览器
     * "com.android.browser","com.android.browser.BrowserActivity"
     *
     * @param context 上下文
     * @param url     要打开的url
     * @return boolean 是否被打开
     */
    public static boolean openUrl(Context context, String url) {
        boolean canOpen = false;
        Uri uri;
        Intent intent;
        try {
            uri = Uri.parse(url);
            intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
            canOpen = true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                uri = Uri.parse(url);
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.setClassName("com.uc.browser", "com.uc.browser.ActivityUpdate");
                context.startActivity(intent);
                canOpen = true;
            } catch (Exception e1) {
                e1.printStackTrace();
                try {
                    uri = Uri.parse(url);
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    intent.setClassName("com.opera.mini.android", "com.opera.mini.android.Browser");
                    context.startActivity(intent);
                    canOpen = true;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    try {
                        uri = Uri.parse(url);
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(uri);
                        intent.setClassName("com.tencent.mtt", "com.tencent.mtt.MainActivity");
                        context.startActivity(intent);
                        canOpen = true;
                    } catch (Exception e3) {
                        e3.printStackTrace();
                        try {
                            uri = Uri.parse(url);
                            intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                            context.startActivity(intent);
                            canOpen = true;
                        } catch (Exception e4) {
                            e4.printStackTrace();
                            canOpen = false;
                        }
                    }
                }
            }
        }
        return canOpen;
    }

    /**
     * 距离现在的时间
     *
     * @param time 时间毫秒数
     * @return 格式化后的字符串
     */
    @SuppressWarnings("deprecation")
    public static String getCostDate(long time) {
        Date now = new Date();
        Date pass = new Date(time);
        long temp = now.getTime() - time;
        if (now.getYear() == pass.getYear()) {
            // 同一年
            if (now.getMonth() == pass.getMonth()) {
                // 同一月
                if (now.getDate() == pass.getDate()) {
                    // 同一天
                    if (temp < 1000 * 60 * 60 * 6) {
                        // 6小时内
                        if (temp < 1000 * 60 * 60) {
                            if (temp < 1000 * 60) {
                                return "刚刚";
                            } else {
                                // 1小时内
                                return (temp / 1000 / 60) + "分钟前";
                            }
                        } else {
                            // 1小时到6小时间
                            return (temp / 1000 / 60 / 60) + "小时前";
                        }
                    } else {
                        return new SimpleDateFormat("HH:mm", Locale.CHINA).format(time);
                    }
                } else if (now.getDate() - 1 == pass.getDate()) {
                    // 前一天
                    return new SimpleDateFormat("昨天 HH:mm", Locale.CHINA).format(time);
                } else if (now.getDate() - 2 == pass.getDate()) {
                    return new SimpleDateFormat("前天 HH:mm", Locale.CHINA).format(time);
                } else {
                    return (pass.getMonth() + 1) + "月" + pass.getDate() + "日 "
                            + new SimpleDateFormat("HH:mm", Locale.CHINA).format(time);
                }
            } else {
                return (pass.getMonth() + 1) + "月" + pass.getDate() + "日 "
                        + new SimpleDateFormat("HH:mm", Locale.CHINA).format(time);
            }
        } else {
            return (pass.getYear() + 1900) + "年" + (pass.getMonth() + 1) + "月" + pass.getDate() + "日 "
                    + new SimpleDateFormat("HH:mm", Locale.CHINA).format(time);
        }
    }

    /**
     * 格式化时间字符串
     *
     * @param time   时间毫秒数
     * @param format 时间格式/yyyy-MM-dd HH:mm:ss/yyyy-MM-dd HH:mm/yyyy-MM-dd
     * @return 格式化后的字符串
     */
    @SuppressLint("SimpleDateFormat")
    public static String getFormatedDate(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    /**
     * 通过uri获取文件对象
     *
     * @param context 上下文
     * @param uri     文件uri
     * @return uri指定的文件
     */
    public static File getFileByUri(Context context, Uri uri) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{Images.ImageColumns._ID, Images.ImageColumns.DATA}, buff.toString(), null,
                        null);
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    path = cur.getString(cur.getColumnIndex(Images.ImageColumns.DATA));
                }
                cur.close();
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {            // 4.2.2以后
            String[] proj = {Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();
            return new File(path);
        } else
            log("Uri Scheme:" + uri.getScheme());
        return null;
    }

    /**
     * 32位加密
     *
     * @param plainText 平文本
     * @return 加密后的字符串
     */
    public static String md5(String plainText) {
        StringBuilder builder = new StringBuilder("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte[] b = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    builder.append("0");
                builder.append(Integer.toHexString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    /**
     * 16位加密
     *
     * @param plainText 平文本
     * @return 加密后的字符串
     */
    public static String md5_16(String plainText) {
        try {
            StringBuffer buf = new StringBuffer("");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte[] b = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString().substring(8, 24);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 手机号验证
     *
     * @param param 手机号码
     * @return 验证通过返回true
     */
    public static boolean isMobile(String param) {
        if (TextUtils.isEmpty(param))
            return false;
        Pattern pattern = Pattern.compile("^((13[0-9])|(147)|(15[^4,\\D])|(17[0-9])|(18[0,3-9]))\\d{8}$"); // 验证手机号
        Matcher matcher = pattern.matcher(param);
        return matcher.matches();
    }

    /**
     * 邮箱验证
     *
     * @param param 邮箱地址
     * @return 验证通过返回false
     */
    public static boolean isEmail(String param) {
        Pattern pattern = Pattern.compile(
                "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(param);
        return matcher.matches();
    }

    /**
     * 压缩bitmap
     *
     * @param image 要压缩的bitmap
     * @return 压缩好的bitmap
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.toByteArray().length / 1024 > 1024) { // 循环判断如果压缩后图片是否大于1M,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
    }

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param file 文件
     * @return 文件大小
     */
    private static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                size = fis.available();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param file 文件夹文件
     * @return 文件夹大小
     */
    private static long getFileSizes(File file) {
        long size = 0;
        File flist[] = file.listFiles();
        if (flist != null && flist.length > 0) {
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getFileSizes(flist[i]);
                } else {
                    size = size + getFileSize(flist[i]);
                }
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 文件或文件夹
     */
    public static void RecursionDeleteFile(File file) {
        if (file == null) return;
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param filePath 图片绝对路径
     * @return 旋转的角度
     */
    public static int readPictureDegree(String filePath) {
        int angle = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    angle = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    angle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    angle = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return angle;
    }

    /**
     * 旋转图片
     *
     * @param angle  需要旋转的角度数
     * @param bitmap 需要进行旋转的图片对象
     * @return Bitmap 旋转后的图片对象
     */
    public static Bitmap rotaingImage(int angle, Bitmap bitmap) {
        // 旋转图片动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 获取拍照Intent
     *
     * @param imageFileName 拍照产生文件的名字
     */
    public static Intent getCameraIntent(String imageFileName) {
        File file = new File(ConstantUtil.PICTURE_PATH);
        if (!file.exists()) file.mkdirs();
        file = new File(ConstantUtil.PICTURE_PATH + imageFileName);
        Uri imageUri = Uri.fromFile(file);// 拍照时，将拍得的照片先保存在本地
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);// 然后到时照片就会保存到你指定的那个路径的IMAGE_FILE_NAME文件了.
        return intent;
    }


    /**
     * 获取软键盘的高度
     *
     * @return 软键盘的高度
     */
    public static int getSupportSoftInputHeight(Activity activity) {
        Rect r = new Rect();
        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();
        //计算软键盘的高度
        int softInputHeight = screenHeight - r.bottom;

        /**
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight(activity);
        }

        if (softInputHeight < 0) {
            CommonUtil.log("SmileKeyboard--Warning: value of softInputHeight is below zero!");
        }
        //存一份到本地
        ShareDataUtil.set(LoverApplication.getInstance(), ConstantUtil.SOFT_INPUT_HEIGHT,
                (softInputHeight > 0 ? softInputHeight : ConstantUtil.DEFAULT_SOFT_INPUT_HEIGHT) + "");
        return softInputHeight;
    }

    /**
     * 获取软键盘高度，由于第一次直接弹出表情时会出现小问题，787是一个均值，作为临时解决方案
     */
    public static int getKeyBoardHeight() {
        int softInputHeight = 0;
        String value = ShareDataUtil.get(LoverApplication.getInstance(), ConstantUtil.SOFT_INPUT_HEIGHT);
        if (!TextUtils.isEmpty(value))
            try {
                softInputHeight = Integer.parseInt(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        if (softInputHeight == 0) softInputHeight = ConstantUtil.DEFAULT_SOFT_INPUT_HEIGHT;
        return softInputHeight;
    }

    /**
     * 获取底部虚拟按键栏的高度
     *
     * @return 底部虚拟按键栏的高度
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static int getSoftButtonsBarHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    /**
     * 更新极光推送tag
     */
    public static void updateJpushTag() {
        JPushInterface.setAlias(LoverApplication.getInstance(), ShareDataUtil.get(LoverApplication.getInstance(), User.ID),
                (int arg0, String arg1, Set<String> arg2) -> {
                });
    }
}