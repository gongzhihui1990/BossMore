/*
 * Copyright (c) 2017. heisenberg.gong
 */

package net.gtr.framework.rx;

import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;

import net.gtr.framework.util.Loger;

import org.reactivestreams.Subscriber;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 线程切换工具类
 *
 * @author heisenberg
 * @date 2017/6/23
 */

public final class RxHelper {


    private static Map<Integer, ThrottleFirstInstance> instance = new HashMap<>();

    /**
     * bind Observable io
     */
    private static <T> Observable<T> bindSameUI(@android.support.annotation.NonNull Observable<T> observable) {
        return observable.subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).onTerminateDetach();
    }

    /**
     * bind Observable io
     */
    private static <T> Observable<T> bindNewThread(@android.support.annotation.NonNull Observable<T> observable) {
        return observable.subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).onTerminateDetach();
    }

    /**
     * bind Flowable io
     */
    private static <T> Flowable<T> bindNewThread(@android.support.annotation.NonNull Flowable<T> flowable) {
        return flowable.subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).onTerminateDetach();
    }

    /**
     * bind Observable io
     */
    private static <T> Observable<T> bindUI(@android.support.annotation.NonNull Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).onTerminateDetach();
    }

    /**
     * bind Flowable io
     */
    private static <T> Flowable<T> bindUI(@android.support.annotation.NonNull Flowable<T> flowable) {
        return flowable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).onTerminateDetach();
    }

    /**
     * 调用此方法：
     * 将被观察者给观察者于IO线程观察
     * 防止遗忘onTerminateDetach
     * 并简化代码
     *
     * @param observable the observable
     * @param <T>        the input type
     */
    public static <T> void bindOnNull(@android.support.annotation.NonNull Observable<T> observable) {
        bindNewThread(observable).subscribe(new ProgressObserverImplementation<T>());
    }

    /**
     * 调用此方法：
     * 将被观察者给观察者于IO线程观察
     * 防止遗忘onTerminateDetach
     * 并简化代码
     *
     * @param flowable the flowable
     * @param <T>      the input type
     */
    public static <T> void bindOnNull(@android.support.annotation.NonNull Flowable<T> flowable) {
        bindNewThread(flowable).subscribe(new ProgressObserverImplementation<T>());
    }

    /**
     * 调用此方法：
     * 将被观察者给观察者于UI线程观察
     * 防止遗忘onTerminateDetach
     * 并简化代码
     *
     * @param observable the observable
     * @param observer   the observer
     * @param <T>        the input type
     */
    public static <T> void bindOnUI(@android.support.annotation.NonNull Observable<T> observable, @android.support.annotation.NonNull Observer<T> observer) {
        bindUI(observable).subscribe(observer);
    }

    /**
     * 调用此方法：
     * 将被观察者给观察者于UI线程观察
     * 防止遗忘onTerminateDetach
     * 并简化代码
     *
     * @param observable the observable
     * @param observer   the observer
     * @param <T>        the input type
     */
    public static <T> void bindSameUI(@android.support.annotation.NonNull Observable<T> observable, @android.support.annotation.NonNull Observer<T> observer) {
        bindSameUI(observable).subscribe(observer);
    }

    /**
     * 调用此方法
     * 将被观察者给观察者于UI线程观察
     * 防止遗忘onTerminateDetach
     * 并简化代码
     *
     * @param <T>        the input type
     * @param flowable   the flowable
     * @param subscriber the subscriber
     */
    public static <T> void bindOnUI(@android.support.annotation.NonNull Flowable<T> flowable, @android.support.annotation.NonNull Subscriber<T> subscriber) {
        bindUI(flowable).subscribe(subscriber);
    }

    /**
     * 调用此方法
     * 按钮点击1秒内只能执行一次，防止连续点击
     *
     * @param view       view on click
     * @param observable the observable
     * @param observer   the observer
     * @param <T>        the input type
     */
    public static <T> void onClickOne(View view, final Observable<T> observable, final Observer<T> observer) {
        RxView.clicks(view).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                bindOnUI(observable, observer);
            }
        });
    }

    /**
     * 调用此方法
     * 按钮点击1秒内只能执行一次，防止连续点击
     *
     * @param view            view on click
     * @param onClickListener the onClickListener
     */
    public static void onClickOne(final View view, final View.OnClickListener onClickListener) {
        RxView.clicks(view).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                onClickListener.onClick(view);
            }
        });
    }

    /**
     * 调用此方法
     * 按钮点击1秒内只能执行一次，防止连续点击
     *
     * @param view       view on click
     * @param flowable   the flowable
     * @param subscriber the subscriber
     * @param <T>        the input type
     */
    public static <T> void onClickOne(View view, final Flowable<T> flowable, final Subscriber<T> subscriber) {
        RxView.clicks(view).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                bindOnUI(flowable, subscriber);
            }
        });
    }

    /**
     * 倒计时
     *
     * @param time
     * @return
     */
    public static Observable<Integer> countdown(int time, long period, TimeUnit unit) {
        if (time < 0) {
            time = 0;
        }
        final int countTime = time;
        return Observable.interval(0, period, unit).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(new Function<Long, Integer>() {
            //通过map()将0、1、2、3...的计数变为...3、2、1、0倒计时
            @Override
            public Integer apply(@NonNull Long increaseTime) throws Exception {
                return countTime - increaseTime.intValue();
            }
            //通过take()取>=0的Observable
        }).take(countTime + 1);
    }

    /**
     * 倒计时
     *
     * @param time
     * @return
     */
    public static Observable<Runnable> delay(int time, TimeUnit unit, Runnable delay) {
        return Observable.just(delay).delay(time, unit);

    }

    private static <T> Observable<T> bindSameUINotSchedule(@android.support.annotation.NonNull Observable<T> observable) {
        return observable.onTerminateDetach();
    }

    /**
     * 调用此方法：
     * 将被观察者给观察者于UI线程观察,不切换线程进度，使用默认线程
     * 防止遗忘onTerminateDetach
     * 并简化代码
     *
     * @param observable the observable
     * @param observer   the observer
     * @param <T>        the input type
     */
    public static <T> void bindSameUINotSchedule(@android.support.annotation.NonNull Observable<T> observable, @android.support.annotation.NonNull Observer<T> observer) {
        bindSameUINotSchedule(observable).subscribe(observer);
    }

    /**
     * @param task   相同的task应该用相同的taskId
     * @param taskId 重复的操作必须相同
     */
    public static void acceptThrottle(Runnable task, final int taskId) {
        if (instance.get(taskId) == null) {
            instance.put(taskId, new ThrottleFirstInstance(task));
        }
        instance.get(taskId).apply(taskId);
    }

    private static class ThrottleFirstInstance {
        private PostCenter messageCenter = null;

        private Runnable param;

        ThrottleFirstInstance(Runnable t) {
            this.param = t;
        }

        private void apply(int taskId) {
            Loger.i("taskId " + taskId);
            if (messageCenter == null) {
                messageCenter = new PostCenter();
                Observable<Runnable> messageObservable = Observable.create(new MessageOnSubscribe(messageCenter, param, taskId));
                bindOnUI(messageObservable.throttleWithTimeout(2, TimeUnit.SECONDS), new ProgressObserverImplementation<Runnable>() {
                    @Override
                    public void onNext(Runnable runnable) {
                        super.onNext(runnable);
                        Loger.e("instance run");
                        runnable.run();
                    }
                });
            } else {
                messageCenter.doPost(param);

            }
        }

        private interface onReceiveListener {

            void onReceive(Runnable param);

        }

        private class PostCenter {
            onReceiveListener listener;

            void setReceiveListener(onReceiveListener listener) {
                this.listener = listener;
            }

            void doPost(Runnable param) {

                if (listener != null) {
                    Loger.i("doPost " + param);
                    listener.onReceive(param);
                } else {
                    Loger.e("listener is null ，delay" + param);
                }
            }
        }

        private final class MessageOnSubscribe implements ObservableOnSubscribe<Runnable> {
            private PostCenter messageCenter;
            private onReceiveListener listener;
            private Runnable runnable;
            private int taskId;

            MessageOnSubscribe(PostCenter messageCenter, Runnable runnable, int taskId) {
                this.messageCenter = messageCenter;
                this.runnable = runnable;
                this.taskId = taskId;
            }

            @Override
            public void subscribe(final ObservableEmitter<Runnable> e) throws Exception {
                if (runnable == null || messageCenter == null || instance.get(taskId) == null) {
                    //防御编程
                    Loger.e("防御成功" + runnable + " " + messageCenter + " " + instance.get(taskId));
                    return;
                }
                listener = param -> {
                    if (!e.isDisposed()) {
                        if (runnable == null || messageCenter == null || instance.get(taskId) == null) {
                            //防御编程
                            Loger.e("内部防御成功" + runnable + " " + messageCenter + " " + instance.get(taskId));
                            return;
                        }
                        e.onNext(param);
                        instance.remove(taskId);
                        Loger.e("instance done and remove " + taskId);
                        runnable = null;
                        e.onComplete();
                    }
                };
                messageCenter.setReceiveListener(listener);
                messageCenter.doPost(runnable);
            }

        }

    }

}
