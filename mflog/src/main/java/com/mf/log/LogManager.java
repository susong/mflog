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
import com.mf.log.xlog.LevelAndDateFileNameGenerator;
import com.tencent.mars.xlog.Xlog;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

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

    private Context context;
    private LogConfig config;
    // 是否已初始化
    private volatile boolean isInit = false;
    private int backupTimerTaskId = -1;
    private List<CacheFileManager> logFileManagers;
    private List<Integer> logFileManagerTimerTaskIds;
    private LogBackupListener logBackupListener;

    public void setLogBackupListener(LogBackupListener logBackupListener) {
        this.logBackupListener = logBackupListener;
    }

    public void init(Context context, LogConfig config) {
        if (isInit) {
            return;
        }
        isInit = true;
        this.context = context;
        this.config = config;

        if (config.isLogToFileByMars) {
            initMarsXLogConfig(config);
            closeMarsXLog();
            moveMarsXLog();
            initMarsXLog();
        }
        initXLog(config);
        initBackupTimer(config);
        initCleanManager(config);
    }

    public void unInit() {
        if (backupTimerTaskId != -1) {
            TimerHandler.getInstance().cancel(backupTimerTaskId);
        }
        unInitCleanManager();
        if (config.isLogToFileByMars) {
            closeMarsXLog();
        }
        isInit = false;
    }

    /**
     * 备份日志文件
     */
    public void backupLog() {
        if (config.isLogToFileByMars) {
            closeMarsXLog();
            moveMarsXLog();
            initMarsXLog();
        }
    }

    /**
     * 初始化日志备份定时器
     */
    private void initBackupTimer(LogConfig config) {
        if (backupTimerTaskId != -1) {
            TimerHandler.getInstance().cancel(backupTimerTaskId);
        }
        backupTimerTaskId = TimerHandler.getInstance()
                .schedule(this,
                        "backupMarsXLogByTimer",
                        10000/*10秒*/,
                        config.backupConfig.logCheckInterval);
    }

    /**
     * 初始化日志文件定时清理器
     */
    private void initCleanManager(LogConfig config) {
        if (config.cleanConfigs != null && config.cleanConfigs.size() > 0) {
            logFileManagers = new ArrayList<>();
            logFileManagerTimerTaskIds = new ArrayList<>();
            for (LogConfig.CleanConfig cleanConfig : config.cleanConfigs) {
                File cacheDir = new File(cleanConfig.logDir);
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }
                CacheFileManager cacheFileManager = new CacheFileManager(
                        cacheDir,
                        cleanConfig.sizeLimit,
                        cleanConfig.countLimit);
                logFileManagers.add(cacheFileManager);

                logFileManagerTimerTaskIds.add(TimerHandler.getInstance()
                        .schedule(cacheFileManager,
                                "checkFile",
                                cleanConfig.checkInterval,
                                cleanConfig.checkInterval));
            }
        }
    }

    private void unInitCleanManager() {
        if (logFileManagerTimerTaskIds != null && logFileManagerTimerTaskIds.size() > 0) {
            for (Integer taskId : logFileManagerTimerTaskIds) {
                TimerHandler.getInstance().cancel(taskId);
            }
        }
        if (logFileManagers != null) {
            logFileManagers.clear();
        }
    }

    /**
     * 初始化xlog
     */
    private void initXLog(LogConfig config) {
        LogConfiguration.Builder builder = new LogConfiguration.Builder();
        if (config.isEnableThreadInfo) {
            builder.enableThreadInfo();
        } else {
            builder.disableThreadInfo();
        }
        if (config.isEnableBorder) {
            builder.enableBorder();
        } else {
            builder.disableBorder();
        }
        if (config.isEnableStackTrace) {
            builder.enableStackTrace(3);
        } else {
            builder.disableStackTrace();
        }
        LogConfiguration logConfiguration = builder.build();

        List<Printer> printers = new ArrayList<>();

        if (config.isLogToConsole) {
            Printer androidPrinter = new AndroidPrinter(true);
            printers.add(androidPrinter);
        }

        if (config.isLogToFileByXLog) {
            Printer filePrinter = new FilePrinter
                    .Builder(config.logBasePath + File.separator + config.logDir)
                    .fileNameGenerator(new LevelAndDateFileNameGenerator())
                    .backupStrategy(new FileSizeBackupStrategy2(config.backupConfig.singleLogMaxSize, config.backupConfig.maxBackupIndex))
                    .cleanStrategy(new FileLastModifiedCleanStrategy(config.backupConfig.logRetentionTime))
                    .flattener(new ClassicFlattener())
                    .build();
            printers.add(filePrinter);
        }

        if (config.isLogToFileByMars) {
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
            printers.add(marsXLogPrinter);
        }

        XLog.init(logConfiguration, printers.toArray(new Printer[0]));
    }

    /**
     * mars保存日志路径
     */
    private String marsLogDirPath;
    /**
     * mars缓存日志路径
     */
    private String marsCachePath;

    /**
     * 定时器自动备份日志，由定时器通过反射调用
     */
    private void backupMarsXLogByTimer() {
        backupLog();
    }

    /**
     * 初始化mars配置
     */
    private void initMarsXLogConfig(LogConfig config) {
        marsLogDirPath = config.logBasePath + File.separator + config.logDir + "_mars";
        File file = new File(marsLogDirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        // this is necessary, or may crash for SIGBUS
        marsCachePath = context.getFilesDir() + File.separator + config.logDir.replaceAll("/", "_") + "_xlog";
        file = new File(marsCachePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 初始化mars
     */
    private void initMarsXLog() {
        com.tencent.mars.xlog.Log.setLogImp(new Xlog());
        com.tencent.mars.xlog.Log.setConsoleLogOpen(false);
        com.tencent.mars.xlog.Log.appenderOpen(Xlog.LEVEL_DEBUG,
                Xlog.AppednerModeAsync, marsCachePath, marsLogDirPath,
                "log", 0);
    }

    /**
     * 关闭mars
     */
    private void closeMarsXLog() {
        com.tencent.mars.xlog.Log.appenderFlush();
        com.tencent.mars.xlog.Log.appenderClose();
    }

    /**
     * 备份mars输出的日志文件
     */
    private void moveMarsXLog() {
        if (marsLogDirPath != null && marsLogDirPath.length() > 0) {
            File marLogDir = new File(marsLogDirPath);
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
    public String getSdPath() {
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
}
