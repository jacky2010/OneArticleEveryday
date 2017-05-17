package com.nuoda.coorlib.utils;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nuoda.coorlib.entity.App;
import com.nuoda.coorlib.entity.FileAccess;
import com.nuoda.coorlib.entity.ImageFileBean;
import com.nuoda.coorlib.entity.UploadImageBean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/1/18.
 */
public class UploadFileManager {
    private Map<String, Object> params;
    private FileAccess fileAccess;
    private List<Observer> observers = new ArrayList();
    private static LinkedList<FileAccess> uploadTaskBeens = new LinkedList<>();
    private static LinkedList<ImageFileBean> imageTask = new LinkedList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                notifyObservers(fileAccess);
                uploadfile();
            } else if (msg.what == 2) {
                if (uploadTaskBeens.size() > 0) {
                    notifyObservers(fileAccess);
                    notifyDownloadComplete(fileAccess);
                    uploadTaskBeens.remove(0);
                    fileAccess = null;
                    uploadfile();
                }

            } else if (msg.what == 3) {
                if (imageTask.size() > 0) {
                    uploadImageToService(imageTask.getFirst());
                }
            }
        }
    };
    private File file;
    private static UploadFileManager uploadFileManager;
    private JSONObject jsonObject;

    private UpLoadImgCallBack upLoadImgCallBack;

    public void setUpLoadImgCallBack(UpLoadImgCallBack upLoadImgCallBack) {
        this.upLoadImgCallBack = upLoadImgCallBack;
    }

    public static UploadFileManager getUploadFileManager() {
        if (uploadFileManager == null) {
            uploadFileManager = new UploadFileManager();
        }
        return uploadFileManager;
    }

    public void addTask(FileAccess uploadTaskBean) {
        synchronized (uploadTaskBeens) {
            uploadTaskBeens.add(uploadTaskBean);
            if (uploadTaskBeens.size() <= 1) {
                uploadfile();
            }
        }
    }

    public void setPauseFile(FileAccess pauseFile) {
        synchronized (uploadTaskBeens) {
            uploadTaskBeens.remove(pauseFile);
        }

    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    private void uploadfile() {
        try {
            if (uploadTaskBeens.size() > 0) {
                if (fileAccess == null) {
                    fileAccess = uploadTaskBeens.getFirst();
                }
                file = fileAccess.moveToNext();
                params.put("fileItem", fileAccess.getFileItem());
                params.put("totalLength", fileAccess.getFileSize() + "");
                HttpClientManager.uploadFile(file, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String result = response.body().string();
                            LOG.e("uploadfile", "--------------------------------->" + result);
                            JSONObject jsonObject = JSON.parseObject(result);
                            String flag = jsonObject.getString("errorNum");
                            if (!flag.equals("0")) {
                                final JSONArray data = jsonObject.getJSONArray("data");
                                if (data.size() > 0) {
                                    final JSONObject object = data.getJSONObject(0);
                                    fileAccess.setFileAddress(object.get("fileAddress").toString());
                                    if (fileAccess.getFileSize() > fileAccess.getReadTotoals()) {
                                        handler.sendEmptyMessageDelayed(1, 0);
                                    } else {
                                        handler.sendEmptyMessageDelayed(2, 0);
                                    }
                                    if (file != null) {
                                        file.delete();
                                    }
                                }

                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addObserver(Observer observer) {
        if (observer == null) {
            throw new NullPointerException("observer == null");
        }
        synchronized (this) {
            if (!observers.contains(observer))
                observers.add(observer);
        }
    }


    public int countObservers() {
        return observers.size();
    }

    public synchronized void deleteObserver(Observer observer) {
        observers.remove(observer);
    }

    public synchronized void deleteObservers() {
        observers.clear();
    }

    private void notifyObservers(FileAccess fileAccess) {
        int size = 0;
        Observer[] arrays = null;

        synchronized (this) {

            size = observers.size();
            arrays = new Observer[size];
            observers.toArray(arrays);
        }
        if (arrays != null) {
            for (Observer observer : arrays) {
                observer.onUpdateProcessListener(fileAccess);
            }

        }
    }


    private void notifyDownloadComplete(FileAccess fileAccess) {
        int size = 0;
        Observer[] arrays = null;

        synchronized (this) {

            size = observers.size();
            arrays = new Observer[size];
            observers.toArray(arrays);
        }
        if (arrays != null) {
            for (Observer observer : arrays) {
                observer.onUploadFinishListener(fileAccess);
            }

        }
    }


    /**
     * 观察的接口
     */
    public interface Observer {
        /**
         * 更新下载进度
         */
        void onUpdateProcessListener(FileAccess fileAccess);

        void onUploadFinishListener(FileAccess fileAccess);

        void onUploadErrorListener(Throwable ex);
    }

    /**
     * 上传图片
     */
    public void uploadImg(String imgPath, String fileItem) {
        imageTask.add(new ImageFileBean(imgPath, fileItem));
        if (imageTask.size() <= 1) {
            uploadImageToService(imageTask.getFirst());
        }
    }

    private void uploadImageToService(final ImageFileBean imageFileBean) {
        //先压缩图片
        final File imgFile = ImageCompress.getSmallImageFile(imageFileBean.getFilePath(), App.getContext());
        if (imgFile.exists()) {
            Map<String, Object> imgRequestParams = new Hashtable<>();
            imgRequestParams.put("action", "upLoadFile");
            imgRequestParams.put("isWaterMark", "false");
            imgRequestParams.put("fileItem", imageFileBean.getFileItemType());

            HttpClientManager.uploadFile(imgFile, imgRequestParams, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    upLoadImgCallBack.onError(e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String result = response.body().string();
                        try {
                            final JSONObject jsonObject = JSON.parseObject(result);
                            final List<UploadImageBean> data = JSON.parseArray(jsonObject.get("data").toString(), UploadImageBean.class);
                            if (Integer.valueOf(jsonObject.get("errorNum").toString()) > 0) {
                                //发布消息上传下一张图片
                                imageTask.removeFirst();
                                handler.sendEmptyMessage(3);
                                upLoadImgCallBack.onSuccess(data.get(0), imgFile.getAbsolutePath());
                            } else {
                                imageTask.removeFirst();
                                handler.sendEmptyMessage(3);
                                upLoadImgCallBack.onError(jsonObject.get("msg").toString());

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (imageTask.size() > 0) {
                                imageTask.removeFirst();
                            }
                            handler.sendEmptyMessage(3);
                            upLoadImgCallBack.onError("上传失败");

                        }
                    }
                }
            });
        }

    }

    public interface UpLoadImgCallBack {
        void onSuccess(UploadImageBean imageFileBean, String absolutePath);

        void onError(String s);
    }
}
