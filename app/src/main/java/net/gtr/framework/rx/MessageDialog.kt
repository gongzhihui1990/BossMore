/*
 * Copyright (c) 2017. heisenberg.gong
 */

package net.gtr.framework.rx

import android.content.Context
import android.view.View

/**
 * Created by heisenberg on 2017/10/20.
 * heisenberg.gong@koolpos.com
 */

interface MessageDialog {


    val isDialogShowing: Boolean

    fun setCancelable(mCancelable: Boolean)

    fun setDialogMessage(mMessage: CharSequence)

    fun setDialogTitle(mTitle: CharSequence)

    fun setConfirmButtonText(mConfirm: CharSequence)

    fun setConfirmButtonOnClickListener(onClickListener: View.OnClickListener)

    fun setCancelButtonText(mCancel: CharSequence)

    fun show()

    fun dismiss()

}
