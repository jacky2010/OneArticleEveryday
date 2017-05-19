package com.base.framework.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.framework.R;
import com.base.framework.contacts.AppContacts;
import com.base.framework.utils.HttpClientManager;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/3.
 */
public abstract class BaseFragment extends Fragment {
    protected Context context;
    protected String tagName;
    private View errorLayout;
    private TextView errorMsg;
    protected FrameLayout framContainer;
    private LoadErrorReceiver loadErrorReceiver;//获取失败信息的提示页面
    private boolean isFirstResume;//是不是第一次显示
    private ImageView errorImg;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tagName = this.getClass().getSimpleName();
        framContainer = new FrameLayout(context);
        View layoutInflateView = getLayoutInflateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, layoutInflateView);
        framContainer.addView(layoutInflateView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        errorLayout = inflater.inflate(R.layout.view_loaderror, null);
        errorMsg = (TextView) errorLayout.findViewById(R.id.loading_error_msg);
        errorImg = (ImageView) errorLayout.findViewById(R.id.loading_error_img);
        errorLayout.setVisibility(View.INVISIBLE);
        isFirstResume = true;
        errorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewDataReplay();
            }
        });
        framContainer.addView(errorLayout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setLoadErrorReceiver();
        initView(layoutInflateView);
        initData();
        bindListener();
        framContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return framContainer;

    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        if (!isFirstResume) {
            setLoadErrorReceiver();
        }
        isFirstResume = false;
        super.onResume();
    }

    /**
     * 设置加载错误显示的提示的监听
     */
    private void setLoadErrorReceiver() {
        loadErrorReceiver = new LoadErrorReceiver();
        IntentFilter intentFilter = new IntentFilter(AppContacts.ACTION_LOADERROR);
        context.registerReceiver(loadErrorReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        context.unregisterReceiver(loadErrorReceiver);
        HttpClientManager.cancleRequset(tagName);
        super.onPause();
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
     * 重新获取页面数据
     */
    protected abstract void getViewDataReplay();

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
    public void onDestroy() {
        HttpClientManager.cancleRequset(tagName);
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    /**
     * 加载数据是否成功的receiver
     */
    private class LoadErrorReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String loadErrorMsg = intent.getStringExtra(AppContacts.LOAD_MSG);
            int msgType = intent.getIntExtra(AppContacts.LOAD_TYPE, AppContacts.LOAD_ERROR);
            if (TextUtils.isEmpty(intent.getStringExtra(AppContacts.TAG_NAME)) ? false : intent.getStringExtra(AppContacts.TAG_NAME).equals(tagName)||msgType==AppContacts.LOAD_ERROR_NET||msgType == AppContacts.LOAD_REPLAY||msgType==AppContacts.LOAD_SUCCESS) {
                if (!TextUtils.isEmpty(loadErrorMsg)) {
                    errorMsg.setText(loadErrorMsg);
                }
                if (msgType == AppContacts.LOAD_ERROR) {
                    errorLayout.setVisibility(View.VISIBLE);
                } else if (msgType == AppContacts.LOAD_SUCCESS) {
                    errorLayout.setVisibility(View.GONE);
                } else if (msgType == AppContacts.LOAD_REPLAY) {
                    errorLayout.setVisibility(View.GONE);
                    getViewDataReplay();
                } else if (msgType == AppContacts.LOAD_ERROR_SERVICE) {
                    errorLayout.setVisibility(View.VISIBLE);
                    errorImg.setImageResource(R.mipmap.loadingweb_servererror);
                } else if (msgType == AppContacts.LOAD_ERROR_NODATA) {
                    errorLayout.setVisibility(View.VISIBLE);
                    errorImg.setImageResource(R.mipmap.loadingweb_nodata);
                } else if (msgType == AppContacts.LOAD_ERROR_NET) {
                    errorLayout.setVisibility(View.VISIBLE);
                    errorImg.setImageResource(R.mipmap.loadingweb_nonet);
                }
            }
            if (msgType == AppContacts.LOAD_ERROR_NET) {
                errorLayout.setVisibility(View.VISIBLE);
                errorImg.setImageResource(R.mipmap.loadingweb_nonet);
            }
            if (msgType == AppContacts.LOAD_REPLAY) {
                errorLayout.setVisibility(View.GONE);
                getViewDataReplay();
            }
        }
    }
}
