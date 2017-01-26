package com.skye.datepicker;

import java.util.List;

import com.skye.datepickerdata.Day;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class DaiesView extends RelativeLayout implements OnClickListener {
    private Resources res;
    private int ids[] = new int[]{R.id.day0, R.id.day1, R.id.day2, R.id.day3, R.id.day4, R.id.day5, R.id.day6,
            R.id.day7, R.id.day8, R.id.day9, R.id.day10, R.id.day11, R.id.day12, R.id.day13, R.id.day14, R.id.day15,
            R.id.day16, R.id.day17, R.id.day18, R.id.day19, R.id.day20, R.id.day21, R.id.day22, R.id.day23, R.id.day24,
            R.id.day25, R.id.day26, R.id.day27, R.id.day28, R.id.day29, R.id.day30, R.id.day31, R.id.day32, R.id.day33,
            R.id.day34, R.id.day35, R.id.day36, R.id.day37, R.id.day38, R.id.day39, R.id.day40, R.id.day41};
    private TextView txt[] = new TextView[42];
    private List<Day> daies;

    public interface OnItemClickListener {
        public void onItemClick(DaiesView daies, Day day);
    }

    private OnItemClickListener listener;

    @SuppressLint("InflateParams")
    public DaiesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int widthHeight = getWindowWidth(context) / 9;
        int margin = getWindowWidth(context) / 63;
        res = context.getResources();
        LayoutInflater.from(context).inflate(R.layout.daiesview, this, true);
        LayoutParams params;
        for (int i = 0; i < 42; i++) {
            txt[i] = (TextView) findViewById(ids[i]);
            txt[i].setOnClickListener(this);
            params = (LayoutParams) txt[i].getLayoutParams();
            params.width = widthHeight;
            params.height = widthHeight;
            params.setMargins(margin, margin, margin, margin);
            txt[i].setLayoutParams(params);
        }
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setDaies(List<Day> daies) {
        this.daies = daies;
        for (int i = 0; i < daies.size(); i++) {
            Day day = daies.get(i);
            if (day.day == 0) {
                txt[i].setText("");
                txt[i].setBackgroundColor(res.getColor(android.R.color.white));
            } else {
                txt[i].setText("" + day.day);
                if (day.check) {
                    txt[i].setBackgroundResource(R.drawable.shape_light_blue_circle);
                    txt[i].setTextColor(res.getColor(android.R.color.white));
                } else {
                    txt[i].setBackgroundColor(res.getColor(android.R.color.white));
                    txt[i].setTextColor(res.getColor(R.color.black));
                }
            }
            txt[i].setTag(Integer.valueOf(i));
            if (i > 27)
                txt[i].setVisibility(VISIBLE);
        }
        for (int i = daies.size(); i < 42; i++) {
            txt[i].setTag(null);
            txt[i].setVisibility(GONE);
        }
    }

    public void setDay(Day day) {
        daies.get(day.dayPosition).check = day.check;
        if (day.check) {
            txt[day.dayPosition].setBackgroundResource(R.drawable.shape_light_blue_circle);
            txt[day.dayPosition].setTextColor(res.getColor(android.R.color.white));
        } else {
            txt[day.dayPosition].setBackgroundColor(res.getColor(android.R.color.white));
            txt[day.dayPosition].setTextColor(res.getColor(R.color.black));
        }
    }

    @Override
    public void onClick(View view) {
        Object tag = view.getTag();
        if (tag instanceof Integer) {
            int location = (Integer) tag;
            if (location >= daies.size())
                return;
            Day day = daies.get(location);
            if (day.day != 0)
                listener.onItemClick(this, day);
        }
    }

    /**
     * 获取屏幕宽度
     */
    @SuppressWarnings("deprecation")
    private int getWindowWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }
}
