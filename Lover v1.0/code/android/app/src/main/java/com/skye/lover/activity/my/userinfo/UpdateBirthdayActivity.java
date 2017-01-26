package com.skye.lover.activity.my.userinfo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.skye.datepicker.DataPicker;
import com.skye.datepickerdata.Data;
import com.skye.datepickerdata.Day;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.exception.NoNetworkConnectException;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareData;
import com.skye.lover.util.URLConfig;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UpdateBirthdayActivity extends BaseActivity {
    @BindView(R.id.datepicker)
    DataPicker datapicker;
    private String birthday;
    /**
     * 更新生日接口回调
     */
    private Callback callbackUpdateBirthday = new Callback() {
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
                            ShareData.setShareStringData(ShareData.BIRTHDAY, birthday);//保存新生日
                            CommonUtil.toast(context, R.string.update_success);
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
    public int getLayoutResourceId() {
        return R.layout.activity_update_birthday;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.birthday);
        topbar.left.setVisibility(View.VISIBLE);
        String birthday = ShareData.getShareStringData(ShareData.BIRTHDAY);
        if (!TextUtils.isEmpty(birthday)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
    }

    @OnClick(R.id.btn_ok)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                Day day = datapicker.getSelectedDay();
                if (day == null) {
                    CommonUtil.toast(context, R.string.birthday_can_not_empty);
                    return;
                }
                birthday = day.year + "-" + check(day.month + 1) + "-" + check(day.day);
                Map<String, String> params = new HashMap<>();
                params.put("userId", ShareData.getShareStringData(ShareData.ID));
                params.put("birthday", birthday);
                dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                OkHttpUtil.doPost(context, URLConfig.ACTION_UPDATE_BIRTHDAY, params, callbackUpdateBirthday);
                break;
            default:
                break;
        }
    }

    /**
     * 一位数字补零
     */
    private String check(int param) {
        if (param >= 1 && param <= 9)
            return "0" + param;
        else
            return "" + param;
    }
}
