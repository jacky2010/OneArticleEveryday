package com.nuoda.coorlib.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.nuoda.coorlib.contacts.AppContacts;
import com.nuoda.coorlib.entity.App;

/**
 * Created by Administrator on 2016/11/11.
 */

public class PageMapUtils {
    /**
     * 获取跳转页面的地址
     * @param mapKey
     * @return 如果下一个页面为空则有可能为默认的页面
     */
    public static String GetPageAddress(String mapKey) {
        final SharedPreferences sharedPreferences = App.getContext().getSharedPreferences(AppContacts.PAGEMAP_SP, Context.MODE_PRIVATE);
        return sharedPreferences.getString(mapKey, "");
    }

    /**
     * 根据类名获取类
     * @param className
     * @return 如果返回是空则表示不存在该类
     */
    public static  Class   getPageClazz(String className) {
        final Class<?> aClass;
        try {
            aClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return aClass;
    }

    /**
     * 检验当前intent中的activity是否存在
     * @param intent
     * @return 如果返回 false 则表示清单文件中不存在要跳转的activity
     */
    public static boolean checkActivity(Intent intent) {
        if (intent.resolveActivity(App.getContext().getPackageManager()) == null) {
            return false;
        } else {
            return true;
        }
    }
}
