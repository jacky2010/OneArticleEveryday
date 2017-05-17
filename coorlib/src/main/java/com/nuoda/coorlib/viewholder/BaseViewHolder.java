package com.nuoda.coorlib.viewholder;

import android.view.View;

/**
 * Created by Administrator on 2015/12/23.
 */
public abstract class BaseViewHolder<T> {

    protected View view;
    private T data;
    public BaseViewHolder() {
        view = initView();
        view.setTag(this);
    }

    public View getView() {
        return view;
    }

    public void setData(T data) {
        this.data = data;
        onRefresh(data);
    }

    protected abstract void onRefresh(T data);
    protected  abstract View initView();
}
