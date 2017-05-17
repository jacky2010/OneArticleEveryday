package com.nuoda.coorlib.entity;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/3.
 */
public class HttpBean<T> {
    private String reqMethod;//请求方式
    private String url;//请求的路径
    private String tagName;//请求的tag
    private Class aClass;//生成的数据类型
    private int resDataType;//返回数据的类型
    private Map<String, String> header;//请求头
    private Map<String, String> reqBody;//请求体
    public static final int RES_DATATYPE_BEAN = 0xcf;//请求返回的是对象类型
    public static final int RES_DATATYPE_BEANLIST = 0xff;//请求返回的是数组

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public Map<String, String> getReqBody() {
        return reqBody;
    }

    public void setReqBody(Map<String, String> reqBody) {
        this.reqBody = reqBody;
    }

    public String getReqMethod() {
        return reqMethod;
    }

    public void setReqMethod(String reqMethod) {
        this.reqMethod = reqMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public int getResDataType() {
        return resDataType;
    }

    public void setResDataType(int resDataType) {
        this.resDataType = resDataType;
    }

    public static int getResDatatypeBean() {
        return RES_DATATYPE_BEAN;
    }

    public static int getResDatatypeBeanlist() {
        return RES_DATATYPE_BEANLIST;
    }

    public static class Builder<E> {

        private String url;//请求的路径
        private Class aClass;//生成的数据类型
        private int resDataType;//返回数据的类型
        private Map<String, String> header;//请求头
        private Map<String, String> reqBody;//请求体
        private String reqMethod;//请求方式
        private String tagName;

        public Builder() {
            header = new Hashtable<>();
            reqBody = new Hashtable<>();
        }

        public Builder setTagName(String tagName) {
            this.tagName = tagName;
            return this;
        }

        public Builder addHeader(String key, Object object) {
            this.header.put(key, object.toString());
            return this;
        }

        public Builder addReqBody(String key, Object object) {
            if (object != null) {
                this.reqBody.put(key, object.toString());
            }
            return this;

        }

        public Builder setReqMethod(String reqMethod) {
            this.reqMethod = reqMethod;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setaClass(Class aClass) {
            this.aClass = aClass;
            return this;
        }

        public Builder setResDataType(int resDataType) {
            this.resDataType = resDataType;
            return this;
        }

        public HttpBean build() {
            HttpBean<E> eHttpRequestBean = new HttpBean<>();
            eHttpRequestBean.url = this.url;
            eHttpRequestBean.aClass = this.aClass;
            eHttpRequestBean.resDataType = this.resDataType;
            eHttpRequestBean.header = this.header;
            eHttpRequestBean.reqBody = this.reqBody;
            eHttpRequestBean.reqMethod = this.reqMethod;
            eHttpRequestBean.tagName = this.tagName;
            return eHttpRequestBean;
        }
    }

    public class HttpResposeBean {
        private String errorNum;
        private String msg;
        private String data;
        private String raw;

        public HttpResposeBean() {
        }


        private List<T> resListData;//返回的数据的list
        private T resBean;//要返回的数据bean对象

        public List<T> getResListData() {
            return resListData;
        }

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        public void setResListData(List<T> resListData) {
            this.resListData = resListData;
        }

        public T getResBean() {
            return resBean;
        }

        public void setResBean(T resBean) {
            this.resBean = resBean;
        }

        public String getErrorNum() {
            return errorNum;
        }

        public void setErrorNum(String errorNum) {
            this.errorNum = errorNum;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    /**
     * 新建一个httpresposebean实例
     * @return
     */
    public HttpResposeBean getHttpResposeBeanIns() {
        return new HttpResposeBean();
    }

}
