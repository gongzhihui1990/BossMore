/*
 * Copyright (c) 2017. heisenberg.gong
 */

package net.gtr.framework.rx;

import android.support.annotation.Nullable;

import org.reactivestreams.Subscription;

import io.reactivex.disposables.Disposable;

/**
 * Created by heisenberg on 2017/7/21.
 * heisenberg.gong@koolpos.com
 * use to add/remove resource :
 * {@link Disposable} or {@link Subscription}
 */

public interface ObserverResourceHolder {
    /**
     * 为容器添加disposable resource
     *
     * @param disposable
     */
    void addDisposable(@Nullable Disposable disposable);

    /**
     * 为容器添加subscription resource
     *
     * @param subscription
     */
    void addSubscription(@Nullable Subscription subscription);

    /**
     * 为容器移除disposable resource
     *
     * @param disposable
     */
    void removeDisposable(@Nullable Disposable disposable);

    /**
     * 为容器移除subscription resource
     *
     * @param subscription
     */
    void removeSubscription(@Nullable Subscription subscription);
    /**
     * 为容器移除所有resource
     *
     */
    void clearWorkOnDestroy();
}
