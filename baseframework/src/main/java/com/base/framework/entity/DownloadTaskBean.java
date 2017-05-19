package com.base.framework.entity;

import android.content.Context;

/**
 * Created by Administrator on 2015/12/21.
 */
public class DownloadTaskBean {
   public Context context;
    public String url;
    public String savePath;
    public DownloadTaskBean(Context context, String url) {
        this.context=context;
        this.url=url;
    }

    public DownloadTaskBean(Context context, String url, String savePath) {
        this.context = context;
        this.url = url;
        this.savePath = savePath;
    }
}
