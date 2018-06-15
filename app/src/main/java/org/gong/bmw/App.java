package org.gong.bmw;

import android.annotation.SuppressLint;
import android.content.Context;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.BuglyLog;
import com.tencent.bugly.crashreport.CrashReport;

import net.gtr.framework.app.BaseApp;

import static android.os.Build.SERIAL;

/**
 * @author caroline
 * @date 2018/6/15
 */

public class App extends BaseApp {

    @SuppressLint("StaticFieldLeak")
    private static App app;

    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        app = this;
        super.onCreate();
    }

    @Override
    public void initApk() {
        initBugly();
    }

    /**
     * 初始化BuglyLog
     */
    private void initBugly() {
        BuglyLog.setCache(30 * 1024);
        Bugly.init(getApplicationContext(), "6e8b9f15a4", true);
        Context context = getApplicationContext();
        String packageName = context.getPackageName();
        String processName = getProcessName();
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        CrashReport.setUserId(SERIAL.toUpperCase());

        Beta.autoInit = true;
        Beta.initDelay = 1 * 1000;
        Beta.defaultBannerId = R.mipmap.ic_app;
        Beta.smallIconId = R.mipmap.ic_app;

    }
}
