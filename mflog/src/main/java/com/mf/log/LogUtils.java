package com.mf.log;

import android.content.Context;

import com.elvishew.xlog.XLog;
import com.mf.log.upload.IUploadFile;
import com.mf.log.upload.UploadFileToMec;
import com.mf.log.upload.UploadFileToQiniu;


public class LogUtils {

    static {
        com.blankj.utilcode.util.LogUtils.Config config = com.blankj.utilcode.util.LogUtils.getConfig();
        config.setStackOffset(1);
    }

    private static IUploadFile uploadFile;

    public static String getSdPath() {
        return LogManager.getInstance().getSdPath();
    }

    public static void init(Context context) {
        init(context, new LogConfig.Builder().build());
    }

    public static void init(Context context, LogConfig config) {
        if (config == null) {
            config = new LogConfig.Builder().build();
        }
        LogManager.getInstance().init(context, config);
        if (config.isUploadLogFile) {
            if (LogConfig.UploadConfig.Target.MEC == config.uploadConfig.target) {
                uploadFile = new UploadFileToMec();
            } else if (LogConfig.UploadConfig.Target.CLOUD == config.uploadConfig.target) {
                uploadFile = new UploadFileToQiniu();
            }
            if (uploadFile != null) {
                uploadFile.setUrl(config.uploadConfig.url);
                uploadFile.setProject(config.uploadConfig.project);
                uploadFile.setDeviceId(config.uploadConfig.deviceId);
                LogManager.getInstance().setLogBackupListener((filePath, filename) -> {
                    uploadFile.uploadFile(filePath, filename, IUploadFile.FileType.IMAGE);
                });
            }
        }
    }

    public static void unInit() {
        LogManager.getInstance().setLogBackupListener(null);
        LogManager.getInstance().unInit();
    }

