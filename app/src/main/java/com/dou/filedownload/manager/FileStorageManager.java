package com.dou.filedownload.manager;

import android.content.Context;
import android.os.Environment;
import android.support.v4.app.FragmentTransitionImpl;

import com.dou.filedownload.common.Md5Util;

import java.io.File;
import java.io.IOException;

/**
 * 文件存储管理器
 * Data: 2019/1/4 18:54:03
 * author: yonghong.liu
 */
public class FileStorageManager {

    private static final FileStorageManager fsManager = new FileStorageManager();
    private Context context;

    public void init(Context context) {
        this.context = context;
    }

    public static FileStorageManager getInstance() {
        return fsManager;
    }

    private FileStorageManager() {
    }

    ;

    public File getFileByName(String url) {
        File parent;
        if (Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)) {
            parent = context.getExternalCacheDir();
        } else {
            parent = context.getCacheDir();
        }
        String fileName = Md5Util.generateCode(url);
        File file = new File(parent, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }


}
