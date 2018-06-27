package net.gtr.framework.rx


import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.gong.bmw.R
import java.util.concurrent.TimeUnit

/**
 * 自定义圆角的dialog
 *
 * @author caroline
 */
class CustomDialog(context: Context, layout: View, style: Int) : Dialog(context, style), LoadingDialog {


    internal var textContent = ""
    private val messageView: TextView?
    private var disposable: Disposable? = null

    init {
        setContentView(layout)
        val window = window
        val params = window!!.attributes
        params.gravity = Gravity.CENTER
        window.attributes = params
        messageView = layout.findViewById(R.id.tvTip)
    }

    override fun isDialogShowing(): Boolean {
        return super.isShowing()
    }

    override fun show() {
        super.show()
        RxHelper.bindOnUI(RxHelper.countdown(25000, (250 * 4).toLong(), TimeUnit.MILLISECONDS), object : Observer<Long> {
            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(integer: Long) {
                val str = StringBuilder()
                (integer % 4..3)
                        .takeWhile { it != 3L }
                        .forEach { str.append(".") }
                val loadingMsg = textContent + str
                messageView!!.text = loadingMsg

            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        })
    }

    override fun dismiss() {
        super.dismiss()
        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
        }
    }

    /**
     * 获取显示密度
     *
     * @param context
     * @return
     */
    private fun getDensity(context: Context): Float {
        val res = context.resources
        val dm = res.displayMetrics
        return dm.density
    }

    override fun setMessage(mMessage: CharSequence) {
        if (messageView != null) {
            textContent = mMessage.toString()
            messageView.text = mMessage
        }
    }
}