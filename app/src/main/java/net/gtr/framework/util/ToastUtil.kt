package net.gtr.framework.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast

import org.gong.bmw.App
import org.gong.bmw.R


class ToastUtil(internal var context: Context?) {
    private var toast: Toast? = null
    private var msg: String? = null

    fun create(): Toast? {
        val contentView = View.inflate(context, R.layout.dialog_toast, null)
        val tvMsg = contentView.findViewById<TextView>(R.id.tv_toast_msg)
        toast = Toast(context)
        toast!!.view = contentView
        toast!!.setGravity(Gravity.CENTER, 0, 0)
        toast!!.duration = Toast.LENGTH_LONG
        tvMsg.text = msg
        return toast
    }

    fun createShort(): Toast? {
        val contentView = View.inflate(context, R.layout.dialog_toast, null)
        val tvMsg = contentView.findViewById<TextView>(R.id.tv_toast_msg)
        toast = Toast(context)
        toast!!.view = contentView
        toast!!.setGravity(Gravity.CENTER, 0, 0)
        toast!!.duration = Toast.LENGTH_SHORT
        tvMsg.text = msg
        return toast
    }

    fun show() {
        if (toast != null) {
            toast!!.show()
        }
    }

    fun setText(text: String) {
        msg = text
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        internal var td: ToastUtil? = null

        fun show(resId: Int) {
            show(App.instance!!.getString(resId))
        }

        fun show(msg: String) {
            if (td == null) {
                td = ToastUtil(App.instance)
            }
            td!!.setText(msg)
            td!!.create()?.show()
        }

        fun shortShow(msg: String) {
            if (td == null) {
                td = ToastUtil(App.instance)
            }
            td!!.setText(msg)
            td!!.createShort()?.show()
        }
    }
}
