package com.mf.log.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.mf.log.LogUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Random;


/**
 * @author Simon
 * @data: 2016/11/25 18:13
 * @version: V1.0
 */
public enum TimerHandler {
    INSTANCE;

    public static TimerHandler getInstance() {
        return INSTANCE;
    }

    private HashSet<Integer> taskIdSet = new HashSet<>();

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String method = bundle.getString("method");
            long period = bundle.getLong("period");
            if (method == null || method.isEmpty()) {
                if (msg.obj instanceof Runnable) {
                    Runnable runnable = (Runnable) msg.obj;
                    runnable.run();
                } else if (msg.obj instanceof TimerRunable) {
                    TimerRunable timerRunable = (TimerRunable) msg.obj;
                    timerRunable.accept(msg.what);
                }
            } else {
                exeTask(msg.obj, method);
            }
            if (period > 0) {
                Message message = Message.obtain(msg);
                handler.sendMessageDelayed(message, period);
            }
        }
    };

    /**
     * 取消指定ID的任务
     *
     * @param what
     */
    public void cancel(int what) {
        synchronized (taskIdSet) {
            if (handler.hasMessages(what)) {
                taskIdSet.remove(what);
                handler.removeMessages(what);
            }
        }
    }

    public boolean hasMessage(int id) {
        synchronized (taskIdSet) {
            return taskIdSet.contains(id);
        }
    }

    /**
     * 定时运行一个任务
     *
     * @param runnable
     * @param delay
     * @param period
     * @return 本次任务分配的ID，可用于查找、删除本任务
     */
    public int schedule(Runnable runnable, long delay, long period) {
        LogUtils.d("new Timer create , delay is %s", delay);
        if (delay < 0) {
            return -1;
        }
        Message message = Message.obtain();
        message.what = obtainID();
        message.obj = runnable;
        Bundle bundle = new Bundle();
        bundle.putLong("period", period);
        message.setData(bundle);
        handler.sendMessageDelayed(message, delay);
        return message.what;
    }

    public int schedule(TimerRunable<?> runnable, long delay, long period) {
        LogUtils.d("new Timer create , delay is %s", delay);
        if (delay < 0) {
            return -1;
        }
        Message message = Message.obtain();
        message.what = obtainID();
        message.obj = runnable;
        Bundle bundle = new Bundle();
        bundle.putLong("period", period);
        message.setData(bundle);
        handler.sendMessageDelayed(message, delay);
        return message.what;
    }

    /**
     * 定时运行一个任务
     *
     * @param instance
     * @param method
     * @param delay
     * @param period
     * @return 本次任务分配的ID，可用于查找、删除本任务
     */
    public int schedule(Object instance, String method, long delay, long period) {
        LogUtils.d("new Timer create , delay is %s", delay);
        if (delay < 0) {
            return -1;
        }
        Message message = Message.obtain();
        message.what = obtainID();
        message.obj = instance;
        Bundle bundle = new Bundle();
        bundle.putLong("period", period);
        bundle.putString("method", method);
        message.setData(bundle);
        handler.sendMessageDelayed(message, delay);
        return message.what;
    }

    private synchronized int obtainID() {
        int id = new Random().nextInt(Integer.MAX_VALUE);
        if (!taskIdSet.add(id)) {
            return obtainID();
        }
        return id;
    }

    private void exeTask(Object object, String methodName) {
        Class className = object.getClass();
        try {
            Method method = className.getDeclaredMethod(methodName, new Class[]{});
            method.setAccessible(true);
            method.invoke(object, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
