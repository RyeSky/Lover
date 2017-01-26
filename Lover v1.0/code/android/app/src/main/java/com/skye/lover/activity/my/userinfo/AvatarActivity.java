package com.skye.lover.activity.my.userinfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.skye.actionsheet.ActionSheet;
import com.skye.lover.ConstantUtil;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.exception.NoNetworkConnectException;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.FilePaths;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareData;
import com.skye.lover.util.URLConfig;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 头像界面
 */
public class AvatarActivity extends BaseActivity {
    public static final String AVATAR = "avatar";//头像
    private static final String IMAGE_FILE_NAME = "avatar.jpeg"; // 暂存用户头像的文字
    private static final String IMAGE_FILE_TEMP = "temp.jpeg"; // 上传图片的地址
    private static final int CAMERA = 1;
    private static final int ALBUM = 2;
    private static final int COMPRESS = 3;
    @BindView(R.id.img_avatar)
    ImageView avatar;//头像
    private String headerImg, photo;
    /**
     * 更新头像接口回调
     */
    private Callback callbackUpdateAvatar = new Callback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.closeLoadingDialog(dialog);
                    if (!(e instanceof NoNetworkConnectException)) //不是没有网络
                        CommonUtil.toast(context, R.string.bad_request);
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String body = response.body().string();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.closeLoadingDialog(dialog);
                    try {
                        CommonUtil.log(body);
                        BaseResponse br = CommonUtil.parseToObject(body, BaseResponse.class);//转化为返回基类
                        if (br.check()) {
                            ShareData.setShareStringData(ShareData.AVATAR, photo);//保存新头像地址
                            setAvatar();
                        } else
                            CommonUtil.toast(context, br.message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtil.toast(context, R.string.bad_request);
                    }
                }
            });
        }
    };

    /**
     * 上传头像接口回调
     */
    private Callback callbackUploadFile = new Callback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.closeLoadingDialog(dialog);
                    if (!(e instanceof NoNetworkConnectException)) //不是没有网络
                        CommonUtil.toast(context, R.string.bad_request);
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String body = response.body().string();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        CommonUtil.log(body);
                        BaseResponse br = CommonUtil.parseToObject(body, BaseResponse.class);//转化为返回基类
                        if (br.check()) {
                            FilePaths fp = CommonUtil.parseToObject(br.result, FilePaths.class);
                            photo = fp.getFilePaths();
                            Map<String, String> params = new HashMap<>();
                            params.put("userId", ShareData.getShareStringData(ShareData.ID));
                            params.put("avatar", photo);
                            OkHttpUtil.doPost(context, URLConfig.ACTION_UPDATE_AVATAR, params, callbackUpdateAvatar);
                        } else {
                            CommonUtil.closeLoadingDialog(dialog);
                            CommonUtil.toast(context, br.message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtil.closeLoadingDialog(dialog);
                        CommonUtil.toast(context, R.string.bad_request);
                    }
                }
            });
        }
    };

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_avatar;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.avatar);
        topbar.right.setText(R.string.more);
        topbar.left.setVisibility(View.VISIBLE);
        topbar.right.setVisibility(View.VISIBLE);
        headerImg = getIntent().getStringExtra(AVATAR);
        setAvatar();
    }

    /**
     * 设置头像
     */
    private void setAvatar() {
        String photo = !TextUtils.isEmpty(headerImg) ? headerImg : ShareData.getShareStringData(ShareData.AVATAR);//头像
        //头像
        if (!TextUtils.isEmpty(photo)) {
            if (!photo.equals(avatar.getTag())) {
                ImageLoader.getInstance().displayImage(photo, avatar);
            }
        } else {
            avatar.setImageResource(R.drawable.shape_empty);
            avatar.setTag(null);
        }
    }

    @OnClick(R.id.tv_right)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right://更多
                setTheme(R.style.ActionSheetStyleIOS7);
                ActionSheet menuView = new ActionSheet(context);
                menuView.setCancelButtonTitle(getString(R.string.cancel));
                if (!TextUtils.isEmpty(headerImg)) menuView.addItems("保存图片");//非登录用户
                else menuView.addItems("拍照", "从相册选择", "保存图片");//登录用户
                menuView.setItemClickListener(new ActionSheet.MenuItemClickListener() {

                    @Override
                    public void onItemClick(int itemPosition) {
                        if (!TextUtils.isEmpty(headerImg)) {
                            switch (itemPosition) {
                                case 0://保存图片
                                    OkHttpUtil.downloadFile(context, ShareData.getShareStringData(ShareData.AVATAR), new OkHttpUtil.OnDownloadFileListener() {
                                        @Override
                                        public void onStart() {
                                            dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                                        }

                                        @Override
                                        public void onCompleted(final String message) {
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    CommonUtil.closeLoadingDialog(dialog);
                                                    CommonUtil.toast(context, message);
                                                }
                                            });
                                        }
                                    });
                                default:
                                    break;
                            }
                        } else {
                            Intent intent;
                            switch (itemPosition) {
                                case 0:// 拍照
                                    if (!CommonUtil.isExistSdcard()) {// 先查看是否有外存储卡
                                        CommonUtil.toast(context, R.string.no_sd_card_take_photo);
                                        return;
                                    }
                                    try {
                                        File file = new File(ConstantUtil.PICTURE_PATH);
                                        if (!file.exists()) file.mkdirs();
                                        file = new File(ConstantUtil.PICTURE_PATH + IMAGE_FILE_NAME);
                                        Uri imageUri = Uri.fromFile(file);// 拍照时，将拍得的照片先保存在本地
                                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);// 然后到时照片就会保存到你指定的那个路径的IMAGE_FILE_NAME文件了.
                                        startActivityForResult(intent, CAMERA);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case 1:// 从相册选择
                                    if (!CommonUtil.isExistSdcard()) {// 先查看是否有外存储卡
                                        CommonUtil.toast(context, R.string.no_sd_card_select_photo);
                                        return;
                                    }
                                    if (Build.VERSION.SDK_INT < 19) {
                                        intent = new Intent(Intent.ACTION_GET_CONTENT);
                                        intent.setType("image/*");
                                    } else {
                                        intent = new Intent(Intent.ACTION_PICK,
                                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    }
                                    startActivityForResult(intent, ALBUM);
                                    break;
                                case 2://保存图片
                                    OkHttpUtil.downloadFile(context, ShareData.getShareStringData(ShareData.AVATAR), new OkHttpUtil.OnDownloadFileListener() {
                                        @Override
                                        public void onStart() {
                                            dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                                        }

                                        @Override
                                        public void onCompleted(final String message) {
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    CommonUtil.closeLoadingDialog(dialog);
                                                    CommonUtil.toast(context, message);
                                                }
                                            });
                                        }
                                    });
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
                menuView.setCancelableOnTouchMenuOutside(true);
                menuView.showMenu();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case CAMERA:// 这个是拍照的请求码
                startPhotoZoom(Uri.fromFile(new File(ConstantUtil.PICTURE_PATH + IMAGE_FILE_NAME)));// 拍照时，将拍得的照片先保存在本地
                break;
            case ALBUM: // 这个是选择图片的请求码
                if (data != null) startPhotoZoom(data.getData());
                break;
            case COMPRESS: // 这里是裁剪后得到的图片
                try {
                    Bundle extras = data.getExtras();
                    if (null != extras) {
                        Bitmap photo = extras.getParcelable("data"); // 得到从intent里拿到的bitmap
                        Bitmap afterBitmap = CommonUtil.compressImage(photo);// 判断phtot大小，如果太大要压缩
                        if (CommonUtil.isExistSdcard()) {
                            File file = new File(ConstantUtil.PICTURE_PATH);
                            if (!file.exists()) file.mkdirs();
                            file = new File(ConstantUtil.PICTURE_PATH + IMAGE_FILE_TEMP);
                            BufferedOutputStream bos = null;
                            try {
                                bos = new BufferedOutputStream(new FileOutputStream(file));
                                afterBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos); // 保存到临时文件上
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (bos != null) {
                                    try {
                                        bos.flush();
                                        bos.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                // 上传文件到服务器上
                                if (file.exists()) {
                                    dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                                    OkHttpUtil.uploadFile(context, file, URLConfig.ACTION_UPLOAD_FILE, callbackUploadFile);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

    }

    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);

        File file = CommonUtil.getFileByUri(context, uri);// 得到图片的文件
        if (file == null || !file.exists()) {
            CommonUtil.toast(context, "文件不存在"); // 文件不存在 就不去放大缩小页面了
            return;
        }
        startActivityForResult(intent, COMPRESS);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        File file = new File(ConstantUtil.PICTURE_PATH + IMAGE_FILE_NAME);
        if (file.exists()) file.delete();
        file = new File(ConstantUtil.PICTURE_PATH + IMAGE_FILE_TEMP);
        if (file.exists()) file.delete();
    }
}
