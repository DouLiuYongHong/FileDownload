package com.dou.filedownload.http;

        import java.io.File;

/**
 * 下载回调接口
 * Data: 2019/1/7 12:42:51
 * author: yonghong.liu
 */
public interface DownloadCallback {
    void success(File file);

    void fail(int errorCode, String errorMsg);

    void progress(int progress);
}
