/*
 * Copyright (c) 2017. heisenberg.gong
 */

package net.gtr.framework.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;

import net.gtr.framework.activity.RxAppCompatActivity;
import net.gtr.framework.rx.ApplicationObserverResourceHolder;
import net.gtr.framework.rx.ObserverResourceManager;

import org.reactivestreams.Subscription;

import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public abstract class RxBaseFragment extends Fragment implements ApplicationObserverResourceHolder {
    protected RxAppCompatActivity mActivity;
    /**
     * use to manage resource
     */
    ObserverResourceManager observerResourceManager = new ObserverResourceManager();

    protected RxAppCompatActivity getBaseActivity() {
        return mActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public final View findViewById(@IdRes int id) {
        if (getView() == null) {
            return null;
        }
        return getView().findViewById(id);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable View container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            ButterKnife.bind(this, container);
        }
        return container;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (RxAppCompatActivity) activity;
    }


    public void addFragment(int layout, Fragment fragment) {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(layout, fragment).commitAllowingStateLoss();
    }

    public void addFragment(int layout, Fragment fragment, boolean isSave) {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(layout, fragment);
        if (isSave) {
            transaction.addToBackStack(null);
        }
        transaction.commitAllowingStateLoss();
    }

    public void addSupportFragment(int layout, Fragment fragment) {
        FragmentManager manager = mActivity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(layout, fragment).commitAllowingStateLoss();
    }

    @Override
    public void onDestroy() {
        clearWorkOnDestroy();
        super.onDestroy();
    }

    /**
     * {@link ObserverResourceManager#clearWorkOnDestroy()}
     */
    @Override
    public void clearWorkOnDestroy() {
        observerResourceManager.clearWorkOnDestroy();
    }

    /**
     * {@link ObserverResourceManager#addDisposable(Disposable)}
     */
    @Override
    public void addDisposable(Disposable disposable) {
        observerResourceManager.addDisposable(disposable);
    }

    /**
     * {@link ObserverResourceManager#addSubscription(Subscription)}
     */
    @Override
    public void addSubscription(Subscription subscription) {
        observerResourceManager.addSubscription(subscription);
    }

    /**
     * {@link ObserverResourceManager#removeDisposable(Disposable)}
     */
    @Override
    public void removeDisposable(Disposable disposable) {
        observerResourceManager.removeDisposable(disposable);
    }

    /**
     * {@link ObserverResourceManager#removeSubscription(Subscription)}
     */
    @Override
    public void removeSubscription(Subscription subscription) {
        observerResourceManager.removeSubscription(subscription);
    }

}
