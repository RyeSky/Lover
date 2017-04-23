package com.skye.lover.activity.pillowtalk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.skye.actionsheet.ActionSheet;
import com.skye.lover.ConstantUtil;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.activity.common.localphotoalbum.LocalPhotoAlbumActivity;
import com.skye.lover.mvp.presenter.PublishPillowTalkPresenter;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.task.CompressAlbumPhotoTask;
import com.skye.lover.task.CompressCameraPhotoTask;
import com.skye.lover.util.CommonInnerHandler;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.view.DynamicAddPictureContainer;
import com.skye.lover.view.FaceContainer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 发表悄悄话界面
 */
public class PublishPillowTalkActivity extends BaseActivity<BaseView, PublishPillowTalkPresenter> implements BaseView
        , View.OnTouchListener, DynamicAddPictureContainer.OnClickAddPictureListener, FaceContainer.OnFaceOprateListener {
    public static final String TYPE = "type";
    public static final String PILLOW_TALK = "0";
    public static final String BROADCAST = "1";
    private static final String IMAGE_FILE_NAME = "camera_photo.jpeg"; // 暂存拍照图片的名字
    private static final int CAMERA = 0;//拍照
    private static final int ALBUM = 1;//从相册中选择
    @BindView(R.id.ed_content)
    EditText content;//悄悄话内容
    @BindView(R.id.dpc)
    DynamicAddPictureContainer dpc;//动态添加图片容器
    @BindView(R.id.face_container)
    FaceContainer fc;
    private String type;
    private InnerHandler handler = new InnerHandler(this);

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_publish_pillow_talk;
    }

    @Override
    public PublishPillowTalkPresenter createPresenter() {
        return new PublishPillowTalkPresenter();
    }

    @Override
    public int getDescription() {
        return R.string.publish_pillow_talk_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.publish_pillow_talk);
        topbar.left.setVisibility(View.VISIBLE);
        content.setOnTouchListener(this);
        dpc.setOnClickAddPictureListener(this);
        fc.setOnFaceOpreateListener(this);
        type = getIntent().getStringExtra(TYPE);
        if (TextUtils.isEmpty(type)) type = BROADCAST;

        //确定
        RxView.clicks(findViewById(R.id.btn_ok)).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((v) -> {
                    final String content = CommonUtil.getEditTextString(this.content);
                    if (TextUtils.isEmpty(content) && dpc.isEmpty()) {
                        CommonUtil.toast(context, R.string.content_image_can_not_empty);
                        return;
                    }
                    if (dpc.isEmpty())
                        presenter.publishPillowTalk(type, content);
                    else {//需要上传图片
                        List<String> pictures = dpc.getPictures();
                        List<File> files = new ArrayList<>();
                        for (String picture : pictures) files.add(new File(picture));
                        presenter.publishPillowTalk(type, content, files);
                    }
                });
        // 调用表情键盘
        RxView.clicks(findViewById(R.id.call_face_container)).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((v) -> {
                    if (fc.getVisibility() == View.VISIBLE) {//正在显示表情面板
                        fc.setVisibility(View.GONE);
                        handler.postDelayed(this::displaySoftKeyboard, 300);
                    } else {
                        hideSoftKeyboard();
                        handler.postDelayed(() -> fc.setVisibility(View.VISIBLE), 300);
                    }
                });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        fc.setVisibility(View.GONE);
        return true;
    }

    @Override
    public void onFaceSelected(String emoji, String character) {
        int index = content.getSelectionStart();
        Editable edit = content.getEditableText();
        if (index < 0 || index >= edit.length()) {
            edit.append(emoji);
        } else {
            edit.insert(index, emoji);
        }
    }

    @Override
    public void onFaceDeleted() {
        int selection = content.getSelectionStart();
        String text = content.getText().toString();
        if (selection >= 1) {
            try {
                String text2 = text.substring(selection - 2, selection);
                if (!TextUtils.isEmpty(text2) && fc.contain(text2)) {// 双字节表情
                    content.getText().delete(selection - 2, selection);
                    return;
                }
                String text4 = text.substring(selection - 4, selection);
                if (!TextUtils.isEmpty(text4) && fc.contain(text4)) {// 四字节表情
                    content.getText().delete(selection - 4, selection);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            content.getText().delete(selection - 1, selection);
        }
    }

    @Override
    public void onClickAddPicture() {
        hideSoftKeyboard();
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(context);
        menuView.setCancelButtonTitle(getString(R.string.cancel));
        menuView.addItems("拍照", "从相册选择");
        menuView.setItemClickListener((int itemPosition) -> {
            switch (itemPosition) {
                case 0:// 拍照
                    if (!CommonUtil.isExistSdcard()) {// 先查看是否有外存储卡
                        CommonUtil.toast(context, R.string.no_sd_card_take_photo);
                        return;
                    }
                    try {
                        startActivityForResult(CommonUtil.getCameraIntent(IMAGE_FILE_NAME), CAMERA);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:// 从相册选择
                    if (!CommonUtil.isExistSdcard()) {// 先查看是否有外存储卡
                        CommonUtil.toast(context, R.string.no_sd_card_select_photo);
                        return;
                    }
                    startActivityForResult(new Intent(context, LocalPhotoAlbumActivity.class)
                            .putExtra(LocalPhotoAlbumActivity.MAX_PHOTOS_SIZE, ConstantUtil.MAX_PHOTOS_SIZE - dpc.size()), ALBUM);
                    break;
                default:
                    break;
            }
        });
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA://拍照
                    File file = new File(ConstantUtil.PICTURE_PATH + IMAGE_FILE_NAME);
                    if (file.exists())
                        new CompressCameraPhotoTask(file.getAbsolutePath(), handler.obtainMessage(CAMERA)).execute();
                    else CommonUtil.toast(context, R.string.get_photo_fail);
                    break;
                case ALBUM://从相册中选择
                    @SuppressWarnings("unchecked")
                    List<String> ls = (List<String>) data.getSerializableExtra(LocalPhotoAlbumActivity.PHOTO_LIST);
                    if (ls == null || ls.isEmpty()) {
                        CommonUtil.toast(context, R.string.get_photo_fail);
                        return;
                    }
                    new CompressAlbumPhotoTask(ls, handler.obtainMessage(ALBUM)).execute();
                    break;
                default:
                    break;
            }
        }
    }

    private static class InnerHandler extends CommonInnerHandler<PublishPillowTalkActivity> {
        InnerHandler(PublishPillowTalkActivity publishPillowTalkActivity) {
            super(publishPillowTalkActivity);
        }

        @Override
        public void handleMessage(PublishPillowTalkActivity publishPillowTalkActivity, Message msg) {
            if (publishPillowTalkActivity == null || publishPillowTalkActivity.isFinishing())
                return;
            switch (msg.what) {
                case CAMERA://拍照
                    try {
                        publishPillowTalkActivity.dpc.add((String) msg.obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                        publishPillowTalkActivity.toast(R.string.get_photo_fail);
                    }
                    break;
                case ALBUM://从相册中选择
                    try {
                        @SuppressWarnings("unchecked")
                        List<String> ls = (List<String>) msg.obj;
                        if (ls == null || ls.isEmpty()) {
                            publishPillowTalkActivity.toast(R.string.get_photo_fail);
                            return;
                        }
                        publishPillowTalkActivity.dpc.add(ls);
                    } catch (Exception e) {
                        e.printStackTrace();
                        publishPillowTalkActivity.toast(R.string.get_photo_fail);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
