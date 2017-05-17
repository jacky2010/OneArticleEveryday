package com.nuoda.coorlib.entity;

import android.content.Context;

import com.nuoda.coorlib.utils.LOG;
import com.nuoda.coorlib.utils.UploadFileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Administrator on 2016/1/18.
 */
public class FileAccess {
    private String filePath;
    private Context context;
    private RandomAccessFile randomAccessFile;
    private File file;
    private byte[] bytes;
    private long readTotoals;
    private File cacheFile;
    private long startPos;
    private String fileItem;
    private String fileAddress = "";
    private String fileName;
    private boolean isClick = false;
    private UploadFileManager.Observer observer;

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public String getFileName() {
        return fileName;
    }

    public UploadFileManager.Observer getObserver() {
        return observer;
    }

    public void setObserver(UploadFileManager.Observer observer) {
        this.observer = observer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileAccess that = (FileAccess) o;

        return fileAddress.equals(that.fileAddress);

    }

    @Override
    public int hashCode() {
        return fileAddress.hashCode();
    }

    public String getFilePath() {
        return filePath;
    }

    /**
     * 上传文件对象
     *
     * @param filePath 文件的路径
     * @param itemSize 上传模块的大小
     * @param startPos 上传文件的起始位置
     * @param fileItem 上传文件的类型
     */

    public FileAccess(String filePath, int itemSize, long startPos, String fileItem,Context context) {
        this.filePath = filePath;
        this.context = context;
        this.startPos = startPos;
        this.fileItem = fileItem;
        readTotoals = startPos;
        file = new File(filePath);
        this.fileName = file.getName();

        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(startPos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = new byte[itemSize];
    }

    public String getFileItem() {
        return fileItem;
    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }

    public long getStartPos() {
        return startPos;
    }

    public File moveToNext() throws FileNotFoundException {
        if (readTotoals < file.length()) {
            try {
                if (readTotoals <= 0) {
                    randomAccessFile.seek(startPos);
                } else if (getFileSize()>readTotoals){
                    randomAccessFile.seek(readTotoals);
                }
                int lon = randomAccessFile.read(bytes);
                readTotoals += lon;
                LOG.e("readtotoals","--------------------------->"+(readTotoals-1)+ "   "+readTotoals+"   "+getFileSize());
                String[] split = filePath.split("/");
                String cacheFilePath = context.getCacheDir().getAbsolutePath() + "/" + split[split.length - 1];
                cacheFile = new File(cacheFilePath);
                FileOutputStream fileOutputStream = new FileOutputStream(cacheFile);
                fileOutputStream.write(bytes,0,lon);
                fileOutputStream.flush();
                fileOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

        }
        return cacheFile;
    }

    public void setStartPos(long startPos) {
        this.startPos = startPos;
        readTotoals = startPos;
    }

    public long getReadTotoals() {
        return readTotoals;
    }

    public long getFileSize() {
        if (file != null) {
            return file.length();
        } else {
            return 0;
        }
    }
}