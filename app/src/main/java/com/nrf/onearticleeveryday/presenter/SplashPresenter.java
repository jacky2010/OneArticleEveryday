package com.nrf.onearticleeveryday.presenter;

import com.base.framework.interfaces.BasePresenter;
import com.nrf.onearticleeveryday.data.source.respository.SplashRespository;

/**
 * Project: OneArticleEveryday
 * Author:  NRF
 * Version:  1.0
 * Date:    2017/5/22
 * Modify:  //TODO
 * Description: //TODO
 * Copyright notice:
 */
public class SplashPresenter implements BasePresenter{
    SplashRespository splashRespository;

    public SplashPresenter(SplashRespository splashRespository) {
        this.splashRespository = splashRespository;
    }

    @Override
    public void start() {

    }

    @Override
    public void cancleAllRequest(String tagName) {

    }

    @Override
    public void getArticleInfo() {
        splashRespository.getArticleInfo();
    }
}
