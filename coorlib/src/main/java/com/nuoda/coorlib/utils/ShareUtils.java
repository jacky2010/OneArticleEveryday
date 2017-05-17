package com.nuoda.coorlib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nuoda.coorlib.R;
import com.nuoda.coorlib.contacts.WeiboContacts;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ShareUtils {
    private static final String QQ_ID = "1105586029";
    private static Tencent mTencent;
    private Context mcontext;
    private IWeiboShareAPI mWeiboShareAPI = null;

    public ShareUtils(Context mcontext) {
        this.mcontext = mcontext;
    }

    //分享qq空间
    public void ShareToQZ(String title, String summary, String imageurl, String url) {
        mTencent = Tencent.createInstance(QQ_ID, mcontext);
        final Bundle bundle = new Bundle();
        bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);
        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
        ArrayList imageUrls = new ArrayList();
        imageUrls.add(imageurl);
        bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        bundle.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                if (mTencent != null) {
                    mTencent.shareToQzone((Activity) mcontext, bundle, qZoneShareListener);
                }
            }
        });

    }


    IUiListener qZoneShareListener = new IUiListener() {

        @Override
        public void onCancel() {
            Log.e("TAG", "onCancel:test");
        }

        @Override
        public void onError(UiError e) {
            Log.e("TAG", "onError: " + e.errorMessage);
        }

        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
            Log.e("TAG", "onComplete: " + response.toString());
        }
    };


    private IWXAPI api;
    private static final String APP_ID = "wx13b745f56ccc01b8";

    private void regToWX() {
        api = WXAPIFactory.createWXAPI(mcontext, APP_ID, true);
        api.registerApp(APP_ID);
    }

    //分享到微信朋友圈
    public void ShareToWX(final int shareType, final String title, final String summary, final String imageurl, final String url) {
        String picUrl = imageurl;
        regToWX();
        if (!api.isWXAppInstalled()) {
            Toast.makeText(mcontext, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }

        ImageLoader.getInstance().loadImage(picUrl, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if (TextUtils.isEmpty(s) || bitmap == null) {
                    return;
                }
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = url;
                //标题，描述内容
                final WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = title;
                msg.description = summary;
                //图片
                Bitmap thumBmp = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
                msg.thumbData = ShareWXUtils.bmpToByteArray(thumBmp, true);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = msg;
                if (shareType==0){
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                }else {
                    req.scene=SendMessageToWX.Req.WXSceneSession;
                }

                api.sendReq(req);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    //分享到微博
    public void ShareToWeibo(String picUrl, String title, String desc, String url) {
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mcontext, WeiboContacts.APP_KEY);
        mWeiboShareAPI.registerApp();
        if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
            int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
            if (supportApi >= 10351) {
                sendMultiMessage(picUrl, title, desc, url);
            }
        } else {
            ToastUtils.showShort(mcontext, "微博客户端不支持SDK分享或微博客户端未安装或微博客户端是非官方版本。");
        }
    }

    private void sendMultiMessage(String picUrl, final String title, final String desc, final String url) {
        ImageLoader.getInstance().loadImage(picUrl, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                WeiboMessage weiboMessage = new WeiboMessage();
                WebpageObject mediaObject = new WebpageObject();
                mediaObject.identify = Utility.generateGUID();
                mediaObject.title = title;
                mediaObject.description = desc;
                Bitmap thumBmp = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
                mediaObject.setThumbImage(thumBmp);
                mediaObject.actionUrl = url;
                mediaObject.defaultText = "Webpage 默认文案";
                weiboMessage.mediaObject = mediaObject;
                // 2. 初始化从第三方到微博的消息请求
                SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
                // 用transaction唯一标识一个请求
                request.transaction = String.valueOf(System.currentTimeMillis());
                request.message = weiboMessage;
                mWeiboShareAPI.sendRequest((Activity) mcontext, request);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }
}
