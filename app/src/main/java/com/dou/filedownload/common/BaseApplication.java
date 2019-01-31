package com.dou.filedownload.common;

import android.app.Application;

import com.dou.filedownload.db.DownloadDbHelper;

/**
 * Data: 2019/1/31 10:09:23
 * author: yonghong.liu
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DownloadDbHelper.getInstance().init(this);
    }
}
