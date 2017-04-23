package com.skye.lover.activity.common.localphotoalbum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.skye.lover.ConstantUtil;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.adapter.PhotoAdapter;
import com.skye.lover.model.Photo;
import com.skye.lover.model.PhotoFolder;
import com.skye.lover.mvp.presenter.BasePresenterEmptyImpl;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnItemClick;

/**
 * 相册文件夹界面
 */
public class PhotoFolderActivity extends BaseActivity<BaseView, BasePresenterEmptyImpl> {
    public static final String PHOTO_FOLDER = "photo_folder";//相册文件夹数据
    public static final String MAX_PHOTOS_SIZE = "max_photos_size";//最大选中张数
    @BindView(R.id.gv_photo_container)
    GridView gvPhotoContainer;//图片容器
    @BindView(R.id.tv_tip)
    TextView tip;//图片张数提示
    private PhotoAdapter adapter;//图片适配器
    private PhotoFolder pf;//相册文件夹数据
    private int maxPhotosSize;//最大选中张数

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_photo_folder;
    }

    @Override
    public BasePresenterEmptyImpl createPresenter() {
        return null;
    }

    @Override
    public int getDescription() {
        return R.string.photo_folder_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.left.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        pf = intent.getParcelableExtra(PHOTO_FOLDER);
        maxPhotosSize = intent.getIntExtra(MAX_PHOTOS_SIZE, ConstantUtil.MAX_PHOTOS_SIZE);
        topbar.title.setText(pf.getDisplayName());
        adapter = new PhotoAdapter(context);
        gvPhotoContainer.setAdapter(adapter);
        adapter.setList(pf.getPhotos());

        RxView.clicks(findViewById(R.id.btn_ok)).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) -> {
            if (adapter.getSelectedPhotosSize() > 0) //选中了图片
                setResult(RESULT_OK, new Intent().putExtra(LocalPhotoAlbumActivity.PHOTO_LIST, (Serializable) adapter.getSelectedPhotos()));
            finish();
        });
    }

    @OnItemClick(R.id.gv_photo_container)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Photo photo = (Photo) parent.getItemAtPosition(position);
        if (photo == null) return;
        View checked = view.findViewById(R.id.checked);
        if (adapter.getSelectedPhotosSize() != maxPhotosSize) {//没有到图片张数限制，可以选中或取消选中
            photo.setChecked(!photo.isChecked());//选中状态取反
            checked.setVisibility(photo.isChecked() ? View.VISIBLE : View.GONE);//选中标志是否显示
        } else {//已经到达图片张数限制
            if (adapter.isSelected(photo)) {//已经选中，可以取消选中
                photo.setChecked(false);//选中状态取反
                checked.setVisibility(View.GONE);//不显示选中图标
            } else//没有选中，不能再选中
                CommonUtil.toast(context, "最多选择" + maxPhotosSize + "张图片");
        }
        tip.setText("已选择" + adapter.getSelectedPhotosSize() + "张图片");
    }
}
