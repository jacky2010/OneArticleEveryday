package com.base.framework.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.framework.utils.HttpClientManager;

import butterknife.ButterKnife;


/**
 * Created by Administrator on 2016/8/3.
 */
public abstract class BaseNoErrorMsgFragment extends Fragment {
    protected Context context;
    protected String tagName;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tagName = this.getClass().getSimpleName();
        View layoutInflateView = getLayoutInflateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, layoutInflateView);
        initView(layoutInflateView);
        bindListener();
        initData();
        return layoutInflateView;

    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    /**
     * 绑定数据
     */
    protected abstract void initData();

    /**
     * 绑定点击事件
     */
    protected abstract void bindListener();

    /**
     * 用于findviewbyID
     *
     * @param layoutInflateView 当前加载的view
     */
    protected abstract void initView(View layoutInflateView);

    /**
     * 初始化fragment
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected abstract View getLayoutInflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onPause() {
        HttpClientManager.cancleRequset(tagName);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        HttpClientManager.cancleRequset(tagName);
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
