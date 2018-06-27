/*
 * Copyright (c) 2017. heisenberg.gong
 */

package net.gtr.framework.rx

/**
 * TODO 空的实现类
 * Created by heisenberg on 2017/10/20.
 * heisenberg.gong@koolpos.com
 */

interface LoadingDialog {

    fun isDialogShowing(): Boolean

    fun setCancelable(mCancelable: Boolean)

    fun setMessage(mMessage: CharSequence)

    fun setTitle(mTitle: CharSequence)

    fun show()

    fun dismiss()

}
