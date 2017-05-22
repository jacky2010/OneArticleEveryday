package com.nrf.onearticleeveryday.activity;

import android.content.Context;

import com.base.framework.interfaces.BaseView;
import com.base.framework.ui.BaseNoErrorMsgActivity;
import com.nrf.onearticleeveryday.R;

/**
 * Project: OneArticleEveryday
 * Author:  NRF
 * Version:  1.0
 * Date:    2017/5/21
 * Modify:  //TODO
 * Description: //TODO
 * Copyright notice:
 */
public class HomeActivity extends BaseNoErrorMsgActivity implements BaseView {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindListener() {

    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public Context setReqContext() {
        return null;
    }

    @Override
    public String setTagName() {
        return null;
    }
}
