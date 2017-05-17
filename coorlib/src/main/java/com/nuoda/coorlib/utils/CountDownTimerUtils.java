package com.nuoda.coorlib.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;

import com.nuoda.coorlib.R;

/**
 * Created by MIN on 16/5/9.
 * 发送验证码按钮倒计时
 */
public class CountDownTimerUtils extends CountDownTimer {
    private Button mCode;
    private Context mContext;
    public CountDownTimerUtils(long millisInFuture, long countDownInterval, Button mCode,Context context) {
        super(millisInFuture, countDownInterval);
        this.mCode = mCode;
        this.mContext=context;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mCode.setClickable(false);
        mCode.setText(millisUntilFinished / 1000 + "秒后重试");
        mCode.setTextColor(mContext.getResources().getColor(R.color.loaderror_bg));
    }

    @Override
    public void onFinish() {
        mCode.setClickable(true);
        mCode.setText("重新发送");
        mCode.setTextColor(mContext.getResources().getColor(android.R.color.black));

    }
}
