package com.nuoda.coorlib.utils;

import java.security.MessageDigest;

/**
 * 用来对数据进行MD5加密的工具类
 * Created by Administrator on 2015/10/24.
 */
public class MD5Utils {
    /**
     * 进行MD5编码
     *
     * @param s 要编码的字符串
     * @return
     */
    public static String encodeMD5(String s) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(s.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] byteArray = new byte[0];
        try {
            byteArray = messageDigest.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString().toUpperCase();
    }
}
