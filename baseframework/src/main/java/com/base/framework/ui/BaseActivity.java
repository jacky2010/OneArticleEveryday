package com.base.framework.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.base.framework.contacts.AppContacts;
import com.base.framework.entity.App;
import com.base.framework.helper.ApplicationHelper;
import com.base.framework.utils.HttpClientManager;
import com.base.framework.utils.RequestPermissionUtils;
import com.base.framework.utils.SharedPreferencesUtils;
import com.base.framework.utils.ToastUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2016/8/9.
 */
public class BaseActivity extends AppCompatActivity {
    private boolean TOUCH_BACK;
    public Context context;
    public String tagName;
    private static LinkedList<AppCompatActivity> activities;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            TOUCH_BACK = false;
        }
    };
    List<Integer> guideRes;
    private ImageView guideImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        TOUCH_BACK = false;
        super.onCreate(savedInstanceState);
        context = this;
        ApplicationHelper.init(App.application);
        if (activities == null) {
            activities = new LinkedList<>();
        }
        tagName = this.getClass().getSimpleName();
        activities.add(this);
        guideImage = new ImageView(context);

        //改变语言
        changeAppLanguage();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        requestPermission();
    }

    private void requestPermission() {
        new RequestPermissionUtils(this).aleraPermission(Manifest.permission.READ_CONTACTS, 1);
        new RequestPermissionUtils(this).aleraPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);
        new RequestPermissionUtils(this).aleraPermission(Manifest.permission.READ_PHONE_STATE, 1);
    }

    @Override
    protected void onStart() {
        try {
            super.onStart();
            if (guideRes != null) {
                if (guideRes.size() > 0 && ((boolean) SharedPreferencesUtils.get(context, tagName + 0, true))) {
                    guideImage.setImageDrawable(getResources().getDrawable(guideRes.get(0)));
                    guideImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    ViewGroup decorView = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
                    try {
                        decorView.addView(guideImage, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    guideImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreferencesUtils.put(context, tagName + 0, false);
                            guideImage.setVisibility(View.GONE);
                        }
                    });
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    private void changeAppLanguage() {

        int sta = (int) SharedPreferencesUtils.get(context, AppContacts.KEY_LANGUAGE_TYPE, 0);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        Locale myLocale;
        switch (sta) {
            case 1:
                // 本地语言设置
                myLocale = new Locale("zh");
                conf.locale = myLocale;
                break;
            case 2:
                myLocale = new Locale("en");
                conf.locale = myLocale;
                break;
            default:
                conf.locale = Locale.getDefault();
                break;
        }

        res.updateConfiguration(conf, dm);

    }

    @Override
    protected void onPause() {
        HttpClientManager.cancleRequset(tagName);
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (activities != null) {
            if (activities.size() == 1) {
                //防止误触返回键
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (!TOUCH_BACK) {
                        TOUCH_BACK = true;
                        ToastUtils.showShort(context, "再按一下退出");
                        handler.sendEmptyMessageDelayed(1, 2000);
                    } else {
                        exitApp();
                    }
                    return false;
                }
            }
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (activities != null) {
            HttpClientManager.cancleRequset(tagName);
            activities.remove(this);
        }
    }

    /**
     * 彻底退出app
     */
    protected void exitApp() {
        finish();
        for (AppCompatActivity baseActivity : activities) {
            baseActivity.finish();
        }
        activities.clear();
        activities = null;
        ApplicationHelper.onDestory(App.application);
    }

    public void startActivity(Class aClass) {
        startActivity(new Intent(context, aClass));
    }
}
