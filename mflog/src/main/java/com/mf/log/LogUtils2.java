package com.mf.log;

public class LogUtils2 {

    static {
        com.blankj.utilcode.util.LogUtils.getConfig().setStackOffset(1);
    }

    public static void v(final Object... contents) {
        com.blankj.utilcode.util.LogUtils.v(contents);
    }

    public static void vTag(final String tag, final Object... contents) {
        com.blankj.utilcode.util.LogUtils.vTag(tag, contents);
    }

    public static void d(final Object... contents) {
        com.blankj.utilcode.util.LogUtils.d(contents);
    }

    public static void dTag(final String tag, final Object... contents) {
        com.blankj.utilcode.util.LogUtils.dTag(tag, contents);
    }

    public static void i(final Object... contents) {
        com.blankj.utilcode.util.LogUtils.i(contents);
    }

    public static void iTag(final String tag, final Object... contents) {
        com.blankj.utilcode.util.LogUtils.iTag(tag, contents);
    }

    public static void w(final Object... contents) {
        com.blankj.utilcode.util.LogUtils.w(contents);
    }

    public static void wTag(final String tag, final Object... contents) {
        com.blankj.utilcode.util.LogUtils.wTag(tag, contents);
    }

    public static void e(final Object... contents) {
        com.blankj.utilcode.util.LogUtils.e(contents);
    }

    public static void eTag(final String tag, final Object... contents) {
        com.blankj.utilcode.util.LogUtils.eTag(tag, contents);
    }

    public static void a(final Object... contents) {
        com.blankj.utilcode.util.LogUtils.a(contents);
    }

    public static void aTag(final String tag, final Object... contents) {
        com.blankj.utilcode.util.LogUtils.aTag(tag, contents);
    }
}