    public static void v(String tag, String msg) {
        if (LogManager.getInstance().isInit()) {
            XLog.tag(tag).v(msg);
        } else {
            com.blankj.utilcode.util.LogUtils.vTag(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (LogManager.getInstance().isInit()) {
            XLog.tag(tag).v(msg, tr);
        } else {
            com.blankj.utilcode.util.LogUtils.vTag(tag, msg, tr);
        }
    }

    public static void v(final Object... contents) {
        if (LogManager.getInstance().isInit()) {
            XLog.v(contents);
        } else {
            com.blankj.utilcode.util.LogUtils.v(contents);
        }
    }

    public static void vTag(final String tag, final Object... contents) {
        if (LogManager.getInstance().isInit()) {
            XLog.tag(tag).v(contents);
        } else {
            com.blankj.utilcode.util.LogUtils.vTag(tag, contents);
        }
    }

    public static void v(Object object) {
        if (LogManager.getInstance().isInit()) {
            XLog.v(object);
        } else {
            com.blankj.utilcode.util.LogUtils.v(object);
        }
    }

    public static void v(String format, Object... args) {
        if (LogManager.getInstance().isInit()) {
            XLog.v(format, args);
        } else {
            com.blankj.utilcode.util.LogUtils.v(String.format(format, args));
        }
    }

    public static void v(String msg) {
        if (LogManager.getInstance().isInit()) {
            XLog.v(msg);
        } else {
            com.blankj.utilcode.util.LogUtils.v(msg);
        }
    }

    public static void v(String msg, Throwable tr) {
        if (LogManager.getInstance().isInit()) {
            XLog.v(msg, tr);
        } else {
            com.blankj.utilcode.util.LogUtils.v(msg, tr);
        }
    }

    public static void d(String tag, String msg) {
        if (LogManager.getInstance().isInit()) {
            XLog.tag(tag).d(msg);
        } else {
            com.blankj.utilcode.util.LogUtils.dTag(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (LogManager.getInstance().isInit()) {
            XLog.tag(tag).d(msg, tr);
        } else {
            com.blankj.utilcode.util.LogUtils.dTag(tag, msg, tr);
        }
    }

    public static void d(final Object... contents) {
        if (LogManager.getInstance().isInit()) {
            XLog.d(contents);
        } else {
            com.blankj.utilcode.util.LogUtils.d(contents);
        }
    }

    public static void dTag(final String tag, final Object... contents) {
        if (LogManager.getInstance().isInit()) {
            XLog.tag(tag).d(contents);
        } else {
            com.blankj.utilcode.util.LogUtils.dTag(tag, contents);
        }
    }

    public static void d(Object object) {
        if (LogManager.getInstance().isInit()) {
            XLog.d(object);
        } else {
            com.blankj.utilcode.util.LogUtils.d(object);
        }
    }

    public static void d(String format, Object... args) {
        if (LogManager.getInstance().isInit()) {
            XLog.d(format, args);
        } else {
            com.blankj.utilcode.util.LogUtils.d(String.format(format, args));
        }
    }

    public static void d(String msg) {
        if (LogManager.getInstance().isInit()) {
            XLog.d(msg);
        } else {
            com.blankj.utilcode.util.LogUtils.d(msg);
        }
    }

    public static void d(String msg, Throwable tr) {
        if (LogManager.getInstance().isInit()) {
            XLog.d(msg, tr);
        } else {
            com.blankj.utilcode.util.LogUtils.d(msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        if (LogManager.getInstance().isInit()) {
            XLog.tag(tag).i(msg);
        } else {
            com.blankj.utilcode.util.LogUtils.iTag(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (LogManager.getInstance().isInit()) {
            XLog.tag(tag).i(msg, tr);
        } else {
            com.blankj.utilcode.util.LogUtils.iTag(tag, msg, tr);
        }
    }

    public static void i(final Object... contents) {
        if (LogManager.getInstance().isInit()) {
            XLog.i(contents);
        } else {
            com.blankj.utilcode.util.LogUtils.i(contents);
        }
    }

    public static void iTag(final String tag, final Object... contents) {
        if (LogManager.getInstance().isInit()) {
            XLog.tag(tag).i(contents);
        } else {
            com.blankj.utilcode.util.LogUtils.iTag(tag, contents);
        }
    }

    public static void i(Object object) {
        if (LogManager.getInstance().isInit()) {
            XLog.i(object);
        } else {
            com.blankj.utilcode.util.LogUtils.i(object);
        }
    }

    public static void i(String format, Object... args) {
        if (LogManager.getInstance().isInit()) {
            XLog.i(format, args);
        } else {
            com.blankj.utilcode.util.LogUtils.i(String.format(format, args));
        }
    }

    public static void i(String msg) {
        if (LogManager.getInstance().isInit()) {
            XLog.i(msg);
        } else {
            com.blankj.utilcode.util.LogUtils.i(msg);
        }
    }

    public static void i(String msg, Throwable tr) {
        if (LogManager.getInstance().isInit()) {
            XLog.i(msg, tr);
        } else {
            com.blankj.utilcode.util.LogUtils.i(msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        if (LogManager.getInstance().isInit()) {
            XLog.tag(tag).w(msg);
        } else {
            com.blankj.utilcode.util.LogUtils.wTag(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (LogManager.getInstance().isInit()) {
            XLog.tag(tag).w(msg, tr);
        } else {
            com.blankj.utilcode.util.LogUtils.wTag(tag, msg, tr);
        }
    }

    public static void w(final Object... contents) {
        if (LogManager.getInstance().isInit()) {
            XLog.w(contents);
        } else {
            com.blankj.utilcode.util.LogUtils.w(contents);
        }
    }

    public static void wTag(final String tag, final Object... contents) {
        if (LogManager.getInstance().isInit()) {
            XLog.tag(tag).w(contents);
        } else {
            com.blankj.utilcode.util.LogUtils.wTag(tag, contents);
        }
    }

    public static void w(Object object) {
        if (LogManager.getInstance().isInit()) {
            XLog.w(object);
        } else {
            com.blankj.utilcode.util.LogUtils.w(object);
        }
    }

    public static void w(String format, Object... args) {
        if (LogManager.getInstance().isInit()) {
            XLog.w(format, args);
        } else {
            com.blankj.utilcode.util.LogUtils.w(String.format(format, args));
        }
    }

    public static void w(String msg) {
        if (LogManager.getInstance().isInit()) {
            XLog.w(msg);
        } else {
            com.blankj.utilcode.util.LogUtils.w(msg);
        }
    }

    public static void w(String msg, Throwable tr) {
        if (LogManager.getInstance().isInit()) {
            XLog.w(msg, tr);
        } else {
            com.blankj.utilcode.util.LogUtils.w(msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        if (LogManager.getInstance().isInit()) {
            XLog.tag(tag).e(msg);
        } else {
            com.blankj.utilcode.util.LogUtils.eTag(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (LogManager.getInstance().isInit()) {
            XLog.tag(tag).e(msg, tr);
        } else {
            com.blankj.utilcode.util.LogUtils.eTag(tag, msg, tr);
        }
    }

    public static void e(final Object... contents) {
        if (LogManager.getInstance().isInit()) {
            XLog.e(contents);
        } else {
            com.blankj.utilcode.util.LogUtils.e(contents);
        }
    }

    public static void eTag(final String tag, final Object... contents) {
        if (LogManager.getInstance().isInit()) {
            XLog.tag(tag).e(contents);
        } else {
            com.blankj.utilcode.util.LogUtils.eTag(tag, contents);
        }
    }

    public static void e(Object object) {
        if (LogManager.getInstance().isInit()) {
            XLog.e(object);
        } else {
            com.blankj.utilcode.util.LogUtils.e(object);
        }
    }

    public static void e(String format, Object... args) {
        if (LogManager.getInstance().isInit()) {
            XLog.e(format, args);
        } else {
            com.blankj.utilcode.util.LogUtils.e(String.format(format, args));
        }
    }

    public static void e(String msg) {
        if (LogManager.getInstance().isInit()) {
            XLog.e(msg);
        } else {
            com.blankj.utilcode.util.LogUtils.e(msg);
        }
    }

    public static void e(String msg, Throwable tr) {
        if (LogManager.getInstance().isInit()) {
            XLog.e(msg, tr);
        } else {
            com.blankj.utilcode.util.LogUtils.e(msg, tr);
        }
    }
}
