/*
 * Copyright (c) 2017. heisenberg.gong
 */

package net.gtr.framework.rx

import android.os.Parcel
import android.os.Parcelable
import android.text.SpannableString
import android.view.View
import android.view.View.OnClickListener

import java.io.Serializable

/**
 * 对话框展示参数
 * heisenberg
 */
class Actions4SimpleDlg : Serializable, Parcelable {
    @Transient
    var cancelable = true
    @Transient
    var layoutView: View? = null
    @Transient
    var title: String? = null
    @Transient
    var message: String? = null
    @Transient
    var textColorRes = android.R.color.black
    @Transient
    var confirmListener: OnClickListener? = null
    @Transient
    var cancelListener: OnClickListener? = null
    @Transient
    var moreListener: OnClickListener? = null
    @Transient
    var confirmBtnText: String? = null
    @Transient
    var cancelBtnText: String? = null
    @Transient
    var moreText: String? = null
    @Transient
    var isNeedCanceBtn: String? = null
    @Transient
    var spannableString: SpannableString? = null

    constructor() {

    }

    constructor(`in`: Parcel) {

    }

    override fun describeContents(): Int {
        // TODO Auto-generated method stub
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        // TODO Auto-generated method stub

    }

    companion object {
        private const val serialVersionUID = 3889542938169910637L

        val CREATOR: Parcelable.Creator<Actions4SimpleDlg> = object : Parcelable.Creator<Actions4SimpleDlg> {
            override fun newArray(size: Int): Array<Actions4SimpleDlg?> {
                // TODO Auto-generated method stub
                return arrayOfNulls(size)
            }

            override fun createFromParcel(source: Parcel): Actions4SimpleDlg {
                // TODO Auto-generated method stub
                return Actions4SimpleDlg(source)
            }
        }
    }
}