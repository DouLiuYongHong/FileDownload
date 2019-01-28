package com.dou.filedownload.manager;

import com.dou.filedownload.common.Constant;
import com.dou.filedownload.http.DownloadCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Response;

/**
 * Data: 2019/1/8 20:43:17
 * author: yonghong.liu
 */
public class DownloadRunnable implements Runnable {

    private long mstart;
    private long mEnd;
    private String mUrl;
    private DownloadCallback mCallBack;
    private DownloadCallback m;

    public DownloadRunnable(long mstart, long mEnd, String mUrl, DownloadCallback mCallBack) {
        this.mstart = mstart;
        this.mEnd = mEnd;
        this.mUrl = mUrl;
        this.mCallBack = mCallBack;
    }

    @Override
    public void run() {
        Response response = HttpManager.getInstanse().syncRequestByRange(mUrl, mstart, mEnd);
        if (response == null && mCallBack != null) {
            mCallBack.fail(Constant.HttpCode.NETWORK_FAIL_CODE, "网络出错了");
            return;
        }
        File file = FileStorageManager.getInstance().getFileByName(mUrl);
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.seek(mstart);
            byte[] buffer = new byte[1024 * 500];
            int length;
            InputStream inputStream = response.body().byteStream();
            while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
                randomAccessFile.write(buffer, 0, length);
            }
            mCallBack.success(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
