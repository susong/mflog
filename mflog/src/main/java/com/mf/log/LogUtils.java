package com.mf.log;

import android.content.Context;

import com.elvishew.xlog.XLog;
import com.mf.log.upload.IUploadFile;
import com.mf.log.upload.UploadFileToMec;


public class LogUtils {

    private static IUploadFile iUploadFile = new UploadFileToMec();

    public static void init(Context context, String logBasePath, String logDir) {
        init(context, logBasePath, logDir, false, false, false);
    }

    public static void init(Context context, String logBasePath, String logDir, boolean enableThreadInfo, boolean enableBorder, boolean enableStackTrace) {
        LogManager.getInstance().init(context, logBasePath, logDir, enableThreadInfo, enableBorder, enableStackTrace);
        LogManager.getInstance().setLogBackupListener((filePath, filename) -> {
            iUploadFile.uploadFile(filePath, filename, IUploadFile.FileType.IMAGE);
        });
    }

    public static void unInit() {
        LogManager.getInstance().setLogBackupListener(null);
        LogManager.getInstance().unInit();
    }

    public static void v(String tag, String msg) {
        XLog.tag(tag).v(msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        XLog.tag(tag).v(msg, tr);
    }

    public static void v(final Object... contents) {
        XLog.v(contents);
    }

    public static void vTag(final String tag, final Object... contents) {
        XLog.tag(tag).v(contents);
    }

    public static void v(Object object) {
        XLog.v(object);
    }

    public static void v(String format, Object... args) {
        XLog.v(format, args);
    }

    public static void v(String msg) {
        XLog.v(msg);
    }

    public static void v(String msg, Throwable tr) {
        XLog.v(msg, tr);
    }

    public static void d(String tag, String msg) {
        XLog.tag(tag).d(msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        XLog.tag(tag).d(msg, tr);
    }

    public static void d(final Object... contents) {
        XLog.d(contents);
    }

    public static void dTag(final String tag, final Object... contents) {
        XLog.tag(tag).d(contents);
    }

    public static void d(Object object) {
        XLog.d(object);
    }

    public static void d(String format, Object... args) {
        XLog.d(format, args);
    }

    public static void d(String msg) {
        XLog.d(msg);
    }

    public static void d(String msg, Throwable tr) {
        XLog.d(msg, tr);
    }

    public static void i(String tag, String msg) {
        XLog.tag(tag).i(msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        XLog.tag(tag).i(msg, tr);
    }

    public static void i(final Object... contents) {
        XLog.i(contents);
    }

    public static void iTag(final String tag, final Object... contents) {
        XLog.tag(tag).i(contents);
    }

    public static void i(Object object) {
        XLog.i(object);
    }

    public static void i(String format, Object... args) {
        XLog.i(format, args);
    }

    public static void i(String msg) {
        XLog.i(msg);
    }

    public static void i(String msg, Throwable tr) {
        XLog.i(msg, tr);
    }

    public static void w(String tag, String msg) {
        XLog.tag(tag).w(msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        XLog.tag(tag).w(msg, tr);
    }

    public static void w(final Object... contents) {
        XLog.w(contents);
    }

    public static void wTag(final String tag, final Object... contents) {
        XLog.tag(tag).w(contents);
    }

    public static void w(Object object) {
        XLog.w(object);
    }

    public static void w(String format, Object... args) {
        XLog.w(format, args);
    }

    public static void w(String msg) {
        XLog.w(msg);
    }

    public static void w(String msg, Throwable tr) {
        XLog.w(msg, tr);
    }

    public static void e(String tag, String msg) {
        XLog.tag(tag).e(msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        XLog.tag(tag).e(msg, tr);
    }

    public static void e(final Object... contents) {
        XLog.e(contents);
    }

    public static void eTag(final String tag, final Object... contents) {
        XLog.tag(tag).e(contents);
    }

    public static void e(Object object) {
        XLog.e(object);
    }

    public static void e(String format, Object... args) {
        XLog.e(format, args);
    }

    public static void e(String msg) {
        XLog.e(msg);
    }

    public static void e(String msg, Throwable tr) {
        XLog.e(msg, tr);
    }
}
