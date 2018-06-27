package org.gong.bmw.view

import android.os.Bundle
import kotlinx.android.synthetic.main.layout_main.*
import org.gong.bmw.R

/**
 *
 * @author caroline
 * @date 2018/6/15
 */

class MainActivity : BaseActivity(){
    override val layoutR: Int
        get() = R.layout.layout_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvMain.setText(R.string.app_name)
    }
}
