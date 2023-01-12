package com.mf.log.log;

import android.os.Looper;
import android.os.Process;

import com.tencent.mars.xlog.Log;

/**
 * @ClassName: Log
 * @Description: 辅助打印日志
 * @Author: duanbangchao
 * @CreateDate: 10/12/20
 * @UpdateUser: updater
 * @UpdateDate: 10/12/20
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class Logger {
    private String mTAGPrefix;
    private String mClassName;
    private final int DEFAULT_STACK_INDEX = 3;

    public Logger(Class<?> cls, String prefix) {
        mClassName = cls.getSimpleName();
        mTAGPrefix = prefix;
    }

    public Logger(String name, String prefix) {
        mClassName = name;
        mTAGPrefix = prefix;
    }

    public void verbose(String tag, String format, Object... params) {
        printLog(Log.LEVEL_VERBOSE, tag, false, DEFAULT_STACK_INDEX + 1, format, params);
    }

    public void verbose(String tag, boolean printStack, String format, Object... params) {
        printLog(Log.LEVEL_VERBOSE, tag, printStack, DEFAULT_STACK_INDEX + 1, format, params);
    }

    public void info(String tag, String format, Object... params) {
        printLog(Log.LEVEL_INFO, tag, false, DEFAULT_STACK_INDEX + 1, format, params);
    }

    public void info(String tag, boolean printStack, String format, Object... params) {
        printLog(Log.LEVEL_INFO, tag, printStack, DEFAULT_STACK_INDEX + 1, format, params);
    }

    public void debug(String tag, String format, Object... params) {
        printLog(Log.LEVEL_DEBUG, tag, false, DEFAULT_STACK_INDEX + 1, format, params);
    }

    public void debug(String tag, boolean printStack, String format, Object... params) {
        printLog(Log.LEVEL_DEBUG, tag, printStack, DEFAULT_STACK_INDEX + 1, format, params);
    }

    public void warn(String tag, String format, Object... params) {
        printLog(Log.LEVEL_WARNING, tag, false, DEFAULT_STACK_INDEX + 1, format, params);
    }

    public void warn(String tag, boolean printStack, String format, Object... params) {
        printLog(Log.LEVEL_WARNING, tag, printStack, DEFAULT_STACK_INDEX + 1, format, params);
    }


    public void error(String tag, String format, Object... params) {
        printLog(Log.LEVEL_ERROR, tag, false, DEFAULT_STACK_INDEX + 1, format, params);
    }

    public void error(String tag, boolean printStack, String format, Object... params) {
        printLog(Log.LEVEL_ERROR, tag, printStack, DEFAULT_STACK_INDEX + 1, format, params);
    }

    public void error(String tag, Throwable tr, String message, Object... params) {
        StackTraceElement stack = new Throwable().getStackTrace()[1];
        String log = params == null ? message : String.format(message, params);
        if (log == null) {
            log = "";
        }
        log += "  " + android.util.Log.getStackTraceString(tr);
        printLog(Log.LEVEL_ERROR, tag, true, DEFAULT_STACK_INDEX + 1, log);
    }

    protected void printLog(int level, String tag, boolean printStack, int stackIndex, String format, Object... params) {
        printLog(level, fixTag(tag), stackIndex, format, params);
        if (printStack) {
            printLog(level, fixTag(tag), stackIndex, getStackInfo());
        }
    }

    protected StackTraceElement getStack(int index) {
        return new Throwable().getStackTrace()[index];
    }

    private void printLog(int level, String tag, int stackIndex, String format, Object... params) {
        StackTraceElement stack = getStack(stackIndex);
        switch (level) {
            case Log.LEVEL_VERBOSE:
                Log.getImpl().logV(0, tag, stack.getFileName(), stack.getMethodName(), stack.getLineNumber(), Process
                        .myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), String.format(format, params));
                break;
            case Log.LEVEL_DEBUG:
                Log.getImpl().logD(0, tag, stack.getFileName(), stack.getMethodName(), stack.getLineNumber(), Process
                        .myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), String.format(format, params));
                break;
            case Log.LEVEL_INFO:
                Log.getImpl().logI(0, tag, stack.getFileName(), stack.getMethodName(), stack.getLineNumber(), Process
                        .myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), String.format(format, params));
                break;
            case Log.LEVEL_WARNING:
                Log.getImpl().logW(0, tag, stack.getFileName(), stack.getMethodName(), stack.getLineNumber(), Process
                        .myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), String.format(format, params));
                break;
            case Log.LEVEL_ERROR:
                Log.getImpl().logE(0, tag, stack.getFileName(), stack.getMethodName(), stack.getLineNumber(), Process
                        .myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), String.format(format, params));
                break;
            default:
        }
    }

    public String getStackInfo() {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        for (StackTraceElement stack : stacks) {
            sb.append("[");
            sb.append(stack.getClassName());
            sb.append(",");
            sb.append(stack.getMethodName());
            sb.append(",");
            sb.append(stack.getLineNumber());
            sb.append("]");
            sb.append("\n");
        }
        return sb.toString();
    }

    private String fixTag(String tag) {
        if (mTAGPrefix.isEmpty()) {
            return tag;
        } else {
            return mTAGPrefix + "." + tag;
        }
    }
}
