package com.nuoda.coorlib.entity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieSyncManager;
import android.widget.FrameLayout;

import com.nuoda.coorlib.utils.LOG;

import java.util.ArrayList;

import io.dcloud.application.DCloudApplication;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.IOnCreateSplashView;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.b.b;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.feature.internal.sdk.SDK;

/**
 * Created by Administrator on 2016/9/6.
 */
public class EntryProxy {
    private ArrayList<Activity> d = new ArrayList(1);
    public static boolean a = false;
    private static EntryProxy e = null;
    boolean b = false;
    b c = null;

    public EntryProxy() {
    }

    public static EntryProxy init(Activity var0, ICore.ICoreStatusListener var1) {
//        ArrayList<Integer> arrayList = new ArrayList<>();
//        if (e != null && e.d != null) {
//            for (int i = 0; i < e.d.size(); i++) {
//                if (e.d.get(i).getClass().getSimpleName().equals("MainActivity")) {
//                    arrayList.add(i);
//                }
//            }
//            for (int i : arrayList) {
//                e.d.remove(i);
//            }
//        }
        a = true;
        Context var2 = var0.getApplicationContext();
        DCloudApplication.setInstance(var2);
        AndroidResources.initAndroidResources(var2);
        if (e != null && e.c.b() != var2) {
            e.destroy(var0);
        }

        if (e == null) {
            e = new EntryProxy();
            CookieSyncManager.createInstance(var2);
            e.c = new b(var2, var1);
        }

        e.d.add(var0);
//        arrayList.clear();
//        arrayList = null;
        return e;
    }


    public ArrayList<Activity> getD() {
        return d;
    }

    public static EntryProxy init(Activity var0) {
        return init(var0, (ICore.ICoreStatusListener) null);
    }

    public static EntryProxy getInstnace() {
        return e;
    }

    public boolean didCreate() {
        return this.b;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public boolean onCreate(Bundle var1, FrameLayout var2, SDK.IntegratedMode var3, IOnCreateSplashView var4) {
        return this.onCreate(var1, var3, var4);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public boolean onCreate(Bundle var1, SDK.IntegratedMode var2, IOnCreateSplashView var3) {
        return this.onCreate((Activity) this.d.get(this.d.size() - 1), var1, var2, var3);
    }

    public boolean onCreate(Activity var1, Bundle var2, SDK.IntegratedMode var3, IOnCreateSplashView var4) {
        this.c.a(var1, var2, var3, var4);
        LOG.e("qqqqqqqqqqqqqqqqqqqqqqqqq", "------------------------------------>" + this.d.size());
        return true;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public boolean onCreate(Bundle var1) {
        return this.onCreate((Bundle) var1, (FrameLayout) null, (SDK.IntegratedMode) null, (IOnCreateSplashView) null);
    }

    public boolean onActivityExecute(Activity var1, ISysEventListener.SysEventType var2, Object var3) {
        return this.c != null ? this.c.a(var1, var2, var3) : false;
    }

    public void onPause(Activity var1) {
        if (this.c != null) {
            this.c.b(var1);
        }

        CookieSyncManager.getInstance().stopSync();
    }

    public void onResume(Activity var1) {
        LOG.e("entryproxy", "------------------->onResume");
        if (this.c != null) {
            this.c.c(var1);
        }

        CookieSyncManager.getInstance().startSync();
    }

    public void onNewIntent(Activity var1, Intent var2) {
        if (this.c != null) {
            this.c.a(var1, var2);
        }

    }

    public void onConfigurationChanged(Activity var1, int var2) {
        if (this.c != null) {
            this.c.a(var1, var2);
        }

    }

    public void onStop(Activity var1) {
        try {
            this.d.remove(var1);
            a = false;
            this.b = false;
//            AndroidResources.clearData();
//            BaseInfo.clearData();
//            this.d.remove(this.d.size() - 1);
            if (this.d.size() == 0) {
                if (this.c != null) {
                    if (this.c.a(var1)) {
                        this.clearData();
                    }
                } else {
                    this.clearData();
                }
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public void clearData() {
        Log.d("EntryProxy", " clearData");
        e = null;
        a = false;
        this.b = false;
        AndroidResources.clearData();
        BaseInfo.clearData();
        this.c = null;
    }

    public void destroy(Activity var1) {
        this.onStop(var1);
    }

    public ICore getCoreHandler() {
        return this.c.a();
    }
}
