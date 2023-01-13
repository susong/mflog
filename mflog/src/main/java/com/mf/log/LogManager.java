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
import com.mf.log.utils.TimerHandler;
import com.tencent.mars.xlog.Xlog;

import java.io.File;
import java.io.FilenameFilter;

public enum LogManager {
    /**
     * 单例
     */
    INSTANCE;

    public static LogManager getInstance() {
        return INSTANCE;
    }

    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("marsxlog");
    }

    // 日志检测轮询时间
    private final int INTERVAL = 10 * 60 * 1000;
    // 日志总大小
    private static final long totalLogMaxSize = 500 * 1024 * 1024;/*500MB*/
    // 单个日志大小
    private static final long singleLogMaxSize = 10 * 1024 * 1024;/*10MB*/
    // 最大备份索引
    private static final int maxBackupIndex = 100;
    // 日志保留时间
    private static final long logRetentionTime = 5 * 24 * 60 * 60 * 1000;/*5天*/

    private Context context;
    private String marLogDirPath;
    private String marCachePath;
    private int timerTaskId = -1;
    // 是否已初始化
    private volatile boolean isInit = false;
    private LogBackupListener logBackupListener;

    public void setLogBackupListener(LogBackupListener logBackupListener) {
        this.logBackupListener = logBackupListener;
    }

    public void init(Context context, String logBasePath, String logDir) {
        init(context, logBasePath, logDir, false, false, false);
    }

    public void init(Context context, String logBasePath, String logDir, boolean enableThreadInfo, boolean enableBorder, boolean enableStackTrace) {
        if (isInit) {
            return;
        }
        isInit = true;
        this.context = context;

        initMarsXLog(logBasePath, logDir);
        moveMarsXLog();
        initXLog(logBasePath, logDir, enableThreadInfo, enableBorder, enableStackTrace);

        if (timerTaskId != -1) {
            TimerHandler.getInstance().cancel(timerTaskId);
        }
        timerTaskId = TimerHandler.getInstance().schedule(this, "backupMarsXLogByTimer", INTERVAL, INTERVAL);
    }

    /**
     * 备份xlog日志
     */
    public void backupMarsXLog() {
        com.tencent.mars.xlog.Log.appenderFlush();
        com.tencent.mars.xlog.Log.appenderClose();
        moveMarsXLog();
        initMarsXLog();
    }

    /**
     * 定时器自动备份xlog日志，由定时器通过反射调用
     */
    private void backupMarsXLogByTimer() {
        backupMarsXLog();
    }

    private void moveMarsXLog() {
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
                    File backupFile = new File(file.getAbsolutePath() + ".bak");
                    file.renameTo(backupFile);
                    if (logBackupListener != null) {
                        logBackupListener.onFileBackup(backupFile.getPath(), file.getName());
                    }
                }
            }
        }
    }

    /**
     * 获取sd卡路径
     */
    private String getSdPath() {
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
     * 初始化xlog
     */
    private void initXLog(String logBasePath, String logDir, boolean enableThreadInfo, boolean enableBorder, boolean enableStackTrace) {
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
                .Builder(getSdPath() + File.separator + logBasePath + File.separator + logDir)
                .fileNameGenerator(new LevelAndDateFileNameGenerator())
                .backupStrategy(new FileSizeBackupStrategy2(singleLogMaxSize, maxBackupIndex))
                .cleanStrategy(new FileLastModifiedCleanStrategy(logRetentionTime))
                .flattener(new ClassicFlattener())
                .build();

        Printer marsXLogPrinter = new Printer() {
            @Override
            public void println(int logLevel, String tag, String msg) {

                final int size = 1024;
                if (msg.length() <= size) {
                    com.tencent.mars.xlog.Log.i(tag, Constant.ENTER + msg + Constant.ENTER + Constant.ENTER);
                    return;
                }

                int msgLength = msg.length();
                int start = 0;
                int end = start + size;
                com.tencent.mars.xlog.Log.i(tag, Constant.ENTER);
                while (start < msgLength) {
                    com.tencent.mars.xlog.Log.i(tag, msg.substring(start, end));
                    start = end;
                    end = Math.min(start + size, msgLength);
                }
                com.tencent.mars.xlog.Log.i(tag, Constant.ENTER + Constant.ENTER);
            }
        };
        XLog.init(config, marsXLogPrinter, filePrinter);
    }

    /**
     * 初始化mars
     */
    private void initMarsXLog(String logBasePath, String logDir) {
        marLogDirPath = getSdPath() + File.separator + logBasePath + File.separator + logDir + "_mars";
        File file = new File(marLogDirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        // this is necessary, or may crash for SIGBUS
        marCachePath = context.getFilesDir() + File.separator + "xlog";
        file = new File(marCachePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        initMarsXLog();
    }

    /**
     * 初始化mars
     */
    private void initMarsXLog() {
        com.tencent.mars.xlog.Log.setLogImp(new Xlog());
        com.tencent.mars.xlog.Log.setConsoleLogOpen(false);
        com.tencent.mars.xlog.Log.appenderOpen(Xlog.LEVEL_DEBUG,
                Xlog.AppednerModeAsync, marCachePath, marLogDirPath,
                "log", 0);
    }
}
