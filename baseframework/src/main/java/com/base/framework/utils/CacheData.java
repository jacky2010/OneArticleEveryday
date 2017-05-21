package com.base.framework.utils;

import com.base.framework.entity.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Project: OneArticleEveryday
 * Author:  NRF
 * Version:  1.0
 * Date:    2017/5/20
 * Modify:  //TODO
 * Description: //TODO
 * Copyright notice:
 */
public class CacheData {

    /**
     * 通过日期获取文件名称
     *
     * @param date    日期
     * @return
     */
    public static String getFileNameByDate (String date) {
        File file = new File(App.getContext().getExternalCacheDir() + File.separator + "OneArticleEveryday");
        if (!file.exists())
            file.mkdirs();
        String fileName = App.getContext().getExternalCacheDir() + File.separator + "OneArticleEveryday" + File.separator + MD5Utils.encodeMD5(date) + ".dat";
        return fileName;
    }


    /**
     * 存储bean类型缓存数据
     *
     * @param date    日期
     * @param data    对象
     * @param <T>     类
     */
    public static <T> void saveBeanCache(String date, T data) {

        String fileName = getFileNameByDate(date);
        File file = new File(fileName);
        try {
            if (!file.exists())
                file.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(data);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取缓存数据：bean类
     *
     * @param date    日期
     * @param <T>     返回类型
     * @return
     */
    public static <T> T getBeanCache(String date) {
        String fileName = getFileNameByDate(date);
        File file = new File(fileName);

        if (file.exists()) {

            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                T bean = null;
                bean = (T) ois.readObject();
                ois.close();
                return bean;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

    }

}
