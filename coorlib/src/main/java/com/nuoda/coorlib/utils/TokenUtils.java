package com.nuoda.coorlib.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by Administrator on 2015/11/9.
 */
public class TokenUtils {
    public static String getSignature(String userName, String date, String token) {
        token += date;
        token += userName;
        LOG.e("getSignature", token);
        return MD5Utils.encodeMD5(token).toUpperCase();
    }

    public static String getToken(String date,Context context) {
        String mac = getMac(context);
        mac += date;
        return MD5Utils.encodeMD5(mac);
    }

    private static String getMac(Context context) {
//        String macSerial = null;
//        String str = "";
//        try {
//            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
//            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
//            LineNumberReader input = new LineNumberReader(ir);
//
//            for (; null != str; ) {
//                str = input.readLine();
//                if (str != null) {
//                    macSerial = str.trim();// 去空格
//                    break;
//                }
//            }
//        } catch (IOException ex) {
//            // 赋予默认值
//            ex.printStackTrace();
//        }
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimSerialNumber();
    }
}
