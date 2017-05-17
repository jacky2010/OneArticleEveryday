package com.nuoda.coorlib.interfaces;

/**
 * Created by Administrator on 2016/8/3.
 * 代理接口
 */
public interface BasePresenter {
    /**
     * 页面加载时调用的数据
     */
    void start();

    /**
     *取消请求
     * @param tagName
     */
    void cancleAllRequest(String tagName);
}
