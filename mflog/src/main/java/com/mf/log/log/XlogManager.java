//package com.mf.log.log;
//
//import android.content.Context;
//import android.os.Environment;
//
//import com.mf.log.BuildConfig;
//import com.mf.log.LogUtils;
//import com.mf.log.utils.TimerHandler;
//import com.tencent.mars.xlog.Log;
//import com.tencent.mars.xlog.Xlog;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//
///**
// * @author Simon
// * @data: 2017/1/6 10:48
// * @version: V1.0
// */
//
//
//public enum XlogManager {
//
//    /**
//     * 单例
//     */
//    INSTANCE;
//
//    static {
//        System.loadLibrary("stlport_shared");
//        System.loadLibrary("marsxlog");
//    }
//
//    private final int INTERVAL = 5 * 60 * 1000;
//
//    private Context mContext;
//
//    private boolean isXlogOpened = false;    // Xlog是否已经开始记录日志
//
//    private int currentTaskID = -1;          // 当前定时任务ID
//
//    private String logPath = "";              // 本地日志储存路径
//
//    File[] files;
//    boolean bUpLoading = false;
//
//    private boolean isConsoleLogOpen = BuildConfig.DEBUG;
//
//    private static final long overdueTime = 3 * 3600 * 1000; //保存3个小时的日志
//    private static final long maxSize = 300 * 1024 * 1024; //50M的日志 改为500M
//
//    private String prefixName = "";
//
//    public void initXlog(Context context, String prefixName) {
//
//        if (isXlogOpened) {
//            return;
//        }
//
//        isXlogOpened = true;
//
//        this.mContext = context;
//        this.prefixName = prefixName;
//
//        moveLogToDocuments();
//
//        if (currentTaskID != -1) {
//            TimerHandler.getTimerHandler().cancel(currentTaskID);
//        }
//
//        currentTaskID = TimerHandler.getTimerHandler().schedule(this, "saveXlog", INTERVAL, -1);
//        // 本地日志储存目录
//        logPath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + prefixName;
//        //init xlog
//        Log.appenderOpen(Xlog.LEVEL_ALL, Xlog.AppednerModeAsync, "", logPath, prefixName, 1);
//        Log.setConsoleLogOpen(isConsoleLogOpen);
//        Log.setLogImp(new Xlog());
//
//        LogUtils.d("prefixName-->" + prefixName);
//        LogUtils.d("init xlog , can %s output to console.", (isConsoleLogOpen ? "" : "not"));
//    }
//
//    public void consoleLogOpen(boolean isOpen) {
//        Log.setConsoleLogOpen(isOpen);
//        isConsoleLogOpen = isOpen;
//        LogUtils.d("switch to %s output to console.", (isOpen ? "open" : "close"));
//    }
//
//    /**
//     * 关闭Xlog日志
//     */
//    private synchronized void closeXlog() {
//        Log.appenderClose();
//        isXlogOpened = false;
//    }
//
//    public void saveXlog() {
//        TimerHandler.getTimerHandler().cancel(currentTaskID);
//        closeXlog();
//        moveLogToDocuments();
//        openXlog();
//    }
//
//    /**
//     * 打开Xlog日志
//     */
//    public synchronized boolean openXlog() {
//        if (isXlogOpened) {
//            return false;
//        }
//        initXlog(mContext, prefixName);
//        return true;
//    }
//
//    private void moveLogToDocuments() {
//        File rawLog = new File(logPath);
//        File[] logs = rawLog.listFiles();
//        if (logs != null) {
//            for (File file : logs) {
//                if (!file.getName().endsWith(".xlog")) {
//                    file.delete();
//                    continue;
//                }
//
//                SimpleDateFormat format = new SimpleDateFormat("HHmmss", Locale.CHINESE);
//                String dateName = format.format(new Date(System.currentTimeMillis()));
//
//                String targetPath = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/" +
//                        dateName + "_" +
//                        file.getName();
//                FileInputStream reader = null;
//                FileOutputStream writer = null;
//                try {
//                    reader = new FileInputStream(file);
//                    writer = new FileOutputStream(targetPath);
//                    byte[] buffer = new byte[1024];
//                    int read = -1;
//                    while ((read = reader.read(buffer)) != -1) {
//                        writer.write(buffer, 0, read);
//                    }
//                    file.delete();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    if (reader != null) {
//                        try {
//                            reader.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (writer != null) {
//                        try {
//                            writer.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    public void uploadFile() {
//
//    }
//
//    public void deleteSomeLog(Context context) {
//        try {
//            File file = FileIOUtils.getCatchFileDir(context);
//            if (!file.exists()) {
//                return;
//            }
//            List<File> files = orderByModifyTime(file.listFiles());
//            deleteSomeLog(files);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private List<File> orderByModifyTime(File[] filesOrigin) {
//        List<File> files = Arrays.asList(filesOrigin);
//        Collections.sort(files, (o1, o2) -> (int) (o1.lastModified() - o2.lastModified()));
//        return files;
//    }
//
//    private void deleteSomeLog(List<File> files) {
//        ArrayList<File> fileArrayList = new ArrayList();
//        for (int i = 0; i < files.size(); i++) {
//            File file = files.get(i);
//            if (file.isDirectory()) {
//                continue;
//            }
//            fileArrayList.add(file);
//        }
//
//        long allFileSize = getAllFileSize(fileArrayList);
//        if (allFileSize > maxSize) {
//            deleteMoreLog(fileArrayList, allFileSize);
//        }
//
//        LogUtils.d("logs size:" + fileArrayList.size());
////        if (fileArrayList.size() < 6) {
////            return;
////        } else {
////            deleteOutTimeLog(fileArrayList);
////        }
//    }
//
//    private void deleteOutTimeLog(ArrayList<File> fileArrayList) {
//        long currentTime = System.currentTimeMillis();
//        for (int i = fileArrayList.size() - 1; i >= 0; i--) {
//            File file = fileArrayList.get(i);
//            long time = file.lastModified();
//            if (currentTime - time > overdueTime) {
//                LogUtils.d("delete log:" + file.getAbsolutePath() + "-time-" + time);
//                fileArrayList.remove(file);
//                if (file.exists()) {
//                    file.delete();
//                }
//            }
//        }
//    }
//
//    private void deleteMoreLog(List<File> files, long allFileSize) {
//
//        if (files.size() > 0) {
//            File file = files.get(0);
//
//            LogUtils.d("delete more log:" + file.getAbsolutePath() + "\\" + file.lastModified());
//
//            long otherFileSize = allFileSize - file.length();
//
//            files.remove(file);
//            file.delete();
//
//            if (otherFileSize > maxSize) {
//                deleteMoreLog(files, otherFileSize);
//            }
//        }
//    }
//
//    private long getAllFileSize(List<File> files) {
//        long sum = 0;
//
//        for (int i = files.size() - 1; i >= 0; i--) {
//            File file = files.get(i);
//            if (file.isDirectory()) {
//                continue;
//            }
//            sum += file.length();
//        }
//
//        return sum;
//    }
//
//}
