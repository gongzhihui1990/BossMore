package net.gtr.framework.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.gtr.framework.activity.RxAppCompatActivity;
import net.gtr.framework.rx.Actions4SimpleDlg;
import net.gtr.framework.rx.DialogObserverHolder;
import net.gtr.framework.rx.ObserverResourceManager;

import org.reactivestreams.Subscription;

import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

/**
 *
 * @author caroline
 * @date 2018/4/24
 */

public abstract class RxBaseDialogFragment extends DialogFragment implements DialogObserverHolder {
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
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

    @Override
    public FragmentManager getSupportFragmentManager() {
        return super.getFragmentManager();
    }

}
