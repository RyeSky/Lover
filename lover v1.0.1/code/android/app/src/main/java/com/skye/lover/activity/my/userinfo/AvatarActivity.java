package com.skye.lover.activity.my.userinfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.request.ImageRequest;
import com.jakewharton.rxbinding.view.RxView;
import com.skye.actionsheet.ActionSheet;
import com.skye.lover.ConstantUtil;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.model.User;
import com.skye.lover.mvp.presenter.AvatarPresenter;
import com.skye.lover.mvp.view.AvatarView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;
import com.skye.lover.view.fresco.zoomable.ZoomableDraweeView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 头像界面
 */
public class AvatarActivity extends BaseActivity<AvatarView, AvatarPresenter> implements AvatarView {
    public static final String AVATAR = "avatar";//头像
    private static final String IMAGE_FILE_NAME = "avatar.jpeg"; // 暂存用户头像的文字
    private static final String IMAGE_FILE_TEMP = "temp.jpeg"; // 上传图片的地址
    private static final int CAMERA = 1;
    private static final int ALBUM = 2;
    private static final int COMPRESS = 3;
    @BindView(R.id.img_avatar)
    ZoomableDraweeView avatar;//头像
    private String headerImg;
    private Handler handler = new Handler();

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_avatar;
    }

    @Override
    public AvatarPresenter createPresenter() {
        return new AvatarPresenter();
    }

    @Override
    public int getDescription() {
        return R.string.avatar_activity;
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

        //更多
        RxView.clicks(topbar.right).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) -> more());
    }

    /**
     * 设置头像
     */
    private void setAvatar() {
        String photo = !TextUtils.isEmpty(headerImg) ? headerImg : ShareDataUtil.get(context, User.AVATAR);//头像url,为空说明是登录用户的头像
        DraweeController controller = Fresco.newDraweeControllerBuilder().setUri(Uri.parse(URLConfig.SERVER_HOST + photo)).build();
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .setFailureImage(CommonUtil.emptyFailureImage, ScalingUtils.ScaleType.FOCUS_CROP)
                .setProgressBarImage(new ProgressBarDrawable())
                .build();
        avatar.setController(controller);
        avatar.setHierarchy(hierarchy);
    }

    @Override
    public void updateAvatar() {
        avatar.setController(Fresco.newDraweeControllerBuilder().
                setImageRequest(ImageRequest.fromUri(URLConfig.SERVER_HOST + ShareDataUtil.get(context, User.AVATAR)))
                .setOldController(avatar.getController()).build());
    }

    /**
     * 更多
     */
    private void more() {
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(context);
        menuView.setCancelButtonTitle(getString(R.string.cancel));
        if (!TextUtils.isEmpty(headerImg)) menuView.addItems("保存图片");//非登录用户
        else menuView.addItems("拍照", "从相册选择", "保存图片");//登录用户
        menuView.setItemClickListener((int itemPosition) -> {
                    if (!TextUtils.isEmpty(headerImg)) {//不是登录用户
                        switch (itemPosition) {
                            case 0://保存图片
                                OkHttpUtil.downloadFile(context, URLConfig.SERVER_HOST + headerImg, new OkHttpUtil.OnDownloadFileListener() {
                                    @Override
                                    public void onStart() {
                                        showDialog(getString(R.string.please_wait));
                                    }

                                    @Override
                                    public void onCompleted(final String message) {
                                        handler.post(() -> {
                                            dismissDialog();
                                            toast(message);
                                        });
                                    }
                                });
                            default:
                                break;
                        }
                    } else {//是登录用户
                        Intent intent;
                        switch (itemPosition) {
                            case 0:// 拍照
                                if (!CommonUtil.isExistSdcard()) {// 先查看是否有外存储卡
                                    toast(R.string.no_sd_card_take_photo);
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
                                    toast(R.string.no_sd_card_select_photo);
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
                                OkHttpUtil.downloadFile(context, URLConfig.SERVER_HOST + ShareDataUtil.get(context, User.AVATAR)
                                        , new OkHttpUtil.OnDownloadFileListener() {
                                            @Override
                                            public void onStart() {
                                                showDialog(getString(R.string.please_wait));
                                            }

                                            @Override
                                            public void onCompleted(final String message) {
                                                handler.post(() -> {
                                                    dismissDialog();
                                                    toast(message);
                                                });
                                            }
                                        });
                                break;
                            default:
                                break;
                        }
                    }
                }
        );
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();
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
                            }
                            // 上传文件到服务器上
                            if (file.exists()) presenter.uploadAvatarFile(file);
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

    /**
     * 对图片进行剪切
     */
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
            toast("文件不存在"); // 文件不存在 就不去放大缩小页面了
            return;
        }
        startActivityForResult(intent, COMPRESS);
    }

    @Override
    protected void onDestroy() {
        File file = new File(ConstantUtil.PICTURE_PATH + IMAGE_FILE_NAME);
        if (file.exists()) file.delete();
        file = new File(ConstantUtil.PICTURE_PATH + IMAGE_FILE_TEMP);
        if (file.exists()) file.delete();
        super.onDestroy();
    }
}
