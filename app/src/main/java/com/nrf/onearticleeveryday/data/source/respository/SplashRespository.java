package com.nrf.onearticleeveryday.data.source.respository;

import com.nrf.onearticleeveryday.data.source.SplashSource;
import com.nrf.onearticleeveryday.data.source.remote.SplashRemote;

/**
 * Project: OneArticleEveryday
 * Author:  NRF
 * Version:  1.0
 * Date:    2017/5/22
 * Modify:  //TODO
 * Description: //TODO
 * Copyright notice:
 */
public class SplashRespository implements SplashSource{
    SplashRemote splashRemote;
    public SplashRespository(SplashRemote splashRemote) {
        this.splashRemote = splashRemote;
    }

    @Override
    public void cancleReq(String tagName) {

    }

    public void getArticleInfo() {
        splashRemote.getArticleInfo();
    }
}
