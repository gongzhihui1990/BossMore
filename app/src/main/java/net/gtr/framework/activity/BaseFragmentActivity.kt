/*
 * Copyright (c) 2017. heisenberg.gong
 */

package net.gtr.framework.activity

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.util.Log

/**
 * Created by heisenberg on 2017/8/29.
 */

@Deprecated("")
internal class BaseFragmentActivity : AppCompatActivity() {

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        val fm = supportFragmentManager
        var index = requestCode shr 16
        if (index != 0) {
            index--
            if (fm.fragments == null || index < 0
                    || index >= fm.fragments.size) {
                Log.w(TAG, "Activity result fragment index out of range: 0x" + Integer.toHexString(requestCode))
                return
            }
            val frag = fm.fragments[index]
            if (frag == null) {
                Log.w(TAG, "Activity result no fragment exists for index: 0x" + Integer.toHexString(requestCode))
            } else {
                handleResult(frag, requestCode, resultCode, data)
            }
        }

    }

    /**
     * 递归调用，对所有子Fragement生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private fun handleResult(frag: Fragment, requestCode: Int, resultCode: Int,
                             data: Intent) {
        frag.onActivityResult(requestCode and 0xffff, resultCode, data)
        val frags = frag.childFragmentManager.fragments
        if (frags != null) {
            for (f in frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data)
            }
        }
    }

    companion object {
        private val TAG = "BaseFragmentActivity"
    }
}