package com.base.framework.helper;

import android.content.Context;
import android.content.IntentFilter;

import com.base.framework.receiver.NetworkStateReceiver;
import com.base.framework.utils.LOG;


/**
 * Created by Administrator on 2016/8/9.
 */
public class ApplicationHelper {

    private static NetworkStateReceiver networkStateReceiver;

    /**
     * 初始化参数
     * @param context
     */
    public static void init(Context context) {
        initGloableReceiver(context);
    }

    /**
     * 注册全局的官博接收者
     * @param context
     */
    private static void initGloableReceiver(Context context) {
        if (networkStateReceiver == null) {
            networkStateReceiver = new NetworkStateReceiver();
            IntentFilter networkFilter=new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            context.registerReceiver(networkStateReceiver, networkFilter);
            LOG.e("----------------------------------------->注册全局监听");
        }
    }

    public  static void onDestory(Context context) {
        unRegisterGloableReceiver(context);

    }

    private static void unRegisterGloableReceiver(Context context) {
        if (networkStateReceiver != null) {
            LOG.e("----------------------------------------->取消全局监听");
            context.unregisterReceiver(networkStateReceiver);
            networkStateReceiver = null;
        }
    }
}
