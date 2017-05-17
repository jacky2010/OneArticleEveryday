package com.nuoda.coorlib.utils;

/**
 * Created by feng on 2016/8/7.
 */
public class AppHelper {
    private static AppHelper Instance;

    private AppHelper() {
    }

    public static AppHelper getInstance() {
        if (Instance == null) {
            Instance = null;
        }
        return Instance;
    }
}
