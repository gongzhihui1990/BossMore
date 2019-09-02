package org.gong.bmw.view

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.layout_main.*
import org.gong.bmw.R
import java.util.*

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
        tvMain.text = String.format("%s %s", "欢迎使用", getString(R.string.my_app_name))
        btnStart.setOnClickListener({ startActivity(Intent(this@MainActivity, PlayActivity::class.java)) })
        btnConnect.setOnClickListener({ startActivity(Intent(this@MainActivity, BLEActivity::class.java)) })
        btnQuit.setOnClickListener({ this@MainActivity.finish() })
        btnSend.setOnClickListener({
            val intent = Intent("com.xilai.express.delivery")
            intent.flags=Intent.FLAG_INCLUDE_STOPPED_PACKAGES
            intent.putExtra("data", "from " + packageName + "." + Date().toGMTString())
            sendBroadcast(intent)
        })
    }

    fun format(number: Number): String {
        return String.format("%.3f", number)
    }
}
