package com.skye.lover.activity.my.userinfo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.skye.datepicker.DataPicker;
import com.skye.datepickerdata.Data;
import com.skye.datepickerdata.Day;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.model.User;
import com.skye.lover.mvp.presenter.UpdateBirthdayPresenter;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ShareDataUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 更新生日界面
 */
public class UpdateBirthdayActivity extends BaseActivity<BaseView, UpdateBirthdayPresenter> implements BaseView {
    @BindView(R.id.datepicker)
    DataPicker datapicker;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_update_birthday;
    }

    @Override
    public UpdateBirthdayPresenter createPresenter() {
        return new UpdateBirthdayPresenter();
    }

    @Override
    public int getDescription() {
        return R.string.update_birthday_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.birthday);
        topbar.left.setVisibility(View.VISIBLE);
        String birthday = ShareDataUtil.get(context, User.BIRTHDAY);
        if (!TextUtils.isEmpty(birthday)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                Date date = sdf.parse(birthday);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR), month = calendar.get(Calendar.MONTH),
                        dayOfYear = calendar.get(Calendar.DAY_OF_MONTH);
                Day day = new Day();
                day.year = year;
                day.month = month;
                day.day = dayOfYear;
                day.monthPosition = Data.getMonthPosition(year, month);
                day.dayPosition = Data.getDayPosition(year, month, dayOfYear);
                datapicker.setSelection(day);
            } catch (Exception e) {
                e.printStackTrace();
                datapicker.setDefaultSelection();
            }
        } else
            datapicker.setDefaultSelection();

        //确定
        RxView.clicks(findViewById(R.id.btn_ok)).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((v) -> {
                    Day day = datapicker.getSelectedDay();
                    if (day == null) {
                        CommonUtil.toast(context, R.string.birthday_can_not_empty);
                        return;
                    }
                    presenter.updateBirthday(day.year + "-" + fill(day.month + 1) + "-" + fill(day.day));
                });
    }

    /**
     * 一位数字补零
     */
    private String fill(int param) {
        return param >= 1 && param <= 9 ? ("0" + param) : (param + "");
    }
}
