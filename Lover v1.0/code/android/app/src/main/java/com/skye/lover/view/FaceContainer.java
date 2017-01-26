package com.skye.lover.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.skye.lover.R;
import com.skye.lover.adapter.EmojiAdapter;
import com.skye.lover.adapter.FaceAdapter;
import com.skye.lover.model.Emoji;
import com.skye.lover.util.FaceUtils;

import java.util.ArrayList;
import java.util.List;

public class FaceContainer extends LinearLayout implements OnItemClickListener {
    private final int PAGE_SIZE = 20; // 表情的数量
    public List<List<Emoji>> pageData = new ArrayList<>(); // 每一页表情的集合
    private Context context;
    private LayoutInflater inflater;
    private ViewPager faceVp;
    private LinearLayout indexContainer;
    private List<Emoji> emojis = new ArrayList<>(); // 所有表情的集合
    private List<FaceAdapter> adapters; // 每一个page所在的适配器的集合
    private int currentPage = 0;
    private ArrayList<ImageView> points; // 小圆点的集合
    private OnFaceOprateListener listener;

    public FaceContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        inflater.inflate(R.layout.face_container, this, true);
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.BOTTOM);
        faceVp = (ViewPager) findViewById(R.id.face_viewpager);
        indexContainer = (LinearLayout) findViewById(R.id.face_index);
        parseData();
        initView();
    }

    @SuppressLint("InflateParams")
    private void initView() {
        ArrayList<View> pages = new ArrayList<>();
        adapters = new ArrayList<>();
        GridView gv;
        FaceAdapter adapter;
        for (int i = 0; i < pageData.size(); i++) {
            gv = (GridView) inflater.inflate(R.layout.face_gridview, null);
            adapter = new FaceAdapter(context, pageData.get(i));
            gv.setAdapter(adapter);
            adapters.add(adapter);
            gv.setOnItemClickListener(this);
            pages.add(gv);
        }

        points = new ArrayList<>();
        ImageView img;
        LayoutParams lp = new LayoutParams(
                new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        lp.leftMargin = 10;
        lp.rightMargin = 10;
        lp.width = 8;
        lp.height = 8;
        for (int i = 0; i < pages.size(); i++) {
            img = new ImageView(context);
            img.setBackgroundResource( R.drawable.shape_index_unchecked);
            indexContainer.addView(img, lp);
            points.add(img);
        }

        faceVp.setAdapter(new EmojiAdapter(pages));
        faceVp.setCurrentItem(0);
        currentPage = 0;
        points.get(0).setBackgroundResource(R.drawable.shape_index_checked);
        faceVp.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                currentPage = arg0;
                faceVp.setCurrentItem(arg0);
                for (int i = 0; i < points.size(); i++)
                    points.get(i).setBackgroundResource(arg0 == i ? R.drawable.shape_index_checked : R.drawable.shape_index_unchecked);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    // 初始化表情数据
    private void parseData() {
        Emoji emoji;
        int len = FaceUtils.faceImgNames.length;
        for (int i = 0; i < len; i++) {
            emoji = new Emoji();
            emoji.setCharacter(FaceUtils.faceImgNames[i]);
            emoji.setString(convertUnicode(FaceUtils.faceImgNames[i])); // 全部解析为Unicode字符串，用于在textview展示
            emojis.add(emoji);
        }
        int pageCount = emojis.size() / PAGE_SIZE;
        if (emojis.size() % PAGE_SIZE != 0) pageCount++;
        for (int i = 0; i < pageCount; i++) {
            pageData.add(getData(i)); // 页面集合，封装每个页面里面是表情，每一个都是一个List对象
        }
    }

    /**
     * 获取分页数据
     *
     * @param page 页数
     * @return 返回指定页数的数据集合
     */
    private List<Emoji> getData(int page) {
        int startIndex = page * PAGE_SIZE;
        int endIndex = startIndex + PAGE_SIZE;
        if (endIndex > emojis.size())
            endIndex = emojis.size();
        List<Emoji> list = new ArrayList<>();
        list.addAll(emojis.subList(startIndex, endIndex));
        if (list.size() < PAGE_SIZE) {
            for (int i = list.size(); i < PAGE_SIZE; i++)
                list.add(new Emoji());
        }
        Emoji emoji = new Emoji();
        emoji.setId(R.mipmap.face_delete); // 每一页最后面添加删除的图片
        list.add(emoji);
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listener == null) return;
        Emoji emoji = (Emoji) adapters.get(currentPage).getItem(position);
        if (emoji.getId() == R.mipmap.face_delete) {
            listener.onFaceDeleted();
        } else if (!TextUtils.isEmpty(emoji.getCharacter())) {
            listener.onFaceSelected(emoji.getString(), emoji.getCharacter());
        }
    }

    /**
     * 判断字符串是否为表情
     */
    public boolean contain(String param) {
        if (TextUtils.isEmpty(param))
            return false;
        boolean contain = false;
        Emoji emoji = new Emoji();
        emoji.setString(param);
        for (List<Emoji> item : pageData) {
            contain = item.contains(emoji);
            if (contain) {
                contain = true;
                break;
            }
        }
        return contain;
    }

    private String convertUnicode(String emo) {
        if (emo.length() < 6) {
            return new String(Character.toChars(Integer.parseInt(emo, 16)));
        }
        String[] emos = emo.split("_");
        char[] char0 = Character.toChars(Integer.parseInt(emos[0], 16));
        char[] char1 = Character.toChars(Integer.parseInt(emos[1], 16));
        char[] emoji = new char[char0.length + char1.length];
        System.arraycopy(char0, 0, emoji, 0, char0.length);
        System.arraycopy(char1, 0, emoji, char0.length, char1.length);
        return new String(emoji);
    }

    /**
     * 设置表情操作事件监听
     */
    public void setOnFaceOpreateListener(OnFaceOprateListener listener) {
        this.listener = listener;
    }

    /**
     * 表情操作事件监听
     */
    public interface OnFaceOprateListener {
        /**
         * 选中表情
         *
         * @param emoji     显示字符串
         * @param character 表情编码
         */
        void onFaceSelected(String emoji, String character);

        /**
         * 删除按钮按下
         */
        void onFaceDeleted();
    }
}
