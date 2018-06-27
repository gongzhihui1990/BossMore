/*
 * Copyright (c) 2017. heisenberg.gong
 */

package net.gtr.framework.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity

import net.gtr.framework.rx.ApplicationObserverResourceHolder
import net.gtr.framework.rx.ObserverResourceManager
import net.gtr.framework.util.Loger

import org.reactivestreams.Subscription

import io.reactivex.disposables.Disposable

/**
 * @author caroline
 */
abstract class RxAppCompatActivity : AppCompatActivity(), ApplicationObserverResourceHolder {

    /**
     * observer 观察者管理
     */
    private var observerResourceManager = ObserverResourceManager()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Loger.w("-" + this)
    }

    override fun onPostResume() {
        super.onPostResume()
        Loger.w("-" + this)
    }

    override fun onRestart() {
        super.onRestart()
        Loger.w("-" + this)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        Loger.w("-" + this)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        Loger.w("-" + this)
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        Loger.w("-" + this)
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        Loger.w("-" + this)
    }

    @CallSuper
    override fun onDestroy() {
        clearWorkOnDestroy()
        super.onDestroy()
        Loger.w("-" + this)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Loger.w("-" + this)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Loger.w("-" + this)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle, persistentState: PersistableBundle) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        Loger.w("-" + this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Loger.w("-" + this)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        Loger.w("-" + this)
    }

    @CallSuper
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Loger.w("-" + this)
    }

    /**
     * 清除FragmentManager内的Fragment层级到指定个数
     *
     * @param popLevel popLevel
     */

    protected fun popFragmentToLevel(popLevel: Int) {
        val manager = supportFragmentManager
        while (manager.backStackEntryCount >= popLevel) {
            manager.popBackStackImmediate()
        }
    }

    /**
     * onDestroy时调用此方法
     * 切断此Activity中的观察者容器中包含的观察者
     */
    override fun clearWorkOnDestroy() {
        observerResourceManager.clearWorkOnDestroy()
    }

    /**
     * 添加disposable到Activity生命周期，Activity销毁时候，disposable执行dispose
     *
     * @param disposable disposable
     */
    override fun addDisposable(disposable: Disposable?) {
        observerResourceManager.addDisposable(disposable)
    }

    /**
     * 类似 addSubscription(Disposable disposable)
     *
     * @param subscription subscription
     */
    override fun addSubscription(subscription: Subscription?) {
        observerResourceManager.addSubscription(subscription)
    }

    override fun removeDisposable(disposable: Disposable?) {
        observerResourceManager.removeDisposable(disposable)
    }

    override fun removeSubscription(subscription: Subscription?) {
        observerResourceManager.removeSubscription(subscription)
    }

    override fun getContext(): Context {
        return this
    }


}