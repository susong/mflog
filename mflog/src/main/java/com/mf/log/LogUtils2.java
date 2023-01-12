package com.mf.log;

import android.os.Environment;
import android.util.Log;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.Printer;
import com.tencent.mars.xlog.Xlog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LogUtils2 {
    private static boolean mIsSaveLog = true;
    private static SimpleDateFormat mDateFormatFileName;
    private static SimpleDateFormat mDateFormat;
    private static String mLogFilePath;
    private static String mLogWithDateFilePath;
    private static String mLogWithDateFile;
    private static String mTagFile;

    static {
        mDateFormatFileName = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        mDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        mDateFormatFileName.applyPattern("yyyy-MM-dd");
        mDateFormat.applyPattern("yyyy-MM-dd  HH:mm:ss.SSS");
        Calendar calendar = Calendar.getInstance();
        String sdPath = getSdPath();
        mLogFilePath = sdPath + Constant.LOG_PATH;
        mLogWithDateFilePath = mLogFilePath + mDateFormatFileName.format(calendar.getTime()) + File.separator;
        mLogWithDateFile = mLogWithDateFilePath + "mf.log";
        mTagFile = sdPath + File.separator + "MfLogFlag";
    }

    /**
     * 是否记录日志
     *
     * @param isSaveLog 是否记录日志
     */
    public static void setIsSaveLog(boolean isSaveLog) {
        mIsSaveLog = isSaveLog;
        File tagFile = new File(mTagFile);
        if (mIsSaveLog) {
            if (!tagFile.exists()) {
                try {
                    if (tagFile.createNewFile()) {
                        Log.d(Constant.TAG, "create tagfile success");
                    }
                } catch (IOException e) {
                    Log.d(Constant.TAG, e.getMessage(), e);
                }
            }
        } else {
            if (tagFile.exists()) {
                if (tagFile.delete()) {
                    Log.d(Constant.TAG, "delete tagfile success");
                }
            }
        }
    }

    /**
     * 获取是否记录日志
     *
     * @return 是否记录日志
     */
    public static boolean isSaveLog() {
        File tagFile = new File(mTagFile);
        mIsSaveLog = tagFile.exists();
        // 先默认一直开启日志记录
        // mIsSaveLog = true;
        return mIsSaveLog;
    }

    /**
     * 把文本写入日志文件
     *
     * @param text 要写入的文本
     */
    public static void writeTextToFile(String text) {
        // 这里不要打日志，会死循环
        BufferedWriter bw = null;
        try {
            if (mIsSaveLog) {
                String str = mDateFormat.format(new Date()) + " " + text;
                File file = createLogFile();
                bw = new BufferedWriter(new FileWriter(file, true));
                bw.write(str + "\r\n");
                bw.close();
            }
        } catch (IOException e) {
            Log.d(Constant.TAG, e.getMessage(), e);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    Log.d(Constant.TAG, e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 获取日志文件
     *
     * @return 日志文件
     */
    public static File getLogFile() {
        return createLogFile();
    }

    /**
     * 创建日志文件
     *
     * @return 日志文件
     */
    private static File createLogFile() {
        // 这里不要打日志，会死循环
        File path = new File(mLogWithDateFilePath);
        if (!path.exists() || !path.isDirectory()) {
            if (path.delete()) {
                Log.d(Constant.TAG, "delete illegal log path");
            }
            if (path.mkdirs()) {
                Log.d(Constant.TAG, "create log path success");
            }
        }
        File file = new File(mLogWithDateFile);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    Log.d(Constant.TAG, "create log file success");
                }
            } catch (IOException e) {
                Log.d(Constant.TAG, e.getMessage(), e);
            }
        }
        return file;
    }

    /**
     * 删除所有日志
     */
    public static void deleteAllLogFile() {
        deleteFile(new File(mLogFilePath));
    }

    /**
     * 删除10天前的日志
     */
    public static void deleteOldLogFile() {
        XLog.i("deleteOldLogFile");
        Calendar calendar = Calendar.getInstance();
        List<String> dateList = new ArrayList<>();
        dateList.add(mDateFormatFileName.format(calendar.getTime()));
        for (int i = 0; i < 9; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            dateList.add(mDateFormatFileName.format(calendar.getTime()));
        }
        XLog.i("keepLogFile:" + Arrays.toString(dateList.toArray()));
        File[] files = new File(mLogFilePath).listFiles();
        if (files == null) {
            return;
        }
        for (File f : files) {
            boolean isCanDelete = true;
            String name = f.getName();
            for (String s : dateList) {
                if (s.equals(name)) {
                    isCanDelete = false;
                    break;
                }
            }
            if (isCanDelete) {
                deleteFile(f);
            }
        }
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static boolean deleteFile(File file) {
        if (file.isFile()) {
            return file.delete();

        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                return file.delete();

            }
            for (File f : childFile) {
                deleteFile(f);
            }
            return file.delete();
        }
        return false;
    }

    /**
     * android获取sd卡路径方法
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
     * xlog与mars中需要使用的日期格式
     */
    private static ThreadLocal<SimpleDateFormat> mLocalDateFormat = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);
        }
    };

    /**
     * 初始化日志
     */
    public static void initLog() {
        initMarsXLog();
        initXLog();
    }


    /**
     * 初始化xlog
     */
    private static void initXLog() {
        LogConfiguration config = new LogConfiguration.Builder()
                // 指定日志级别，低于该级别的日志将不会被打印，默认为 LogLevel.ALL
                .logLevel(LogLevel.ALL)
                // 指定 TAG，默认为 "X-LOG"
                .tag("XLog")
                // 允许打印线程信息，默认禁止
                .t()
                // 允许打印深度为2的调用栈信息，默认禁止
                .st(1)
                .build();

        // 通过 android.util.Log 打印日志的打印器
        // Printer androidPrinter = new AndroidPrinter();
        Printer androidPrinter = new Printer() {
            @Override
            public void println(int logLevel, String tag, String msg) {
                final int size = 1024;
                if (msg.length() <= size) {
                    Log.println(logLevel, tag, msg);
                    return;
                }

                int msgLength = msg.length();
                int start = 0;
                int end = start + size;
                while (start < msgLength) {
                    Log.println(logLevel, tag, msg.substring(start, end));

                    start = end;
                    end = Math.min(start + size, msgLength);
                }
            }
        };

        // 打印日志到文件的打印器
//        Printer filePrinter = new FilePrinter
//                // 指定保存日志文件的路径
//                .Builder(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Constant.PUBLIC_PATH + "/xlog/")
//                // 指定日志文件名生成器，默认为 ChangelessFileNameGenerator("log")
//                .fileNameGenerator(new DateFileNameGenerator())
//                .backupStrategy(new BackupStrategy() {
//                    @Override
//                    public boolean shouldBackup(File file) {
//                        return false;
//                    }
//                })
//                // 指定日志平铺器，默认为 DefaultFlattener
//                .logFlattener(new Flattener() {
//                    @Override
//                    public CharSequence flatten(int logLevel, String tag, String message) {
//                        SimpleDateFormat sdf = mLocalDateFormat.get();
//                        sdf.setTimeZone(TimeZone.getDefault());
//                        String date = sdf.format(new Date());
//                        return date + "|" + LogLevel.getShortLevelName(logLevel) + "|"
//                               + tag + Constant.ENTER + message + Constant.ENTER;
//                    }
//                })
//                .build();

        Printer marsXLogPrinter = new Printer() {
            @Override
            public void println(int logLevel, String tag, String msg) {
//                SimpleDateFormat sdf = mLocalDateFormat.get();
//                sdf.setTimeZone(TimeZone.getDefault());
//                String date = sdf.format(new Date());

                final int size = 1024;
                if (msg.length() <= size) {
                    com.tencent.mars.xlog.Log.i(tag, Constant.ENTER + msg + Constant.ENTER + Constant.ENTER);
                    return;
                }

                int msgLength = msg.length();
                int start = 0;
                int end = start + size;
                while (start < msgLength) {
                    com.tencent.mars.xlog.Log.i(tag, Constant.ENTER + msg.substring(start, end) + Constant.ENTER + Constant.ENTER);
                    start = end;
                    end = Math.min(start + size, msgLength);
                }
            }
        };

//        if (isSaveLog()) {
            XLog.init(config, androidPrinter, marsXLogPrinter);
//        } else {
//            XLog.init(config, marsXLogPrinter);
//        }

    }

    /**
     * 初始化mars
     */
    private static void initMarsXLog() {
        // mars
        final String SDCARD = getSdPath();
        final String logPath = SDCARD + File.separator + Constant.LOG_PATH + "/marslog/";
        //init xlog
        com.tencent.mars.xlog.Log.appenderOpen(Xlog.LEVEL_DEBUG, Xlog.AppednerModeAsync, "", logPath, "log", 1);
        com.tencent.mars.xlog.Log.setLogImp(new Xlog());
    }

    /**
     * 获取mars日志文件路径
     *
     * @return 日志文件
     */
    public static String getMarsXLogFilePath() {
        return getSdPath() + File.separator + Constant.LOG_PATH + "/marslog/" + "log_" + new SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(new Date()) + ".xlog";
    }

    /**
     * 获取mars日志文件
     *
     * @return 日志文件
     */
    public static File getMarsXLogFile() {
        return new File(getMarsXLogFilePath());
    }

    /*
     * 加载mars的os库
     */
    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("marsxlog");
    }

}
