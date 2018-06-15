package net.gtr.framework.rx;

import android.support.annotation.Nullable;

import org.reactivestreams.Subscription;

import io.reactivex.disposables.Disposable;

/**
 * Created by caroline on 2018/1/23.
 */

class DefaultObserverResourceHolder implements ObserverResourceHolder {
    @Override
    public void addDisposable(@Nullable Disposable disposable) {

    }

    @Override
    public void addSubscription(@Nullable Subscription subscription) {

    }

    @Override
    public void removeDisposable(@Nullable Disposable disposable) {

    }

    @Override
    public void removeSubscription(@Nullable Subscription subscription) {

    }

    @Override
    public void clearWorkOnDestroy() {

    }
}
