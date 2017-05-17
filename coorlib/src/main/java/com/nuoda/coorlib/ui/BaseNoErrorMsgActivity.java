package com.nuoda.coorlib.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nuoda.coorlib.R;
import com.nuoda.coorlib.contacts.AppContacts;
import com.nuoda.coorlib.utils.DensityUtils;
import com.nuoda.coorlib.utils.ToastUtils;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/3.
 */
public abstract class BaseNoErrorMsgActivity extends BaseActivity {
    private final int REQUEST_PHONE_PERMISSIONS = 0;
    private RelativeLayout container;
    private View dataHandle;
    private AnimationDrawable drawable;
    private LoadErrorReceiver loadErrorReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        container = new RelativeLayout(context);
        dataHandle = LayoutInflater.from(context).inflate(R.layout.view_data_handl, null);
        container.addView(LayoutInflater.from(context).inflate(getLayoutId(), null, false), new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        container.addView(dataHandle, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dataHandle.setVisibility(View.GONE);
        setContentView(container, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ButterKnife.bind(this);
        loadErrorReceiver = new LoadErrorReceiver();
        IntentFilter intentFilter = new IntentFilter(AppContacts.ACTION_LOADERROR);
        registerReceiver(loadErrorReceiver, intentFilter);
//        //动态获取权限
//        final List<String> permissionsList = new ArrayList<>();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if ((checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED))
//                permissionsList.add(Manifest.permission.READ_PHONE_STATE);
//            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
//                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            if (permissionsList.size() == 0) {
//                initView();
//                initData();
//                bindListener();
//            } else {
//                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
//                        REQUEST_PHONE_PERMISSIONS);
//            }
//        } else {
        initView();
        initData();
        bindListener();
//        }
    }

    /**
     * 设置布局id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始控件 做一些findviewbyId
     */
    protected abstract void initView();

    /**
     * 设置数据的显示
     */
    protected abstract void initData();

    /**
     * 设置点击事件
     */
    protected abstract void bindListener();

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        if (loadErrorReceiver != null)
            unregisterReceiver(loadErrorReceiver);
        super.onDestroy();
    }

    /**
     * 显示数据处理
     */
    public void showDataHandleDialog() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, DensityUtils.dp2px(context, 56), 0, 0);
        dataHandle.setLayoutParams(layoutParams);
        dataHandle.setVisibility(View.VISIBLE);
        ImageView dataHandl = (ImageView) dataHandle.findViewById(R.id.data_handl_img);
        dataHandl.setImageResource(R.drawable.anim_data_handl_bg);
        drawable = (AnimationDrawable) dataHandl.getDrawable();
        drawable.start();
    }

    /**
     * 显示数据处理
     */
    public void showDataHandleDialogHasTopBar() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 0, 0);
        dataHandle.setLayoutParams(layoutParams);
        dataHandle.setVisibility(View.VISIBLE);
        ImageView dataHandl = (ImageView) dataHandle.findViewById(R.id.data_handl_img);
        try {
            dataHandl.setImageResource(R.drawable.anim_data_handl_bg);
            drawable = (AnimationDrawable) dataHandl.getDrawable();
            drawable.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void hideDataHandleDialog() {
        if (drawable != null) {
            if (drawable.isRunning()) {
                drawable.stop();
            }
        }
        dataHandle.setVisibility(View.GONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        hideDataHandleDialog();
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 加载数据是否成功的receiver
     */
    private class LoadErrorReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int msgType = intent.getIntExtra(AppContacts.LOAD_TYPE, AppContacts.LOAD_ERROR);
            String loadErrorMsg = intent.getStringExtra(AppContacts.LOAD_MSG);
            if (TextUtils.isEmpty(intent.getStringExtra(AppContacts.TAG_NAME)) ? false : intent.getStringExtra(AppContacts.TAG_NAME).equals(tagName) || msgType == AppContacts.LOAD_ERROR_CHECKFALSE) {
                if (msgType == AppContacts.LOAD_ERROR_CHECKFALSE) {
                    ToastUtils.showShort(context, loadErrorMsg + "请重新登录");
                }
            }
        }
    }
}
