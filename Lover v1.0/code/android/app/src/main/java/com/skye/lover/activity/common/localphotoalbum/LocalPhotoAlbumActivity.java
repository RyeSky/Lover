package com.skye.lover.activity.common.localphotoalbum;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.skye.lover.ConstantUtil;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.adapter.PhotoFolderAdapter;
import com.skye.lover.model.PhotoFolder;
import com.skye.lover.task.LoadPhotosTask;
import com.skye.lover.util.CommonUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;

/**
 * 本地手机相册界面
 */
public class LocalPhotoAlbumActivity extends BaseActivity {
    public static final String MAX_PHOTOS_SIZE = "max_photos_size";//最大选中张数
    public static final String PHOTO_LIST = "photo_list";//图片集合
    private final int PHOTO_FOLDER = 0;//相册文件夹
    @BindView(R.id.gv_photo_folder_container)
    GridView gvPhotoFolderContainer;//图片文件夹容器
    private PhotoFolderAdapter adapter;//图片文件夹适配器
    private int maxPhotosSize;//最大选中张数

    @Override
    public void handleMsg(Message msg) {
        super.handleMsg(msg);
        switch (msg.what) {
            case 0:
                CommonUtil.closeLoadingDialog(dialog);
                @SuppressWarnings("unchecked")
                List<PhotoFolder> list = (List<PhotoFolder>) msg.obj;
                if (list == null || list.isEmpty()) return;
                adapter.setList(list);
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_local_photo_album;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.local_photo_album);
        topbar.left.setVisibility(View.VISIBLE);
        maxPhotosSize = getIntent().getIntExtra(MAX_PHOTOS_SIZE, ConstantUtil.MAX_PHOTOS_SIZE);
        adapter = new PhotoFolderAdapter(context);
        gvPhotoFolderContainer.setAdapter(adapter);
        dialog = CommonUtil.showLoadingDialog(context);
        new LoadPhotosTask(context, handler.obtainMessage(0)).execute();
    }

    @OnItemClick(R.id.gv_photo_folder_container)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PhotoFolder pf = (PhotoFolder) parent.getItemAtPosition(position);
        if (pf == null) return;
        startActivityForResult(new Intent(context, PhotoFolderActivity.class)
                .putExtra(PhotoFolderActivity.PHOTO_FOLDER, pf).putExtra(PhotoFolderActivity.MAX_PHOTOS_SIZE,maxPhotosSize), PHOTO_FOLDER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_FOLDER://相册文件夹
                    setResult(RESULT_OK, data);
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
}
