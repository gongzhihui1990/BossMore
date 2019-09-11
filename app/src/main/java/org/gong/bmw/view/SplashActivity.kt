package org.gong.bmw.view

import android.content.Intent
import com.jaeger.library.StatusBarUtil
import io.reactivex.Observable
import io.reactivex.functions.Function3
import kotlinx.android.synthetic.main.activity_splash.*
import net.gtr.framework.rx.ProgressObserverImplementation
import net.gtr.framework.rx.RxHelper
import net.gtr.framework.util.Loger
import org.gong.bmw.R
import java.util.concurrent.TimeUnit

/**
 * @author caroline
 * @date 2018/6/15
 */
internal class SplashActivity : BaseActivity() {

    private var jump = false

    override val layoutR: Int
        get() = R.layout.activity_splash

    override fun onViewLayout() {
        ivAppIcon.setOnClickListener {
            jump = true
            splashEnd()
        }
        StatusBarUtil.setColor(this, resources.getColor(android.R.color.transparent))
        //同步初始化代码
        val taskInit = Observable.just("应用启动中").map { param ->
            Loger.i(param)
            true
        }
        //蓝牙 电话通讯录 位置 相机、麦克风 存储空间
        val taskPermission = Observable.just(true);//RxPermissions(this).request(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECORD_AUDIO, Manifest.permission.WAKE_LOCK, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_SETTINGS)
        //延迟操作
        val taskDelay = Observable.just(true).delay(500, TimeUnit.MILLISECONDS)
        val zipperSplash = Function3<Boolean, Boolean, Boolean, Boolean> { permission, delay, init -> permission && delay && init }
        val splashObservable = Observable.zip<Boolean, Boolean, Boolean, Boolean>(taskPermission, taskDelay, taskInit, zipperSplash)
        val splashObserver = object : ProgressObserverImplementation<Boolean>(this@SplashActivity) {
            override fun onNext(t: Boolean) {
                super.onNext(t)
                if (!jump) {
                    splashEnd()
                }
            }
        }.setShow(false)
        RxHelper.bindOnUI<Boolean>(splashObservable, splashObserver)
    }

    private fun splashEnd() {
        startActivity(Intent(baseContext, WelcomeActivity::class.java))
        finish()
    }

}
