package com.mf.log;

import android.os.Environment;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.flattener.DefaultFlattener;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.backup.FileSizeBackupStrategy2;
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy;

public class FlutterLogUtils {

    static {
        LogConfiguration config = new LogConfiguration.Builder()
                .enableThreadInfo()
                .disableBorder()
                .disableStackTrace()
                .build();
        Printer androidPrinter = new AndroidPrinter(true);
        Printer filePrinter = new FilePrinter
                .Builder(Environment.getExternalStorageDirectory().getPath() + "/mf/log/flutter/")
                .fileNameGenerator(new LevelAndDateFileNameGenerator())
                .backupStrategy(new FileSizeBackupStrategy2(5 * 1024 * 1024/*5MB*/, 1000))
                .cleanStrategy(new FileLastModifiedCleanStrategy(5 * 24 * 60 * 60 * 1000/*5天*/))
                .flattener(new DefaultFlattener())
                .build();

        XLog.init(config, androidPrinter, filePrinter);
    }

    public static void v(String tag, String msg) {
        XLog.tag(tag).v(msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        XLog.tag(tag).v(msg, tr);
    }

    public static void d(String tag, String msg) {
        XLog.tag(tag).d(msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        XLog.tag(tag).d(msg, tr);
    }

    public static void i(String tag, String msg) {
        XLog.tag(tag).i(msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        XLog.tag(tag).i(msg, tr);
    }

    public static void w(String tag, String msg) {
        XLog.tag(tag).w(msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        XLog.tag(tag).w(msg, tr);
    }

    public static void w(String tag, Throwable tr) {
        XLog.tag(tag).w("", tr);
    }

    public static void e(String tag, String msg) {
        XLog.tag(tag).e(msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        XLog.tag(tag).e(msg, tr);
    }

    public static void e(String tag, Throwable tr) {
        XLog.tag(tag).e("", tr);
    }
}
