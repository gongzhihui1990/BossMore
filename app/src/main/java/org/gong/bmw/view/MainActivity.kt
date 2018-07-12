package org.gong.bmw.view

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.layout_main.*
import org.gong.bmw.R

/**
 *
 * @author caroline
 * @date 2018/6/15
 */

class MainActivity : BaseActivity() {
    override val layoutR: Int
        get() = R.layout.layout_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ag = Math.PI * 210 / 180
        tvMain.text = String.format("%s %s", "欢迎使用\n" +"角度"+ag+"\ncos 210="+ format(Math.cos(ag)) + "\nsin 210=" + format(Math.sin(ag)), getString(R.string.my_app_name))
        btnStart.setOnClickListener({ startActivity(Intent(this@MainActivity, PlayActivity::class.java)) })
        btnQuit.setOnClickListener({ this@MainActivity.finish() })
    }

    fun format(number: Number):String{
        return String.format("%.3f",number)
    }
}
