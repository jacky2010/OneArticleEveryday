package com.nuoda.coorlib.utils;

import com.nuoda.coorlib.entity.App;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2016/8/10.
 */
public class CommonUtils {
    /**
     * 获得日期
     *
     * @return
     */
    public static String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new java.util.Date());
    }
    /**
     * 获取制定路径下的缓存文件
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static double getFolderSize(File file) throws Exception {
        double size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
    /**
     * 根据指定路径删除文件
     *
     * @param filePath
     */
    public static void cleanCustomCache(String filePath) {
        try {
            File directory = new File(filePath);
            if (directory.exists()) {
                if (directory.isFile()) {
                    directory.delete();
                } else {
                    for (File item : directory.listFiles()) {
                        if (item.isFile()) {
                            item.delete();
                        } else {
                            cleanCustomCache(item.getAbsolutePath());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //清空缓存目录；
        File fileCache = App.getContext().getCacheDir();
        if (fileCache.exists()) {
            File[] files = fileCache.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }

    }
}
