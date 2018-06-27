/*
 * Copyright (c) 2017. heisenberg.gong
 */

package net.gtr.framework.rx

import android.annotation.SuppressLint
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.gtr.framework.util.Loger
import org.reactivestreams.Subscriber
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 线程切换工具类
 *
 * @author heisenberg
 * @date 2017/6/23
 */

object RxHelper {


    @SuppressLint("UseSparseArrays")
    private val instance = HashMap<Int, ThrottleFirstInstance>()

    /**
     * bind Observable io
     */
    private fun <T> bindSameUI(observable: Observable<T>): Observable<T> {
        return observable.subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).onTerminateDetach()
    }

    /**
     * bind Observable io
     */
    private fun <T> bindNewThread(observable: Observable<T>): Observable<T> {
        return observable.subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).onTerminateDetach()
    }

    /**
     * bind Flowable io
     */
    private fun <T> bindNewThread(flowable: Flowable<T>): Flowable<T> {
        return flowable.subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).onTerminateDetach()
    }

    /**
     * bind Observable io
     */
    private fun <T> bindUI(observable: Observable<T>): Observable<T> {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).onTerminateDetach()
    }

    /**
     * bind Flowable io
     */
    private fun <T> bindUI(flowable: Flowable<T>): Flowable<T> {
        return flowable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).onTerminateDetach()
    }

    /**
     * 调用此方法：
     * 将被观察者给观察者于IO线程观察
     * 防止遗忘onTerminateDetach
     * 并简化代码
     *
     * @param observable the observable
     * @param <T>        the input type
    </T> */
    fun <T> bindOnNull(observable: Observable<T>) {
        bindNewThread(observable).subscribe(ProgressObserverImplementation())
    }

    /**
     * 调用此方法：
     * 将被观察者给观察者于IO线程观察
     * 防止遗忘onTerminateDetach
     * 并简化代码
     *
     * @param flowable the flowable
     * @param <T>      the input type
    </T> */
    fun <T> bindOnNull(flowable: Flowable<T>) {
        bindNewThread(flowable).subscribe(ProgressObserverImplementation())
    }

    /**
     * 调用此方法：
     * 将被观察者给观察者于UI线程观察
     * 防止遗忘onTerminateDetach
     * 并简化代码
     *
     * @param observable the observable
     * @param observer   the observer
     * @param <T>        the input type
    </T> */
    fun <T> bindOnUI(observable: Observable<T>, observer: Observer<T>) {
        bindUI(observable).subscribe(observer)
    }

    /**
     * 调用此方法：
     * 将被观察者给观察者于UI线程观察
     * 防止遗忘onTerminateDetach
     * 并简化代码
     *
     * @param observable the observable
     * @param observer   the observer
     * @param <T>        the input type
    </T> */
    fun <T> bindSameUI(observable: Observable<T>, observer: Observer<T>) {
        bindSameUI(observable).subscribe(observer)
    }

    /**
     * 调用此方法
     * 将被观察者给观察者于UI线程观察
     * 防止遗忘onTerminateDetach
     * 并简化代码
     *
     * @param <T>        the input type
     * @param flowable   the flowable
     * @param subscriber the subscriber
    </T> */
    fun <T> bindOnUI(flowable: Flowable<T>, subscriber: Subscriber<T>) {
        bindUI(flowable).subscribe(subscriber)
    }

    /**
     * 调用此方法
     * 按钮点击1秒内只能执行一次，防止连续点击
     *
     * @param view       view on click
     * @param observable the observable
     * @param observer   the observer
     * @param <T>        the input type
    </T> */
    fun <T> onClickOne(view: View, observable: Observable<T>, observer: Observer<T>) {
        RxView.clicks(view).throttleFirst(1, TimeUnit.SECONDS).subscribe { bindOnUI(observable, observer) }
    }

    /**
     * 调用此方法
     * 按钮点击1秒内只能执行一次，防止连续点击
     *
     * @param view            view on click
     * @param onClickListener the onClickListener
     */
    fun onClickOne(view: View, onClickListener: View.OnClickListener) {
        RxView.clicks(view).throttleFirst(1, TimeUnit.SECONDS).subscribe { onClickListener.onClick(view) }
    }

    /**
     * 调用此方法
     * 按钮点击1秒内只能执行一次，防止连续点击
     *
     * @param view       view on click
     * @param flowable   the flowable
     * @param subscriber the subscriber
     * @param <T>        the input type
    </T> */
    fun <T> onClickOne(view: View, flowable: Flowable<T>, subscriber: Subscriber<T>) {
        RxView.clicks(view).throttleFirst(1, TimeUnit.SECONDS).subscribe { bindOnUI(flowable, subscriber) }
    }

    /**
     * 倒计时
     *
     * @param time
     * @return
     */
    fun countdown(time: Long, period: Long, unit: TimeUnit): Observable<Long> {
        var time = time
        if (time < 0) {
            time = 0
        }
        val countTime = time
        return Observable.interval(0, period, unit).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
                .map { increaseTime -> countTime - increaseTime }
                .take((countTime + 1))
    }

    /**
     * 倒计时
     *
     * @param time
     * @return
     */
    fun delay(time: Int, unit: TimeUnit, delay: Runnable): Observable<Runnable> {
        return Observable.just(delay).delay(time.toLong(), unit)

    }

    private fun <T> bindSameUINotSchedule(observable: Observable<T>): Observable<T> {
        return observable.onTerminateDetach()
    }

    /**
     * 调用此方法：
     * 将被观察者给观察者于UI线程观察,不切换线程进度，使用默认线程
     * 防止遗忘onTerminateDetach
     * 并简化代码
     *
     * @param observable the observable
     * @param observer   the observer
     * @param <T>        the input type
    </T> */
    fun <T> bindSameUINotSchedule(observable: Observable<T>, observer: Observer<T>) {
        bindSameUINotSchedule(observable).subscribe(observer)
    }

    /**
     * @param task   相同的task应该用相同的taskId
     * @param taskId 重复的操作必须相同
     */
    fun acceptThrottle(task: Runnable, taskId: Int) {
        if (instance[taskId] == null) {
            instance.put(taskId, ThrottleFirstInstance(task))
        }
        instance[taskId]?.apply(taskId)
    }

    private class ThrottleFirstInstance internal constructor(private val param: Runnable) {
        private var messageCenter: PostCenter? = null

        internal fun apply(taskId: Int) {
            Loger.i("taskId " + taskId)
            if (messageCenter == null) {
                messageCenter = PostCenter()
                val messageObservable = Observable.create(MessageOnSubscribe(messageCenter, param, taskId))
                bindOnUI(messageObservable.throttleWithTimeout(2, TimeUnit.SECONDS), object : ProgressObserverImplementation<Runnable>() {
                    override fun onNext(runnable: Runnable) {
                        super.onNext(runnable)
                        Loger.e("instance run")
                        runnable.run()
                    }
                })
            } else {
                messageCenter!!.doPost(param)

            }
        }

        private interface onReceiveListener {

            fun onReceive(param: Runnable?)

        }

        private inner class PostCenter {
            internal var listener: onReceiveListener? = null

            internal fun setReceiveListener(listener: onReceiveListener?) {
                this.listener = listener
            }

            internal fun doPost(param: Runnable?) {
                if (listener != null) {
                    Loger.i("doPost " + param)
                    listener!!.onReceive(param)
                } else {
                    Loger.e("listener is null ，delay" + param)
                }
            }
        }

        private inner class MessageOnSubscribe internal
        constructor(private val messageCenter: PostCenter?, private var runnable: Runnable?,
                    private val taskId: Int?) : ObservableOnSubscribe<Runnable> {
            private var listener: onReceiveListener? = null

            @Throws(Exception::class)
            override fun subscribe(e: ObservableEmitter<Runnable>) {
                if (instance[taskId] == null) {
                    //防御编程
                    Loger.e("防御成功" + runnable + " " + messageCenter + " " + instance[taskId])
                    return
                }
                listener = object : onReceiveListener {
                    override fun onReceive(param: Runnable?) {
                        if (!e.isDisposed) {
                            if (runnable == null || messageCenter == null || instance[taskId] == null) {
                                //防御编程
                                Loger.e("内部防御成功" + runnable + " " + messageCenter + " " + instance[taskId])
                                return
                            }
                            if (param != null) {
                                e.onNext(param)
                            }
                            instance.remove(taskId)
                            Loger.e("instance done and remove " + taskId)
                            runnable = null
                            e.onComplete()
                        }
                    }
                }
                if (messageCenter != null) {
                    messageCenter.setReceiveListener(listener)
                    messageCenter.doPost(runnable)
                }
            }

        }

    }

}
