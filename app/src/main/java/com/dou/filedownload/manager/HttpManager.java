package com.dou.filedownload.manager;

import android.content.Context;

import com.dou.filedownload.common.Constant;
import com.dou.filedownload.http.DownloadCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Data: 2019/1/7 12:55:04
 * author: yonghong.liu
 */
public class HttpManager {

    private static final HttpManager httpMgr = new HttpManager();

    private HttpManager() {
        this.okHttpClient = new OkHttpClient();
    }

    public static HttpManager getInstanse() {
        return httpMgr;
    }

    private Context context;

    private OkHttpClient okHttpClient;

    public void init(Context context) {
        this.context = context;
    }

    /**
     * 同步请求方法
     *
     * @date 2019/1/8 16:51:58
     * @author yonghong.liu
     */
    public Response syncRequest(String url) {
        Request request = new Request.Builder().url(url).build();
        try {
            return okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 同步请求方法，需要区间参数
     *
     * @date 2019/1/8 16:51:58
     * @author yonghong.liu
     */
    public Response syncRequestByRange(String url, long start, long end) {
        Request request = new Request.Builder().url(url)
                .addHeader("Range", "bytes=" + start + "-" + end)
                .build();
        try {
            return okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 异步调用
     *
     * @param url
     * @param callback
     */
    public void asyncRequest(final String url, Callback callback) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public void asyncRequest(final String url, final DownloadCallback downloadCallback) {
        final Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (downloadCallback != null) {

                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful() && okHttpClient != null) {
                    downloadCallback.fail(Constant.HttpCode.NETWORK_FAIL_CODE, "请求失败");
                } else {
                    File file = FileStorageManager.getInstance().getFileByName(url);
                    byte[] buffer = new byte[1024 * 500];
                    int length;
                    FileOutputStream fileOutput = new FileOutputStream(file);
                    InputStream inputStream = response.body().byteStream();
                    while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
                        fileOutput.write(buffer, 0, length);
                        fileOutput.flush();
                    }
                    downloadCallback.success(file);
                }
            }
        });
    }


}
