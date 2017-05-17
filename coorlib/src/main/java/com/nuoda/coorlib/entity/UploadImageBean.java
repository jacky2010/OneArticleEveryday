package com.nuoda.coorlib.entity;

/**
 * Created by Administrator on 2016/9/6.
 */
public class UploadImageBean {

    /**
     * fileName : 1473141241621.jpg
     * ImgSmallAddress : ../../UploadFiles/SlamBallUser/Img/20160906/20160906135401_5521small.jpg
     * ImgLargeAddress : ../../UploadFiles/SlamBallUser/Img/20160906/20160906135401_5521large.jpg
     */

    private String fileName;
    private String ImgSmallAddress;
    private String ImgLargeAddress;
    /**
     * receivedLength : 360261
     * fileAddress : ../UploadFiles/SlamBallZoon/File/20160909/20160909225105_5006.mp4
     */

    private String receivedLength;
    private String fileAddress;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getImgSmallAddress() {
        return ImgSmallAddress;
    }

    public void setImgSmallAddress(String ImgSmallAddress) {
        this.ImgSmallAddress = ImgSmallAddress;
    }

    public String getImgLargeAddress() {
        return ImgLargeAddress;
    }

    public void setImgLargeAddress(String ImgLargeAddress) {
        this.ImgLargeAddress = ImgLargeAddress;
    }

    public String getReceivedLength() {
        return receivedLength;
    }

    public void setReceivedLength(String receivedLength) {
        this.receivedLength = receivedLength;
    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }
}
