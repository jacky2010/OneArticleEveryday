package com.nuoda.coorlib.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.nuoda.coorlib.entity.DownloadTaskBean;
import com.nuoda.coorlib.ui.BaseActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Administrator on 2015/12/21.
 */
public class MuiltiThreadDownloadUtils {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DOWN_COMPLETE) {
                if (taskLinkedList.size() > 0) {
                    LOG.e("muiltithread", "当前下载的是" + taskLinkedList.get(0).url);
                    if (!controller.isAlive()) {
                        controller = null;
                        isPause = false;
                        isDownLoading = true;
                        controller = new Controller();
                        controller.start();
                    }

                }
            }
        }
    };
    private static LinkedList<DownloadTaskBean> taskLinkedList;


    private long progress = 0;
    private int threadCount = 1;
    private boolean isPause = false;
    private int threadFinish = 0;
    private static MuiltiThreadDownloadUtils muiltiThreadDownloadUtils;
    private static Controller controller;
    private List<Observer> observers = new ArrayList();
    private static final int DOWN_COMPLETE = 1;
    private boolean isDownLoading = false;

    public static MuiltiThreadDownloadUtils getInstace() {
        if (muiltiThreadDownloadUtils == null) {

            muiltiThreadDownloadUtils = new MuiltiThreadDownloadUtils();
        }

        return muiltiThreadDownloadUtils;
    }

    private MuiltiThreadDownloadUtils() {
        if (taskLinkedList == null) {
            taskLinkedList = new LinkedList<>();
        }
    }

    public void removerObserver(Observer observer) {
        observers.remove(observer);
    }

    public void addDownloadTask(Context context, String url) {
        try {
            DownloadTaskBean downloadTaskBean = new DownloadTaskBean(context, url);
            isPause = false;
            taskLinkedList.add(downloadTaskBean);
            if (controller != null) {
                if (!controller.isAlive()) {
                    if (!isDownLoading) {
                        controller = null;
                        controller = new Controller();
                        controller.start();
                    }
                }
            } else {
                if (!isPause) {
                    controller = new Controller();
                    controller.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 带有制定存储路径的下载任务
     *
     * @param context  上下文
     * @param url      文件网络地址
     * @param savePath 存储路径
     */
    public void addDownloadTask(Context context, String url, String savePath) {
        DownloadTaskBean downloadTaskBean = new DownloadTaskBean(context, url, savePath);
        isPause = false;
        taskLinkedList.add(downloadTaskBean);
        if (controller != null) {
            if (!controller.isAlive()) {
                if (!isDownLoading) {
                    controller = null;
                    controller = new Controller();
                    controller.start();
                }
            }
        } else {
            if (!isPause) {
                controller = new Controller();
                controller.start();
            }
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

    private void notifyObservers(Object data, String url) {
        int size = 0;
        Observer[] arrays = null;

        synchronized (this) {

            size = observers.size();
            arrays = new Observer[size];
            observers.toArray(arrays);
        }
        if (arrays != null) {
            for (Observer observer : arrays) {
                observer.update(data, url);
            }

        }
    }

    private void notifyTotale(long data, String url) {
        int size = 0;
        Observer[] arrays = null;

        synchronized (this) {

            size = observers.size();
            arrays = new Observer[size];
            observers.toArray(arrays);
        }
        if (arrays != null) {
            for (Observer observer : arrays) {
                observer.notifyTotalSize(data, url);
            }

        }
    }


    private void notifyDownloadComplete(String path, URL url) {
        int size = 0;
        Observer[] arrays = null;

        synchronized (this) {

            size = observers.size();
            arrays = new Observer[size];
            observers.toArray(arrays);
        }
        if (arrays != null) {
            for (Observer observer : arrays) {
                try {
                    observer.notifyDownloadComplete(path, url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void notifySDDisavail() {
        int size = 0;
        Observer[] arrays = null;

        synchronized (this) {

            size = observers.size();
            arrays = new Observer[size];
            observers.toArray(arrays);
        }
        if (arrays != null) {
            for (Observer observer : arrays) {
                observer.notifySDDisAvail();
            }

        }
    }

    public void setPause() {
        isPause = true;
        progress = 0;
    }

    private class DownLoadThread extends Thread {
        private long startIndex;
        private long endIndex;
        private int threadId;
        private long currentIndex;
        private String strUrl;
        private Context context;
        private String savePath;

        public DownLoadThread(Context context, long startIndex, long endIndex, int threadId, String url, String savePath) {
            super();
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.threadId = threadId;
            this.strUrl = url;
            this.context = context;
            this.savePath = savePath;
            LOG.e("---------------------------------->" + startIndex + "   " + endIndex + "   " + url + "   ");
        }

        @Override
        public void run() {
            try {
                String[] split = strUrl.split("/");
                File temp = new File((TextUtils.isEmpty(savePath) ? getSdCardPath()+ "/"  : savePath + "/" )+ (split[split.length - 1].split("\\.")[0]) + ".txt");
                if (temp.exists()) {
                    FileReader reader = new FileReader(temp);
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    currentIndex = Integer.parseInt(bufferedReader.readLine());
                    progress += (currentIndex - startIndex);
                    startIndex = currentIndex;
                }

                // 为下载节点文件设置初始值；
                currentIndex = startIndex;
                // 下载相应部分的内容
                final URL url = new URL(strUrl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setReadTimeout(50 * 5000);
                conn.setConnectTimeout(50 * 5000);
                conn.setRequestMethod("GET");
                // 设置为请求部分内容,设置请求头文件内容
                conn.setRequestProperty("Range", "bytes=" + startIndex + "-"
                        + endIndex);
                if (conn.getResponseCode() == 206) {
                    final File file = new File((TextUtils.isEmpty(savePath) ? getSdCardPath() + "/" : savePath + "/") + getFileName(strUrl));
                    RandomAccessFile accessFile = new RandomAccessFile(file,
                            "rwd");
                    InputStream stream = conn.getInputStream();
                    int len = 0;
                    byte[] data = new byte[1024 * 1024];
                    int total = 0;
                    // 设定文件写入的位置
                    accessFile.seek(startIndex);
                    while ((len = stream.read(data)) != -1 && isPause == false) {
                        progress += len;
                        ((BaseActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifyObservers(progress, strUrl);
                            }
                        });
                        accessFile.write(data, 0, len);
                        currentIndex += len;
                        RandomAccessFile accessFile2 = new RandomAccessFile(
                                temp, "rwd");
                        accessFile2.write((currentIndex + "").getBytes());
                        // 关闭文件流
                        accessFile2.close();
                    }
                    // 关闭文件流
                    stream.close();
                    accessFile.close();

                    threadFinish++;

                    synchronized (strUrl) {

                        if (threadFinish == threadCount) {

                            if (!isPause) {
                                for (int i = 0; i < threadFinish; i++) {
                                    File file2 = new File((TextUtils.isEmpty(savePath) ? getSdCardPath()+ "/" : savePath + "/") + (split[split.length - 1].split("\\.")[0])
                                            + ".txt");
                                    file2.delete();
                                }
                            }
                            progress = 0;
                            threadFinish = 0;
                            isDownLoading = false;
                            ((BaseActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!isPause) {
                                        notifyDownloadComplete(file.getPath(),url);
                                    }
                                }
                            });
                            handler.sendEmptyMessage(DOWN_COMPLETE);
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private class Controller extends Thread {
        @Override
        public void run() {
            isDownLoading = true;
            if (taskLinkedList.size() > 0) {
                DownloadTaskBean downloadTaskBean = taskLinkedList.get(0);
                if (threadFinish < threadCount) {
                    try {
                        long length = 0;
                        URL url = new URL(downloadTaskBean.url);
                        HttpURLConnection conn = (HttpURLConnection) url
                                .openConnection();
                        conn.setReadTimeout(5000);
                        conn.setConnectTimeout(5000);
                        conn.setRequestMethod("GET");
                        if (conn.getResponseCode() == 200) {
                            length = conn.getContentLength();
                            long sdCardAvailBolck = SDCardUtils.getSdCardAvailBolck();
                            System.out.println("---------------->剩余空间" + Math.abs(sdCardAvailBolck));
                            //检查剩余空间是否可用
//                            if (length <= sdCardAvailBolck) {
                            File file = new File(TextUtils.isEmpty(downloadTaskBean.savePath) ? getSdCardPath()+"/"+getFileName(downloadTaskBean.url) : downloadTaskBean.savePath + "/"
                                    + getFileName(downloadTaskBean.url));
                            RandomAccessFile accessFile = new RandomAccessFile(
                                    file, "rwd");
                            accessFile.setLength(length);
                            long size = length / threadCount;
                            notifyTotale(length, downloadTaskBean.url);
                            String strUrl = taskLinkedList.get(0).url;
                            for (int j = 0; j < threadCount; j++) {
                                long startIndex = j * size;
                                long endIndex = (j + 1) * size - 1;
                                if (j == threadCount - 1) {
                                    endIndex = length;
                                }
                                DownLoadThread downLoadThread = new DownLoadThread(taskLinkedList.get(0).context,
                                        startIndex, endIndex, j, strUrl, downloadTaskBean.savePath);
                                downLoadThread.start();
                            }
//                            } else {
//                                ((AppCompatActivity) taskLinkedList.get(0).context).runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        notifySDDisavail();
//                                    }
//                                });
//                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                taskLinkedList.remove(downloadTaskBean);
            }
        }


    }

    // 获取文件名的方法
    private String getFileName(String urlStr) {
        int index = urlStr.lastIndexOf("/");
        return urlStr.substring(index + 1);
    }

    //获取sd卡的路径
    private String getSdCardPath() {
        String path = null;
        //判断sd卡的状态
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "slb" + File.separator + "download");
            if (file.exists() == false) {
                if (file.mkdirs()) {
                    path = file.getAbsolutePath();
                }
            } else {
                path = file.getAbsolutePath();
            }
        }
        return path;
    }
    /**
     *
     *
     *
     *
     *
     *
     * 添加观察者
     */
    /**
     * 观察的接口
     */
    public interface Observer {
        /**
         * 更新下载进度
         *
         * @param data
         */
        void update(Object data, String url);

        /**
         * 提示sd卡容量不足
         */
        void notifySDDisAvail();

        /**
         * 获得当前下载的文件的大小
         *
         * @param size
         * @param url
         */
        void notifyTotalSize(long size, String url);

        /**
         * 发布下载完毕通知
         */
        void notifyDownloadComplete(String filePath, URL url);
    }

    public void onDestory() {
        if (!isPause) {
            isPause = !isPause;
            taskLinkedList = null;
            observers = null;
            controller = null;
        }
    }
}
