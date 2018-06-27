/*
 * Copyright (c) 2017. heisenberg.gong
 */

package net.gtr.framework.rx

import org.reactivestreams.Subscription

import io.reactivex.disposables.Disposable

/**
 * Created by heisenberg on 2017/7/21.
 * heisenberg.gong@koolpos.com
 * use to add/remove resource :
 * [Disposable] or [Subscription]
 */

interface ObserverResourceHolder {
    /**
     * 为容器添加disposable resource
     *
     * @param disposable
     */
    fun addDisposable(disposable: Disposable?)

    /**
     * 为容器添加subscription resource
     *
     * @param subscription
     */
    fun addSubscription(subscription: Subscription?)

    /**
     * 为容器移除disposable resource
     *
     * @param disposable
     */
    fun removeDisposable(disposable: Disposable?)

    /**
     * 为容器移除subscription resource
     *
     * @param subscription
     */
    fun removeSubscription(subscription: Subscription?)

    /**
     * 为容器移除所有resource
     *
     */
    fun clearWorkOnDestroy()
}
