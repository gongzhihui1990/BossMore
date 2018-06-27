package net.gtr.framework.rx

import org.reactivestreams.Subscription

import io.reactivex.disposables.Disposable

/**
 * Created by caroline on 2018/1/23.
 */

internal class DefaultObserverResourceHolder : ObserverResourceHolder {
    override fun addDisposable(disposable: Disposable?) {

    }

    override fun addSubscription(subscription: Subscription?) {

    }

    override fun removeDisposable(disposable: Disposable?) {

    }

    override fun removeSubscription(subscription: Subscription?) {

    }

    override fun clearWorkOnDestroy() {

    }
}
