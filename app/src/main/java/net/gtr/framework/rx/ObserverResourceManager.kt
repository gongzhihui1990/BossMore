package net.gtr.framework.rx

import org.reactivestreams.Subscription

import java.util.ArrayList

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 *
 * @author heisenberg
 * @date 2018/1/15
 */

class ObserverResourceManager : ObserverResourceHolder {
    private val compositeSubscription = ArrayList<Subscription>()
    private val compositeDisposable = CompositeDisposable()


    override fun addDisposable(disposable: Disposable?) {
        if (disposable == null) {
            return
        }
        compositeDisposable.add(disposable)
    }

    override fun addSubscription(subscription: Subscription?) {
        if (subscription == null) {
            return
        }
        compositeSubscription.add(subscription)
    }

    override fun removeDisposable(disposable: Disposable?) {
        if (disposable == null) {
            return
        }
        compositeDisposable.remove(disposable)
    }

    override fun removeSubscription(subscription: Subscription?) {
        if (subscription == null) {
            return
        }
        compositeSubscription.remove(subscription)
    }

    override fun clearWorkOnDestroy() {
        //disposable clear
        compositeDisposable.clear()
        //subscription clear
        for (subscription in compositeSubscription) {
            subscription.cancel()
        }
        compositeSubscription.clear()
    }

}
