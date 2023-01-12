package com.mf.log;

import android.content.Context;
import android.os.Environment;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.flattener.ClassicFlattener;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.backup.FileSizeBackupStrategy2;
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy;
import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

import java.io.File;
import java.io.FilenameFilter;


public class LogUtils {

    private static String marLogDirPath;
    private static String marLogPath;
    private static Context context;

    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("marsxlog");
    }

    public static void init(Context context, String logPath) {
        init(context, logPath, false, false, false);
    }

    public static void init(Context context, String logPath, boolean enableThreadInfo, boolean enableBorder, boolean enableStackTrace) {
        LogUtils.context = context;
        if (logPath == null || logPath.length() == 0) {
            logPath = "/mf/log/tmp/";
        }
        LogConfiguration.Builder builder = new LogConfiguration.Builder();
        if (enableThreadInfo) {
            builder.enableThreadInfo();
        } else {
            builder.disableThreadInfo();
        }
        if (enableBorder) {
            builder.enableBorder();
        } else {
            builder.disableBorder();
        }
        if (enableStackTrace) {
            builder.enableStackTrace(3);
        } else {
            builder.disableStackTrace();
        }
        LogConfiguration config = builder.build();
        Printer androidPrinter = new AndroidPrinter(true);
        Printer filePrinter = new FilePrinter
                .Builder(getSdPath() + File.separator + logPath)
                .fileNameGenerator(new LevelAndDateFileNameGenerator())
                .backupStrategy(new FileSizeBackupStrategy2(10 * 1024 * 1024/*10MB*/, 100))
                .cleanStrategy(new FileLastModifiedCleanStrategy(5 * 24 * 60 * 60 * 1000/*5天*/))
                .flattener(new ClassicFlattener())
                .build();

        Printer marsXLogPrinter = new Printer() {
            @Override
            public void println(int logLevel, String tag, String msg) {

                final int size = 1024;
                if (msg.length() <= size) {
                    Log.i(tag, Constant.ENTER + msg + Constant.ENTER + Constant.ENTER);
                    return;
                }

                int msgLength = msg.length();
                int start = 0;
                int end = start + size;
                Log.i(tag, Constant.ENTER);
                while (start < msgLength) {
                    Log.i(tag, msg.substring(start, end));
                    start = end;
                    end = Math.min(start + size, msgLength);
                }
                Log.i(tag, Constant.ENTER + Constant.ENTER);
            }
        };

        initMarsXLog("/mf/log/mars/");
        XLog.init(config, androidPrinter, marsXLogPrinter, filePrinter);
    }

    public static void backup() {
        Log.appenderFlush();
        Log.appenderClose();
        if (marLogDirPath != null && marLogDirPath.length() > 0) {
            File marLogDir = new File(marLogDirPath);
            File[] files = marLogDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".xlog");
                }
            });
            if (files != null) {
                for (File file : files) {
                    file.renameTo(new File(file.getAbsolutePath() + ".bak"));
                }
            }
        }
        initMarsXLog(marLogPath);
    }

    /**
     * 获取sd卡路径
     */
    private static String getSdPath() {
        File sdDir;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        } else {
            sdDir = Environment.getDataDirectory();
        }
        return sdDir.getAbsolutePath();
    }

    /**
     * 初始化mars
     */
    private static void initMarsXLog(String logPath) {
        marLogPath = logPath;
        marLogDirPath = getSdPath() + File.separator + logPath;
        File file = new File(marLogDirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        // this is necessary, or may crash for SIGBUS
        final String marCachePath = context.getFilesDir() + "/xlog";

        Log.setLogImp(new Xlog());
        Log.appenderOpen(Xlog.LEVEL_DEBUG,
                Xlog.AppednerModeAsync, marCachePath, marLogDirPath,
                "log", 0);
        Log.setConsoleLogOpen(false);
    }

    // ============ 日志输出方法 ============

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
