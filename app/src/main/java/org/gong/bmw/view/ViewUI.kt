package org.gong.bmw.view

import android.support.annotation.LayoutRes

/**
 * UI is
 * Created by caroline on 2018/3/2.
 */

internal interface ViewUI {

    /**
     * @return 布局文件资源
     */
    @get:LayoutRes
    val layoutR: Int

    /**
     * call after view layoutR create
     * like [android.app.Activity.setContentView] .etc
     */
    fun onViewLayout()
}
