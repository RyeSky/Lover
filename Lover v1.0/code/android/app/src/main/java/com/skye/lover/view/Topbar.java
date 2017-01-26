package com.skye.lover.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skye.lover.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 标题栏
 */
public class Topbar extends RelativeLayout {
    @BindView(R.id.tv_left)
    public TextView left;//左侧文字
    @BindView(R.id.tv_right)
    public TextView right;//右侧文字
    @BindView(R.id.tv_title)
    public TextView title;//标题

    public Topbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.topbar, this, true);
        ButterKnife.bind(this);
        setBackgroundColor(context.getResources().getColor(R.color.bg_blue));
    }
}
