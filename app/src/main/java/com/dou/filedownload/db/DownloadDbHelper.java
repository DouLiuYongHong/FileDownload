package com.dou.filedownload.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dou.filedownload.db.greendao.DaoMaster;
import com.dou.filedownload.db.greendao.DaoSession;
import com.dou.filedownload.db.greendao.DownloadEntityDao;

import java.util.List;

/**
 * @author nate
 */
public class DownloadDbHelper {

    private static DownloadDbHelper sHelper = new DownloadDbHelper();

    public static DownloadDbHelper getInstance() {
        return sHelper;
    }

    private DownloadDbHelper() {

    }

    public void init(Context context) {
        SQLiteDatabase db = new DaoMaster.DevOpenHelper(context, "download.db", null).getWritableDatabase();
        mMaster = new DaoMaster(db);
        mSession = mMaster.newSession();
        mDao = mSession.getDownloadEntityDao();
    }

    private DaoMaster mMaster;

    private DaoSession mSession;

    private DownloadEntityDao mDao;


    public void insert(DownloadEntity entity) {
        mDao.insertOrReplace(entity);
    }

    public List<DownloadEntity> getAll(String url) {
        return mDao.queryBuilder().where(DownloadEntityDao.Properties.Download_url.eq(url)).orderAsc(DownloadEntityDao.Properties.Thread_id).list();
    }


}
