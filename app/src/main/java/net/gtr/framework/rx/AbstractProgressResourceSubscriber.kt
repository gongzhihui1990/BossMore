/*
 * Copyright (c) 2017. heisenberg.gong
 */

package net.gtr.framework.rx


import com.tencent.bugly.crashreport.CrashReport

import net.gtr.framework.util.Loger

import org.gong.bmw.BuildConfig
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by heisenberg on 2017/4/18.
 * 实现了Observer和Subscriber的观察者,执行过程为
 * [.onBegin]、[.onNext]、[.onError] or [.onRelease]
 */

abstract class AbstractProgressResourceSubscriber<T> : Observer<T>, Subscriber<T> {
    private var disposable: Disposable? = null
    private var subscription: Subscription? = null
    private var mHolder: ObserverResourceHolder? = null
    private var requestVar1: Long = 1

    /**
     * 显示进度条对话框
     */
    protected abstract fun showProgress()

    /**
     * 隐藏进度条对话框
     */
    protected abstract fun dismissProgress()

    /**
     * [AbstractProgressResourceSubscriber.onSubscribe]
     * set var1 used for : [Subscription.request] 's param
     *
     * @param var1 请求次数
     * @return AbstractProgressObserver
     */
    fun setSubscribeRequest(var1: Long): AbstractProgressResourceSubscriber<T> {
        requestVar1 = var1
        return this
    }

    override fun onSubscribe(d: Disposable) {
        if (DEBUG_TAG) {
            Loger.d("---")
        }
        disposable = d
        onBeginInternal()
    }

    override fun onSubscribe(s: Subscription) {
        if (DEBUG_TAG) {
            Loger.d("---")
        }
        subscription = s
        s.request(requestVar1)
        onBeginInternal()
    }

    override fun onNext(t: T) {
        if (DEBUG_TAG) {
            Loger.d("---")
        }
    }

    override fun onError(t: Throwable) {
        if (DEBUG_TAG) {
            Loger.d("---")
            t.printStackTrace()
        }
        CrashReport.postCatchedException(t)
        onReleaseInternal()
    }

    override fun onComplete() {
        if (DEBUG_TAG) {
            Loger.d("---")
        }
        onReleaseInternal()
    }

    fun setObserverHolder(observerHolder: ObserverResourceHolder) {
        mHolder = observerHolder
    }

    private fun onBeginInternal() {
        onBegin()
        showProgress()
        if (mHolder == null) {
            return
        }
        if (disposable != null) {
            mHolder!!.addDisposable(disposable)
        }
        if (subscription != null) {
            mHolder!!.addSubscription(subscription)
        }
    }

    private fun onReleaseInternal() {
        onRelease()
        dismissProgress()
        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
        }
        if (subscription != null) {
            subscription!!.cancel()
        }
        if (mHolder == null) {
            return
        }
        if (disposable != null) {
            mHolder!!.removeDisposable(disposable)
        }
        if (subscription != null) {
            mHolder!!.removeSubscription(subscription)
        }
    }

    /**
     * 开始执行任务，可以在此执行想要的操作
     * onSubscribe
     */

    protected fun onBegin() {
        if (DEBUG_TAG) {
            Loger.d("---")
        }
    }

    /**
     * 结束执行任务，可以在结束显示
     * onError onComplete
     */
    protected fun onRelease() {
        if (DEBUG_TAG) {
            Loger.d("---")
        }
    }

    companion object {

        private val DEBUG_TAG = BuildConfig.DEBUG
    }
}
