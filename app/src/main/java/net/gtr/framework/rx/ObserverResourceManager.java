package net.gtr.framework.rx;

import android.support.annotation.Nullable;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 *
 * @author heisenberg
 * @date 2018/1/15
 */

public class ObserverResourceManager implements ObserverResourceHolder {
    private List<Subscription> compositeSubscription = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null || disposable == null) {
            return;
        }
        compositeDisposable.add(disposable);
    }

    @Override
    public void addSubscription(@Nullable Subscription subscription) {
        if (compositeSubscription == null || subscription == null) {
            return;
        }
        compositeSubscription.add(subscription);
    }

    @Override
    public void removeDisposable(@Nullable Disposable disposable) {
        if (compositeDisposable == null || disposable == null) {
            return;
        }
        compositeDisposable.remove(disposable);
    }

    @Override
    public void removeSubscription(@Nullable Subscription subscription) {
        if (compositeSubscription == null || subscription == null) {
            return;
        }
        compositeSubscription.remove(subscription);
    }

    @Override
    public void clearWorkOnDestroy() {
        //disposable clear
        compositeDisposable.clear();
        //subscription clear
        for (Subscription subscription : compositeSubscription) {
            if (subscription != null) {
                subscription.cancel();
            }
            subscription = null;
        }
        compositeSubscription.clear();
    }

}
