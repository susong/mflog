package com.mf.log.log;

import android.os.Environment;

import com.mf.log.BuildConfig;
import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: LogFactory
 * @Description: 日志工厂模块
 * @Author: duanbangchao
 * @CreateDate: 10/12/20
 * @UpdateUser: updater
 * @UpdateDate: 10/12/20
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public final class LoggerFactory {
    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("marsxlog");

        final String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
        final String logPath = SDCARD + "/mf/log";
        final String logFileName = "log";

        //init xlog
        Xlog.XLogConfig logConfig = new Xlog.XLogConfig();
        logConfig.mode = Xlog.AppednerModeAsync;
        logConfig.logdir = logPath;
        logConfig.nameprefix = logFileName;
        logConfig.pubkey = "";
        logConfig.compressmode = Xlog.ZLIB_MODE;
        logConfig.compresslevel = 0;
        logConfig.cachedir = "";
        logConfig.cachedays = 0;
        if (BuildConfig.DEBUG) {
            logConfig.level = Xlog.LEVEL_VERBOSE;
            Log.setConsoleLogOpen(true);
        } else {
            logConfig.level = Xlog.LEVEL_INFO;
            Log.setConsoleLogOpen(false);
        }

        Log.setLogImp(new Xlog());
    }

    private static Map<String, Logger> mLoggerMap = new ConcurrentHashMap<>();

    public static Logger getLogger(Class<?> cls) {
        return getLogger(cls, "");
    }

    public static Logger getLogger(Class<?> cls, String tag) {
        if (!mLoggerMap.containsKey(cls.getName())) {
            mLoggerMap.put(cls.getName(), new Logger(cls, tag));
        }
        return mLoggerMap.get(cls.getName());
    }

    public static Logger getLogger(Class<?> cls, String className, Object[] params, Class<?>... parameterTypes) {
        if (!mLoggerMap.containsKey(cls.getName())) {
            Class<? extends Logger> logger = null;
            try {
                logger = (Class<? extends Logger>) Class.forName(className);
                Constructor<?> method = logger.getConstructor(parameterTypes);
                mLoggerMap.put(cls.getSimpleName(), (Logger) method.newInstance(params));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mLoggerMap.get(cls.getName());
    }
}
