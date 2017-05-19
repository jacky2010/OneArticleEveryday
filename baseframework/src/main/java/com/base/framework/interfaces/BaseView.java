package com.base.framework.interfaces;

import android.content.Context;

/**
 * Created by Administrator on 2016/8/3.
 * 视图层基本接口
 */
public interface BaseView<T> {
    /**
     * 设置代理
     * @param presenter
     */
    void setPresenter(T presenter);

    /**
     * 获取上下文路径
     * @return
     */
    Context setReqContext();

    /**
     * 设置请求的tags
     * @return
     */
    String setTagName();
}
