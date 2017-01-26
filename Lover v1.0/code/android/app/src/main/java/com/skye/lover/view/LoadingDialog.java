package com.skye.lover.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.skye.lover.R;

/**
 * 加载对话框
 */
public class LoadingDialog extends Dialog {
    private Context context;
    private String loadingText;

    public LoadingDialog(Context context, String loadingText) {
        super(context, R.style.loading_dialog);
        this.context = context;
        this.loadingText = loadingText;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        TextView txt = (TextView) findViewById(R.id.txt);
        txt.setText(TextUtils.isEmpty(loadingText) ? context.getString(R.string.loading) : loadingText);
        ObjectAnimator
                oa = ObjectAnimator.ofInt(findViewById(R.id.progress), "backgroundColor",
                0xFFFC2D35, 0xFFFFA43A, 0xFFFFE550, 0xFF9EE144, 0xFF22AF3E, 0xFF00A5F7, 0xFF0267CF, 0xFF7B2AB7, 0xFFB120AB);
        oa.setInterpolator(new LinearInterpolator());
        oa.setDuration(5000);
        oa.setRepeatCount(-1);
        oa.setRepeatMode(ValueAnimator.REVERSE);
        oa.setEvaluator(new ArgbEvaluator());
        oa.start();
    }

}
