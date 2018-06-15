/*
 * Copyright (c) 2017. heisenberg.gong
 */

package net.gtr.framework.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.bugly.crashreport.BuglyLog;

/**
 * 封装默认的Log类
 * @author caroline
 */
public class Loger {

    private final static boolean DO_PRINT_STREAM = false;

    private final static boolean IS_DEBUG = true;

    public static void v(String msg) {
        v(msg, "");
    }

    public static void v(String msg, String tag) {
        if (IS_DEBUG) {
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag();
                msg = generateMsg(msg);
            }
            BuglyLog.v(tag, msg);
        }
    }

    public static void i(String msg) {
        i(msg, "");
    }

    public static void i(String msg, String tag) {
        if (IS_DEBUG) {
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag();
                msg = generateMsg(msg);
            }
            BuglyLog.i(tag, msg);
        }
    }

    public static void d(String msg) {
        d(msg, "");
    }

    public static void d(String msg, String tag) {
        if (IS_DEBUG) {
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag();
                msg = generateMsg(msg);
            }
            BuglyLog.d(tag, msg);
        }
    }

    public static void w(String msg) {
        w(msg, "");
    }

    public static void w(String msg, String tag) {
        if (IS_DEBUG) {
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag();
                msg = generateMsg(msg);
            }
            BuglyLog.w(tag, msg);
        }
    }

    public static void e(String msg) {
        e(msg, "");
    }

    public static void e(String msg, String tag) {
        if (IS_DEBUG) {
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag();
                msg = generateMsg(msg);
            }
            BuglyLog.e(tag, msg);
        }
    }

    public static void a(String msg) {
        a(msg, "");
    }

    public static void a(String msg, String tag) {
        if (IS_DEBUG) {
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag();
                msg = generateMsg(msg);
            }
            Log.println(Log.ASSERT, tag, msg);
        }
    }

    private static String generateTag() {
        final StackTraceElement[] stack = new Throwable().getStackTrace();
        int i = 0;
        for (StackTraceElement stackItem : stack) {

            if (stackItem.getClassName().equals(Loger.class.getName())) {
                //当前类堆栈不打印
                i = i + 1;
                continue;
            }
            break;
        }
        final StackTraceElement ste = stack[i];
        return ste.getClassName();
    }

    @SuppressLint("DefaultLocale")
    private static String generateMsg(String msg) {
        final StackTraceElement[] stack = new Throwable().getStackTrace();
        int i = 0;
        for (StackTraceElement stackItem : stack) {
            if (stackItem.getClassName().equals(Loger.class.getName())) {
                //当前类堆栈不打印
                i = i + 1;
                continue;
            }
            if (DO_PRINT_STREAM) {
                msg += ("\n\tat " + stackItem);
            }
            break;
        }
        final StackTraceElement ste = stack[i];
        return String.format("[%s][%d]%s", ste.getMethodName(),
                ste.getLineNumber(), msg);
    }

}
