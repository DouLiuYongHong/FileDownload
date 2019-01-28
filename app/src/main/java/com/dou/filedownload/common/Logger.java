package com.dou.filedownload.common;

import android.util.Log;

import java.util.Locale;

/**
 * Log 日志输出信息
 * Data: 2019/1/4 18:42:07
 * author: yonghong.liu
 */
public class Logger {

    public static final boolean ISDEBUG = true;

    public static void debug(String tag, String message) {
        if (!ISDEBUG) return;
        Log.d(tag, message);
    }

    public static void debug(String tag, String message, Object... args) {
        if (!ISDEBUG) return;
        Log.d(tag, String.format(Locale.getDefault(), message, args));
    }

    public static void error(String tag, String message) {
        if (!ISDEBUG) return;
        Log.e(tag, message);
    }


    public static void info(String tag, String message) {
        if (!ISDEBUG) return;
        Log.e(tag, message);
    }
    public static void i(String tag, String message) {
        if (!ISDEBUG) return;
        Log.e(tag, message);
    }
}
