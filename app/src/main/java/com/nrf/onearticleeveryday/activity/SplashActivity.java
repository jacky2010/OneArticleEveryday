package com.nrf.onearticleeveryday.activity;

import android.content.Context;
import android.text.TextUtils;

import com.base.framework.interfaces.BaseView;
import com.base.framework.ui.BaseNoErrorMsgActivity;
import com.base.framework.utils.CacheData;
import com.base.framework.utils.CommonUtils;
import com.nrf.onearticleeveryday.R;
import com.nrf.onearticleeveryday.data.bean.ArticleBean;
import com.nrf.onearticleeveryday.data.source.SplashSource;
import com.nrf.onearticleeveryday.data.source.remote.SplashRemote;
import com.nrf.onearticleeveryday.data.source.respository.SplashRespository;
import com.nrf.onearticleeveryday.presenter.SplashPresenter;

public class SplashActivity extends BaseNoErrorMsgActivity implements BaseView{


    private SplashPresenter splashPresenter;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        splashPresenter = new SplashPresenter(new SplashRespository(new SplashRemote()));
    }

    @Override
    protected void initData() {
        String date = CommonUtils.getDate();
       ArticleBean articleBean =  CacheData.getBeanCache(date);

        if(!TextUtils.isEmpty(articleBean.toString())){

        }else {
            splashPresenter.getArticleInfo();
        }

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
