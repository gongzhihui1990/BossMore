/*
 * Copyright (c) 2017. heisenberg.gong
 */

package net.gtr.framework.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import net.gtr.framework.rx.ApplicationObserverResourceHolder;
import net.gtr.framework.rx.ObserverResourceManager;
import net.gtr.framework.util.Loger;

import org.reactivestreams.Subscription;

import io.reactivex.disposables.Disposable;

/**
 * @author caroline
 */
public abstract class RxAppCompatActivity extends AppCompatActivity implements ApplicationObserverResourceHolder {

    /**
     * observer 观察者管理
     */
    ObserverResourceManager observerResourceManager = new ObserverResourceManager();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Loger.w("-" + this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Loger.w("-" + this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Loger.w("-" + this);
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        Loger.w("-" + this);
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        Loger.w("-" + this);
    }

    @Override
    @CallSuper
    protected void onPause() {
        super.onPause();
        Loger.w("-" + this);
    }

    @Override
    @CallSuper
    protected void onStop() {
        super.onStop();
        Loger.w("-" + this);
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        clearWorkOnDestroy();
        super.onDestroy();
        Loger.w("-" + this);
    }

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Loger.w("-" + this);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Loger.w("-" + this);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        Loger.w("-" + this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Loger.w("-" + this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Loger.w("-" + this);
    }

    @Override
    @CallSuper
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Loger.w("-" + this);
    }

    /**
     * 清除FragmentManager内的Fragment层级到指定个数
     *
     * @param popLevel popLevel
     */

    protected final void popFragmentToLevel(int popLevel) {
        FragmentManager manager = getSupportFragmentManager();
        while (manager.getBackStackEntryCount() >= popLevel) {
            manager.popBackStackImmediate();
        }
    }

    /**
     * onDestroy时调用此方法
     * 切断此Activity中的观察者容器中包含的观察者
     */
    @Override
    public void clearWorkOnDestroy() {
        observerResourceManager.clearWorkOnDestroy();
    }

    /**
     * 添加disposable到Activity生命周期，Activity销毁时候，disposable执行dispose
     *
     * @param disposable disposable
     */
    @Override
    public void addDisposable(Disposable disposable) {
        observerResourceManager.addDisposable(disposable);
    }

    /**
     * 类似 addSubscription(Disposable disposable)
     *
     * @param subscription subscription
     */
    @Override
    public void addSubscription(Subscription subscription) {
        observerResourceManager.addSubscription(subscription);
    }

    @Override
    public void removeDisposable(Disposable disposable) {
        observerResourceManager.removeDisposable(disposable);
    }

    @Override
    public void removeSubscription(Subscription subscription) {
        observerResourceManager.removeSubscription(subscription);
    }

    @Override
    public Context getContext() {
        return this;
    }


}