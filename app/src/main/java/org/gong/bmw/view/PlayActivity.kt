package org.gong.bmw.view

import android.os.Bundle

import net.gtr.framework.util.Loger

/**
 *
 * @author caroline
 * @date 2018/6/27
 */

class PlayActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (this is PlayActivity) {
            Loger.i("this instanceof PlayActivity")
        }
        if (PlayActivity::class.java.isAssignableFrom(this.javaClass)) {
            Loger.i("PlayActivity.class.isAssignableFrom(this.getClass())")
        }
    }
}
