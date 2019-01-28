package com.dou.filedownload.manager;

import com.dou.filedownload.common.Constant;
import com.dou.filedownload.http.DownloadCallback;

import java.io.IOException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Data: 2019/1/8 20:31:29
 * author: yonghong.liu
 */
public class DownloadManager {
    private final static int MAX_THREAD = 3;
    private static final DownloadManager mDownMgr = new DownloadManager();

    private static final ThreadPoolExecutor mThreadPool = new ThreadPoolExecutor(MAX_THREAD,
            MAX_THREAD, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new ThreadFactory() {
        private AtomicInteger mInteger = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "DownloadManager-thread." + mInteger.getAndIncrement());
            return thread;
        }
    });

    private static DownloadManager getInstance() {
        return mDownMgr;
    }

    private DownloadManager() {
    }

    ;

    public void download(final String url, final DownloadCallback callback) {
        HttpManager.getInstanse().asyncRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful() && callback != null) {
                    callback.fail(Constant.HttpCode.NETWORK_FAIL_CODE, "网络出问题了");
                    return;
                }
                long length = response.body().contentLength();
                if (length == -1) {
                    callback.fail(Constant.HttpCode.CONTENT_LENGTH_ERROR, "下载内容长度出错");
                }
                processDownload(url, length, callback);
            }
        });
    }

    private void processDownload(String url, long length, DownloadCallback callback) {
        long threadDownloadSize = length / MAX_THREAD;
        for (int i = 0; i < MAX_THREAD; i++) {
            long startSize = i * threadDownloadSize;
            long endSize = 0;
            if (endSize == MAX_THREAD - 1) {
                endSize = length - 1;
            } else {
                endSize = (i + 1) * threadDownloadSize - 1;
            }
            mThreadPool.execute(new DownloadRunnable(startSize, endSize, url, callback));
        }

    }
}
