package com.skye.lover.activity.pillowtalk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.skye.actionsheet.ActionSheet;
import com.skye.lover.ConstantUtil;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.activity.common.localphotoalbum.LocalPhotoAlbumActivity;
import com.skye.lover.exception.NoNetworkConnectException;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.FilePaths;
import com.skye.lover.task.CompressAlbumPhotoTask;
import com.skye.lover.task.CompressCameraPhotoTask;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareData;
import com.skye.lover.util.URLConfig;
import com.skye.lover.view.DynamicAddPictureContainer;
import com.skye.lover.view.FaceContainer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 发表悄悄话界面
 */
public class PublishPillowTalkActivity extends BaseActivity implements View.OnTouchListener,
        DynamicAddPictureContainer.OnClickAddPictureListener, FaceContainer.OnFaceOprateListener {
    public static final String TYPE = "type";
    public static final String PILLOW_TALK = "pillowTalk";
    public static final String BROADCAST = "broadcast";
    private static final String IMAGE_FILE_NAME = "camera_photo.jpeg"; // 暂存拍照图片的名字
    private final int CAMERA = 0;//拍照
    private final int ALBUM = 1;//从相册中选择
    @BindView(R.id.ed_content)
    EditText content;//悄悄话内容
    @BindView(R.id.dpc)
    DynamicAddPictureContainer dpc;//动态添加图片容器
    @BindView(R.id.face_container)
    FaceContainer fc;
    private String type;
    /**
     * 发表悄悄话接口回调
     */
    private Callback callbackPublishPillowTalk = new Callback() {
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
                            CommonUtil.toast(context, R.string.publish_success);
                            setResult(RESULT_OK);
                            finish();
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

    @Override
    public void handleMsg(Message msg) {
        super.handleMsg(msg);
        switch (msg.what) {
            case CAMERA://拍照
                try {
                    dpc.add((String) msg.obj);
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtil.toast(context, R.string.get_photo_fail);
                }
                break;
            case ALBUM://从相册中选择
                try {
                    @SuppressWarnings("unchecked")
                    List<String> ls = (List<String>) msg.obj;
                    if (ls == null || ls.isEmpty()) {
                        CommonUtil.toast(context, R.string.get_photo_fail);
                        return;
                    }
                    dpc.add(ls);
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtil.toast(context, R.string.get_photo_fail);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_publish_pillow_talk;
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

    @OnClick({R.id.btn_ok, R.id.call_face_container})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok://发表
                final String content = CommonUtil.getEditTextString(this.content);
                if (TextUtils.isEmpty(content) && dpc.isEmpty()) {
                    CommonUtil.toast(context, R.string.content_image_can_not_empty);
                    return;
                }
                if (dpc.isEmpty()) publish(content, "");
                else {//需要上传图片
                    List<String> pictures = dpc.getPictures();
                    List<File> files = new ArrayList<>();
                    for (String picture : pictures) files.add(new File(picture));
                    dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                    OkHttpUtil.uploadFiles(context, files, URLConfig.ACTION_UPLOAD_FILE, new Callback() {
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
                                            publish(content, fp.getFilePaths());
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
                    });
                }
                break;
            case R.id.call_face_container://调用表情键盘
                if (fc.getVisibility() == View.VISIBLE) {//正在显示表情面板
                    fc.setVisibility(View.GONE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            displaySoftKeyboard();
                        }
                    }, 300);
                } else {
                    hideSoftKeyboard();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fc.setVisibility(View.VISIBLE);
                        }
                    }, 300);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 发表悄悄话
     */
    private void publish(String content, String imgs) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", ShareData.getShareStringData(ShareData.ID));
        params.put("type", PILLOW_TALK.equals(type) ? "0" : "1");
        params.put("content", content);
        params.put("imgs", imgs);
        dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
        OkHttpUtil.doPost(context, URLConfig.ACTION_PUBLISH_PILLOW_TALK, params, callbackPublishPillowTalk);
    }

    @Override
    public void onClickAddPicture() {
        hideSoftKeyboard();
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(context);
        menuView.setCancelButtonTitle(getString(R.string.cancel));
        menuView.addItems("拍照", "从相册选择");
        menuView.setItemClickListener(new ActionSheet.MenuItemClickListener() {
            @Override
            public void onItemClick(int itemPosition) {
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
}
