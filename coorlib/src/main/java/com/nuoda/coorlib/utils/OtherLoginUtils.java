package com.nuoda.coorlib.utils;

import android.app.Activity;
import android.content.Context;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/10/20.
 */

public class OtherLoginUtils {
    private Context context;

    public OtherLoginUtils(Context context) {
        this.context = context;
    }

    OnOpenId onOpenId;

    public interface OnOpenId {
        void getOpenId(String openId);
    }

    public void qqLogin(Tencent tencent, OnOpenId onOpenId) {
        if (!tencent.isSupportSSOLogin((Activity) context)) {
            ToastUtils.showShort(context, "未安装QQ客户端或版本过低！");
            return;
        }
        this.onOpenId = onOpenId;
        tencent.login((Activity) context, "all", new QQUiListener());
    }

    class QQUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            JSONObject object = (JSONObject) o;
            try {
                String openID = object.getString("openid");
                String accessToken = object.getString("access_token");
                String expires = object.getString("expires_in");
//                mTencent.setOpenId(openID);
//                mTencent.setAccessToken(accessToken, expires);
//                // qqReturn.onSuccess(openID);
//                qqOpenID = openID;
                onOpenId.getOpenId(openID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }
}
