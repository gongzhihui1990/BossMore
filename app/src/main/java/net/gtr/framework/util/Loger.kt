/*
 * Copyright (c) 2017. heisenberg.gong
 */

package net.gtr.framework.util

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log

import com.tencent.bugly.crashreport.BuglyLog

/**
 * 封装默认的Log类
 * @author caroline
 */
internal object Loger {

    private val DO_PRINT_STREAM = false

    private val IS_DEBUG = true

    @JvmOverloads
    fun v(msg: String, tag: String = "") {
        var msg = msg
        var tag = tag
        if (IS_DEBUG) {
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag()
                msg = generateMsg(msg)
            }
            BuglyLog.v(tag, msg)
        }
    }

    @JvmOverloads
    fun i(msg: String, tag: String = "") {
        var msg = msg
        var tag = tag
        if (IS_DEBUG) {
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag()
                msg = generateMsg(msg)
            }
            BuglyLog.i(tag, msg)
        }
    }

    @JvmOverloads
    fun d(msg: String, tag: String = "") {
        var msg = msg
        var tag = tag
        if (IS_DEBUG) {
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag()
                msg = generateMsg(msg)
            }
            BuglyLog.d(tag, msg)
        }
    }

    @JvmOverloads
    fun w(msg: String, tag: String = "") {
        var msg = msg
        var tag = tag
        if (IS_DEBUG) {
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag()
                msg = generateMsg(msg)
            }
            BuglyLog.w(tag, msg)
        }
    }
    @JvmOverloads
    fun e(msg: String, tag: String = "") {
        var msg = msg
        var tag = tag
        if (IS_DEBUG) {
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag()
                msg = generateMsg(msg)
            }
            BuglyLog.e(tag, msg)
        }
    }

    @JvmOverloads
    fun a(msg: String, tag: String = "") {
        var msg = msg
        var tag = tag
        if (IS_DEBUG) {
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag()
                msg = generateMsg(msg)
            }
            Log.println(Log.ASSERT, tag, msg)
        }
    }

    private fun generateTag(): String {
        val stack = Throwable().stackTrace
        var i = 0
        for (stackItem in stack) {

            if (stackItem.className == Loger::class.java.name) {
                //当前类堆栈不打印
                i = i + 1
                continue
            }
            break
        }
        val ste = stack[i]
        return ste.className
    }

    @SuppressLint("DefaultLocale")
    private fun generateMsg(msg: String): String {
        var msg = msg
        val stack = Throwable().stackTrace
        var i = 0
        for (stackItem in stack) {
            if (stackItem.className == Loger::class.java.name) {
                //当前类堆栈不打印
                i = i + 1
                continue
            }
            if (DO_PRINT_STREAM) {
                msg += "\n\tat " + stackItem
            }
            break
        }
        val ste = stack[i]
        return String.format("[%s][%d]%s", ste.methodName,
                ste.lineNumber, msg)
    }

}
