package org.gong.bmw.view

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.WindowManager
import net.gtr.framework.activity.RxAppCompatActivity
import org.gong.bmw.R

/**
 * @author caroline
 * @date 2018/6/15
 */

abstract class BaseActivity : RxAppCompatActivity(), ViewUI {

    override val layoutR: Int
        get() = R.layout.layout_disable

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        super.onCreate(savedInstanceState)
        setContentView(layoutR)
        onViewLayout()
    }

    override fun onViewLayout() {

    }


}
