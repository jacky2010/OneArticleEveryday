package com.base.framework.entity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.base.framework.helper.ApplicationHelper;
import com.base.framework.utils.ImageUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/8/3.
 */
public class App extends MultiDexApplication {
    public static Application application;
    private static Context context;
    private static App app;

    @Override
    public void onCreate() {
        application = this;
        app = this;
        context = getApplicationContext();
        super.onCreate();
//        Foreground.init(this);
        //初始化图片加载器
        ImageUtils.init(this);
        //初始化配置
        ApplicationHelper.init(application);
    }

    public static Context getContext() {
        return context;
    }

    public static App getInstance() {
        return app;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public boolean isMainProcess() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
