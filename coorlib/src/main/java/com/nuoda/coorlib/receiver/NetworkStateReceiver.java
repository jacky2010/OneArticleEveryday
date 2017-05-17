package com.nuoda.coorlib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nuoda.coorlib.contacts.AppContacts;
import com.nuoda.coorlib.utils.NetStatusUtils;

/**
 * Created by Administrator on 2016/8/9.
 */
public class NetworkStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!NetStatusUtils.isConnected(context)) {
            sendLoadMsg(context, "网络未设置好，请设置网络", AppContacts.LOAD_ERROR_NET);
        } else {
            sendLoadMsg(context, "", AppContacts.LOAD_REPLAY);
        }
    }
    private static void sendLoadMsg(Context context, String msg,int msgType) {
        Intent intent = new Intent(AppContacts.ACTION_LOADERROR);
        intent.putExtra(AppContacts.LOAD_TYPE, msgType);
        intent.putExtra(AppContacts.LOAD_MSG, msg);
        context.sendBroadcast(intent);
    }
}
