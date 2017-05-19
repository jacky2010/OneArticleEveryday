package com.base.framework.entity;

import android.content.Context;

/**
 * Created by Administrator on 2016/4/6.
 */
public class ImageFileBean {
    private String filePath;
    private String fileItemType;
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ImageFileBean(String filePath, String fileItemType) {
        this.filePath = filePath;
        this.fileItemType = fileItemType;
    }

    public String getFilePath() {
        return filePath;
    }



    public String getFileItemType() {
        return fileItemType;
    }

}
