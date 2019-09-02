package org.gong.bmw

import android.annotation.SuppressLint
import android.content.Context

import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.crashreport.BuglyLog
import com.tencent.bugly.crashreport.CrashReport

import net.gtr.framework.app.BaseApp

import android.os.Build.SERIAL

/**
 * @author caroline
 * @date 2018/6/15
 */

internal class App : BaseApp() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun initApk() {
        initBugly()
    }

    /**
     * 初始化BuglyLog
     */
    private fun initBugly() {
        BuglyLog.setCache(30 * 1024)
        Bugly.init(applicationContext, "6e8b9f15a4", true)
        val context = applicationContext
        val packageName = context.packageName
        val processName = BaseApp.processName
        // 设置是否为上报进程
        val strategy = CrashReport.UserStrategy(context)
        strategy.isUploadProcess = processName == null || processName == packageName
        CrashReport.setUserId(SERIAL.toUpperCase())
        Beta.autoInit = true
        Beta.initDelay = (1 * 1000).toLong()
        Beta.defaultBannerId = R.mipmap.ic_app
        Beta.smallIconId = R.mipmap.ic_app
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: App
    }
}
