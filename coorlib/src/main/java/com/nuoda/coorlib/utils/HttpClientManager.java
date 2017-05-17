package com.nuoda.coorlib.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nuoda.coorlib.contacts.AppContacts;
import com.nuoda.coorlib.contacts.HttpContacts;
import com.nuoda.coorlib.entity.App;
import com.nuoda.coorlib.entity.HttpBean;
import com.nuoda.coorlib.entity.SlamBallUserBean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/3.
 */
public class HttpClientManager {
    private static OkHttpClient okHttpClient;
    private static final String TYPE = "application/octet-stream";
    private static LinkedList<Call> callLinkedList;
    private static int pageIndex = -1;
    private static String currentUrl="";
    private HttpClientManager() {
    }

    /**
     * 单例
     *
     * @return
     */
    private static OkHttpClient getHttpClientInstance() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.readTimeout(10, TimeUnit.SECONDS);
            builder.writeTimeout(5, TimeUnit.MINUTES);
//            builder.cache(n)
            okHttpClient = builder.build();
            if (callLinkedList == null) {
                callLinkedList = new LinkedList<>();
            }
        }
        return okHttpClient;
    }

    /**
     * 提交form表单数据
     *
     * @param httpBean 请求的bean
     */
    public static void postForm(final Context context, final HttpBean httpBean, final RequestCallBack requestCallBack) {
        if (NetStatusUtils.isConnected(context)) {
            getHttpClientInstance();
            FormBody.Builder formBody = new FormBody.Builder();
            //设置表单内容
            if (httpBean.getReqBody() != null) {
                pageIndex = -1;
                initParams(httpBean.getReqBody(), context);
                Iterator<Map.Entry<String, Object>> entryIterator = httpBean.getReqBody().entrySet().iterator();
                while (entryIterator.hasNext()) {
                    Map.Entry<String, Object> entry = entryIterator.next();
                    formBody.add(TextUtils.isEmpty(entry.getKey()) ? "key" : entry.getKey(), TextUtils.isEmpty(entry.getValue().toString()) ? "  " : entry.getValue().toString());
                    if (entry.getKey().toString().equals("pageIndex")) {
                        pageIndex = Integer.valueOf(entry.getValue().toString());
                    }
                }

            }
            LOG.e("------------------------------> " + httpBean.getReqBody().toString());
            Request.Builder builder = new Request.Builder();
            builder.post(formBody.build()).url(httpBean.getUrl() + "/" + httpBean.getReqBody().get("action"));
            //设置请求头内容
            if (httpBean.getHeader() != null) {
                Iterator<Map.Entry<String, Object>> entryIterator = httpBean.getHeader().entrySet().iterator();
                while (entryIterator.hasNext()) {
                    Map.Entry<String, Object> entry = entryIterator.next();
                    builder.addHeader(TextUtils.isEmpty(entry.getKey()) ? "key" : entry.getKey(), TextUtils.isEmpty(entry.getValue().toString()) ? "  " : entry.getValue().toString());
                }

            }
            //设置请求的tag
            builder.tag(httpBean.getTagName());
            //发送请求
            Call call = okHttpClient.newCall(builder.build());
            callLinkedList.add(call);
            if (!call.isCanceled()) {
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LOG.e("------------------------------>onFailure" + "  " + e.toString());
                        HttpBean.HttpResposeBean httpResposeBeanIns = httpBean.getHttpResposeBeanIns();
                        httpResposeBeanIns.setErrorNum("0");
                        httpResposeBeanIns.setMsg("请求失败");
                        refreshUI(httpResposeBeanIns, context, requestCallBack, false);
                        sendLoadMsg(context, "服务器累晕了!\n点击重试", AppContacts.LOAD_ERROR_SERVICE, httpBean.getTagName());
                        callLinkedList.remove(call);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        resloveOnResponse(call, response, httpBean, context, requestCallBack);
                    }
                });
            }
        } else {
            HttpBean.HttpResposeBean httpResposeBeanIns = httpBean.getHttpResposeBeanIns();
            httpResposeBeanIns.setErrorNum("0");
            httpResposeBeanIns.setMsg("您的网络未设置好");
            refreshUI(httpResposeBeanIns, context, requestCallBack, false);
            sendLoadMsg(context, "网络未设置好，请设置网络", AppContacts.LOAD_ERROR_NET, httpBean.getTagName());
        }
    }

    /**
     * 处理返回的结果
     *
     * @param call
     * @param response
     * @param httpBean
     * @param context
     * @param requestCallBack
     * @throws IOException
     */
    private static void resloveOnResponse(Call call, Response response, HttpBean httpBean, Context context, RequestCallBack requestCallBack) throws IOException {
        final String string = response.body().string();
        LOG.e("------------------------------>onSuccess" + "  " + string);
        if (response.isSuccessful()) {
            if (!TextUtils.isEmpty(string)) {
                try {
                    JSONObject jsonObject = JSON.parseObject(string);
                    HttpBean.HttpResposeBean httpResposeBean = httpBean.getHttpResposeBeanIns();
                    for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                        if (entry.getKey().equals("errorNum")) {
                            httpResposeBean.setErrorNum(entry.getValue().toString());
                        } else if (entry.getKey().equals("msg")) {
                            httpResposeBean.setMsg(entry.getValue().toString());
                        } else if (entry.getKey().equals("data")) {
                            httpResposeBean.setData(entry.getValue().toString());
                        }
                    }
                    httpResposeBean.setRaw(string);
                    handlerResponseMsg(httpResposeBean, httpBean, context, requestCallBack, pageIndex, true, call);
                } catch (Exception e) {
                    HttpBean.HttpResposeBean httpResposeBeanIns = httpBean.getHttpResposeBeanIns();
                    httpResposeBeanIns.setErrorNum("0");
                    httpResposeBeanIns.setMsg("请求失败");
                    sendLoadMsg(context, "服务器累晕了!\n点击重试", AppContacts.LOAD_ERROR_SERVICE, httpBean.getTagName());
                    refreshUI(httpResposeBeanIns, context, requestCallBack, false);
                    e.printStackTrace();
                    callLinkedList.remove(call);
                }
            }

        } else {
            LOG.e("------------------------------>onServiceError  " + response.code() + string + "  ");
            HttpBean.HttpResposeBean httpResposeBeanIns = httpBean.getHttpResposeBeanIns();
            httpResposeBeanIns.setErrorNum("0");
            httpResposeBeanIns.setMsg("请求失败");
            sendLoadMsg(context, "服务器累晕了!\n点击重试", AppContacts.LOAD_ERROR_SERVICE, httpBean.getTagName());
            refreshUI(httpResposeBeanIns, context, requestCallBack, false);
            callLinkedList.remove(call);
        }
    }

    /**
     * 提交form表单数据
     * 隐藏没有数据时会弹出没有数据的提示
     *
     * @param httpBean 请求的bean
     */
    public static void postFormNoData(final Context context, final HttpBean httpBean, final RequestCallBack requestCallBack) {
        if (NetStatusUtils.isConnected(context)) {
            getHttpClientInstance();
            FormBody.Builder formBody = new FormBody.Builder();
            //设置表单内容
            if (httpBean.getReqBody() != null) {
                pageIndex = -1;
                initParams(httpBean.getReqBody(), context);
                Iterator<Map.Entry<String, Object>> entryIterator = httpBean.getReqBody().entrySet().iterator();
                while (entryIterator.hasNext()) {
                    Map.Entry<String, Object> entry = entryIterator.next();
                    formBody.add(TextUtils.isEmpty(entry.getKey()) ? "key" : entry.getKey(), TextUtils.isEmpty(entry.getValue().toString()) ? "  " : entry.getValue().toString());
                    if (entry.getKey().toString().equals("pageIndex")) {
                        pageIndex = Integer.valueOf(entry.getValue().toString());
                    }
                }

            }
            LOG.e("------------------------------> " + httpBean.getReqBody().toString());
            Request.Builder builder = new Request.Builder();
            builder.post(formBody.build()).url(httpBean.getUrl() + "/" + httpBean.getReqBody().get("action"));
            //设置请求头内容
            if (httpBean.getHeader() != null) {
                Iterator<Map.Entry<String, Object>> entryIterator = httpBean.getHeader().entrySet().iterator();
                while (entryIterator.hasNext()) {
                    Map.Entry<String, Object> entry = entryIterator.next();
                    builder.addHeader(TextUtils.isEmpty(entry.getKey()) ? "key" : entry.getKey(), TextUtils.isEmpty(entry.getValue().toString()) ? "  " : entry.getValue().toString());
                }

            }
            //设置请求的tag
            builder.tag(httpBean.getTagName());
            //发送请求
            Call call = okHttpClient.newCall(builder.build());
            callLinkedList.add(call);
            if (!call.isCanceled()) {
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LOG.e("------------------------------>onFailure" + "  " + e.toString());
                        HttpBean.HttpResposeBean httpResposeBeanIns = httpBean.getHttpResposeBeanIns();
                        httpResposeBeanIns.setErrorNum("0");
                        httpResposeBeanIns.setMsg("请求失败");
                        refreshUI(httpResposeBeanIns, context, requestCallBack, false);
                        sendLoadMsg(context, "服务器累晕了!\n点击重试", AppContacts.LOAD_ERROR_SERVICE, httpBean.getTagName());
                        callLinkedList.remove(call);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        resloveOnResponse(call, response, httpBean, context, requestCallBack);
                    }
                });
            }
        } else {
            HttpBean.HttpResposeBean httpResposeBeanIns = httpBean.getHttpResposeBeanIns();
            httpResposeBeanIns.setErrorNum("0");
            httpResposeBeanIns.setMsg("您的网络未设置好");
//            refreshUI(httpResposeBeanIns, context, requestCallBack, false);
//            sendLoadMsg(context, "网络未设置好，请设置网络", AppContacts.LOAD_ERROR_NET);
        }
    }

    /**
     * 处理请求成功后返回的数据
     *
     * @param
     * @param httpResposeBean
     * @param httpBean
     * @param context
     * @param requestCallBack
     * @param pageIndex
     * @param call
     */
    private static void handlerResponseMsg(final HttpBean.HttpResposeBean httpResposeBean, HttpBean httpBean, final Context context, final RequestCallBack requestCallBack, int pageIndex, boolean isShowNoData, Call call) {
        //请求成功
        if (httpResposeBean.getErrorNum().equals("1")) {
            //解析成bean
            if (!TextUtils.isEmpty(httpResposeBean.getData())) {
                if (httpBean.getResDataType() == HttpBean.RES_DATATYPE_BEAN) {
                    List list = JSON.parseArray(httpResposeBean.getData(), httpBean.getaClass());
                    if (list.size() > 0) {
                        httpResposeBean.setResBean(list.get(0));
                    } else {
                        if (isShowNoData) {
                            sendLoadMsg(context, "没有数据哦！点击重试", AppContacts.LOAD_ERROR_NODATA, httpBean.getTagName());
                        }
                    }

                } else if (httpBean.getResDataType() == HttpBean.RES_DATATYPE_BEANLIST) {
                    httpResposeBean.setResListData(JSON.parseArray(httpResposeBean.getData(), httpBean.getaClass()));
                    if (httpResposeBean.getResListData().size() <= 0 && pageIndex <= 1) {
                        if (isShowNoData) {
                            sendLoadMsg(context, "没有数据哦！点击重试", AppContacts.LOAD_ERROR_NODATA, httpBean.getTagName());
                        }
                    } else {
                        sendLoadMsg(context, "请求成功", AppContacts.LOAD_SUCCESS, httpBean.getTagName());
                    }
                }
            }

            refreshUI(httpResposeBean, context, requestCallBack, true);
            callLinkedList.remove(call);
        } else if (httpResposeBean.getMsg().equals("验证失败") && httpResposeBean.getErrorNum().equals("0")) {
            retryLogin(context, call, requestCallBack,httpBean);
        } else {
            callLinkedList.remove(call);
            sendLoadMsg(context, "服务器累晕了!点击重试", AppContacts.LOAD_ERROR_SERVICE, httpBean.getTagName());
            refreshUI(httpResposeBean, context, requestCallBack, false);
        }
    }

    /**
     * 更新UI数据
     *
     * @param httpResposeBean
     * @param context
     * @param requestCallBack
     * @param isSuccess
     */
    private static void refreshUI(final HttpBean.HttpResposeBean httpResposeBean, final Context context, final RequestCallBack requestCallBack, final boolean isSuccess) {
        if (context instanceof Application) {
            if (isSuccess) {
                requestCallBack.onSuccess(httpResposeBean);
            } else {
                requestCallBack.onError(httpResposeBean);
            }
        } else {
            ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isSuccess) {
                        requestCallBack.onSuccess(httpResposeBean);
                    } else {
                        requestCallBack.onError(httpResposeBean);
                    }
                }
            });
        }
    }

    /**
     * 上传文件 注意：该方法为上传文件管理类的方法
     *
     * @param file     要上传的文件
     * @param params   上传文件是附带的表单参数
     * @param callback 回掉
     */
    public static void uploadFile(File file, Map<String, Object> params, Callback callback) {

        if (!file.exists())
            return;
        initParams(params, App.getContext());

        getHttpClientInstance();
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        //设置表单内容
        if (params != null) {
            Iterator<Map.Entry<String, Object>> entryIterator = params.entrySet().iterator();
            while (entryIterator.hasNext()) {
                Map.Entry<String, Object> entry = entryIterator.next();
                requestBody.addFormDataPart(TextUtils.isEmpty(entry.getKey()) ? "key" : entry.getKey(), TextUtils.isEmpty(entry.getValue().toString()) ? "  " : entry.getValue().toString());
                LOG.e("----------------> " + entry.getKey() + "------------------>" + entry.getValue());
            }

        }
        requestBody.setType(MultipartBody.FORM);
        RequestBody fileBody = RequestBody.create(MediaType.parse(TYPE), file);
        requestBody.addFormDataPart("filename", file.getName(), fileBody);
        Request requestPostFile = new Request.Builder()
                .url(HttpContacts.UPLOAD_FILE)
                .post(requestBody.build())
                .build();
        okHttpClient.newCall(requestPostFile).enqueue(callback);


    }

    /**
     * 网络请求的回掉
     */
    public interface RequestCallBack<T> {
        void onSuccess(HttpBean.HttpResposeBean resposeBean);

        void onError(HttpBean.HttpResposeBean resposeBean);

    }

    /**
     * 取消正在进行的请求
     *
     * @param tagName 当前请求的类的名称
     */
    public static void cancleRequset(String tagName) {
        LOG.e("--------------------------->取消请求：" + tagName);
        List<Call> callList = new ArrayList<>();
        try {
            if (callLinkedList != null) {
                for (Call call : callLinkedList) {
                    LOG.e("-------------------------->" + call.request().tag().toString());
                    if (call.request().tag().toString().equals(tagName)) {
                        if (!call.isCanceled()) {
                            call.cancel();
                        }
                        callList.add(call);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Call call : callList) {
            callLinkedList.remove(call);
        }
        callList.clear();
        callList = null;
    }

    /**
     * 发送请求信息的广播
     *
     * @param context 上下文
     * @param msg     信息
     * @param msgType 类型
     * @param tagName
     */

    private static void sendLoadMsg(Context context, String msg, int msgType, String tagName) {
        Intent intent = new Intent(AppContacts.ACTION_LOADERROR);
        intent.putExtra(AppContacts.TAG_NAME, tagName);
        intent.putExtra(AppContacts.LOAD_TYPE, msgType);
        intent.putExtra(AppContacts.LOAD_MSG, msg);
        context.sendBroadcast(intent);
    }

    /**
     * 初始化请求参数
     *
     * @param map
     */
    public static void initParams(Map<String, Object> map, Context context) {
        String date = CommonUtils.getDate();
        String token = AppContacts.TOKEN;
        String accountId = null;
        if (TextUtils.isEmpty(accountId)) {
            if (map.get("userAccount") != null)
                accountId = map.get("userAccount").toString();
        }
        if (TextUtils.isEmpty(accountId)) {
            if (map.get("mobileNo") != null)
                accountId = map.get("mobileNo").toString();
        }
        if (AppContacts.SLAMBALL_USER != null && accountId == null) {
            accountId = AppContacts.SLAMBALL_USER.getUserAccount();
        }
        if (context != null) {
            if (TextUtils.isEmpty(token)) {
                token = (String) SharedPreferencesUtils.get(context, AppContacts.KEY_TOKEN, "");
            }
            if (TextUtils.isEmpty(accountId)) {
                accountId = (String) SharedPreferencesUtils.get(context, AppContacts.KEY_ACCOUNT_SLAM, "");
            }
            map.put("signature", TokenUtils.getSignature(accountId, date, token));
            map.put("timeStamp", date + "");
            if (map.get("userAccount") == null) {
                map.put("userAccount", accountId);
            }
        }
    }

    /**
     * 提交form表单数据 无需刷新ui
     *
     * @param httpBean 请求的bean
     */
    public static void postFormNoRefrsh(final Context context, final HttpBean httpBean, final RequestCallBack requestCallBack) {
        if (NetStatusUtils.isConnected(context)) {
            getHttpClientInstance();
            FormBody.Builder formBody = new FormBody.Builder();
            //设置表单内容
            if (httpBean.getReqBody() != null) {
                initParams(httpBean.getReqBody(), context);
                Iterator<Map.Entry<String, Object>> entryIterator = httpBean.getReqBody().entrySet().iterator();
                while (entryIterator.hasNext()) {
                    Map.Entry<String, Object> entry = entryIterator.next();
                    formBody.add(TextUtils.isEmpty(entry.getKey()) ? "key" : entry.getKey(), TextUtils.isEmpty(entry.getValue().toString()) ? "  " : entry.getValue().toString());
                }

            }
            LOG.e("------------------------------> " + httpBean.getReqBody().toString());
            Request.Builder builder = new Request.Builder();
            builder.post(formBody.build()).url(httpBean.getUrl());
            //设置请求头内容
            if (httpBean.getHeader() != null) {
                Iterator<Map.Entry<String, Object>> entryIterator = httpBean.getHeader().entrySet().iterator();
                while (entryIterator.hasNext()) {
                    Map.Entry<String, Object> entry = entryIterator.next();
                    builder.addHeader(TextUtils.isEmpty(entry.getKey()) ? "key" : entry.getKey(), TextUtils.isEmpty(entry.getValue().toString()) ? "  " : entry.getValue().toString());
                }

            }
            //设置请求的tag
            builder.tag(httpBean.getTagName());
            //发送请求
            Call call = okHttpClient.newCall(builder.build());
            callLinkedList.add(call);
            if (!call.isCanceled()) {
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LOG.e("------------------------------>onFailure" + "  " + e.toString());
                        HttpBean.HttpResposeBean httpResposeBeanIns = httpBean.getHttpResposeBeanIns();
                        httpResposeBeanIns.setErrorNum("0");
                        httpResposeBeanIns.setMsg("请求失败");
                        refreshUI(httpResposeBeanIns, context, requestCallBack, false);
                        callLinkedList.remove(call);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        callLinkedList.remove(call);
                        final String string = response.body().string();
                        if (response.isSuccessful()) {
                            if (!TextUtils.isEmpty(string)) {
                                try {
                                    LOG.e("------------------------------>isSuccessful" + string);
                                    JSONObject jsonObject = JSON.parseObject(string);
                                    HttpBean.HttpResposeBean httpResposeBean = httpBean.getHttpResposeBeanIns();
                                    for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                                        if (entry.getKey().equals("errorNum")) {
                                            httpResposeBean.setErrorNum(entry.getValue().toString());
                                        } else if (entry.getKey().equals("msg")) {
                                            httpResposeBean.setMsg(entry.getValue().toString());
                                        } else if (entry.getKey().equals("data")) {
                                            httpResposeBean.setData(entry.getValue().toString());
                                        }
                                    }
                                    httpResposeBean.setRaw(string);
                                    handlerResponseMsg(httpResposeBean, httpBean, context, requestCallBack, pageIndex, false, call);
                                } catch (Exception e) {
                                    HttpBean.HttpResposeBean httpResposeBeanIns = httpBean.getHttpResposeBeanIns();
                                    httpResposeBeanIns.setErrorNum("0");
                                    httpResposeBeanIns.setMsg("请求失败");
                                    refreshUI(httpResposeBeanIns, context, requestCallBack, false);
                                    e.printStackTrace();
                                }
                            }

                        } else {
                            LOG.e("------------------------------>onServiceError  " + response.code() + string + "  ");
                            HttpBean.HttpResposeBean httpResposeBeanIns = httpBean.getHttpResposeBeanIns();
                            httpResposeBeanIns.setErrorNum("0");
                            httpResposeBeanIns.setMsg("请求失败");
                            refreshUI(httpResposeBeanIns, context, requestCallBack, false);
                        }
                    }
                });
            }
        } else {
            HttpBean.HttpResposeBean httpResposeBeanIns = httpBean.getHttpResposeBeanIns();
            httpResposeBeanIns.setErrorNum("0");
            httpResposeBeanIns.setMsg("您的网络未设置好");
            refreshUI(httpResposeBeanIns, context, requestCallBack, false);
            sendLoadMsg(context, "网络未设置好，请设置网络", AppContacts.LOAD_ERROR, httpBean.getTagName());
        }
    }

    /**
     * 验证失败重新尝试登录
     *
     * @param call
     */
    private static void retryLogin(final Context context, final Call call, final RequestCallBack requestCallBack, final HttpBean requestHttpBean) {
        boolean isLogin = (boolean) SharedPreferencesUtils.get(context, AppContacts.KEY_IS_LOGIN, false);
        AppContacts.TOKEN = TokenUtils.getToken(CommonUtils.getDate(), context);
        if (isLogin) {
            final String userAccount = (String) SharedPreferencesUtils.get(context, AppContacts.KEY_ACCOUNT_SLAM, "");
            String pwd = (String) SharedPreferencesUtils.get(context, AppContacts.KEY_PWD, "");
            if (!TextUtils.isEmpty(userAccount) && !TextUtils.isEmpty(pwd)) {
                Log.e("HttpClientManager", "HttpClientManager--------" +AppContacts.KEY_LOCATION);
                final HttpBean httpBean = new HttpBean.Builder<SlamBallUserBean>()
                        .addReqBody("userAccount", userAccount)
                        .addReqBody("userPassword", pwd)
                        .addReqBody("lastLoginPhone", "0")
                        .addReqBody("token", AppContacts.TOKEN)
                        .addReqBody("lastLoginAddress",AppContacts.KEY_LOCATION)
                        .setaClass(SlamBallUserBean.class)
                        .setTagName("retryLogin")
                        .setResDataType(HttpBean.RES_DATATYPE_BEAN)
                        .setUrl(HttpContacts.LOGIN)
                        .build();
                HttpClientManager.postForm(context, httpBean, new HttpClientManager.RequestCallBack() {

                    @Override
                    public void onSuccess(HttpBean.HttpResposeBean resposeBean) {
                        SlamBallUserBean resBean = (SlamBallUserBean) resposeBean.getResBean();
                        resBean.writeObj();
                        AppContacts.SLAMBALL_USER = resBean;
                        SharedPreferencesUtils.put(context, AppContacts.KEY_TOKEN, AppContacts.TOKEN);
                        SharedPreferencesUtils.put(context, AppContacts.KEY_ACCOUNT_SLAM, resBean.getUserAccount());
                        SharedPreferencesUtils.put(context, AppContacts.KEY_ACCOUNT, resBean.getMobileNo());
                        SharedPreferencesUtils.put(context, AppContacts.KEY_IS_LOGIN, true);
                        callLinkedList.remove(call);

                            if (!currentUrl.equals(call.request().url().toString())) {
                                okHttpClient.newCall(call.request()).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        LOG.e("------------------------------>onFailure" + "  " + e.toString());
                                        HttpBean.HttpResposeBean httpResposeBeanIns = httpBean.getHttpResposeBeanIns();
                                        httpResposeBeanIns.setErrorNum("0");
                                        httpResposeBeanIns.setMsg("请求失败");
                                        refreshUI(httpResposeBeanIns, context, requestCallBack, false);
                                        sendLoadMsg(context, "服务器累晕了!\n点击重试", AppContacts.LOAD_ERROR_SERVICE, httpBean.getTagName());
                                        currentUrl = call.request().url().toString();
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        currentUrl = call.request().url().toString();
                                        resloveOnResponse(call, response, requestHttpBean, context, requestCallBack);
                                    }
                                });
                            }

                    }

                    @Override
                    public void onError(HttpBean.HttpResposeBean resposeBean) {
                       sendLoadMsg(context,"用户验证失败,请重新登录",AppContacts.LOAD_ERROR_CHECKFALSE,requestHttpBean.getTagName());
                    }
                });
            } else {
                // TODO: 2016/12/6  跳转登陆界面
                sendLoadMsg(context,"用户验证失败,请重新登录",AppContacts.LOAD_ERROR_CHECKFALSE,requestHttpBean.getTagName());
            }

        } else {
            // TODO: 2016/12/6  跳转登陆界面
            sendLoadMsg(context,"用户验证失败,请重新登录",AppContacts.LOAD_ERROR_CHECKFALSE,requestHttpBean.getTagName());
        }
    }

}
