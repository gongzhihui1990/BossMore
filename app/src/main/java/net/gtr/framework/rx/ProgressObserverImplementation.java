/*
 * Copyright (c) 2017. heisenberg.gong
 */

package net.gtr.framework.rx;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import net.gtr.framework.app.BaseApp;
import net.gtr.framework.exception.IServerException;
import net.gtr.framework.exception.IgnoreShow;
import net.gtr.framework.util.Loger;

import org.gong.bmw.R;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.annotations.NonNull;

import static java.net.HttpURLConnection.HTTP_BAD_GATEWAY;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_GATEWAY_TIMEOUT;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static java.net.HttpURLConnection.HTTP_UNAVAILABLE;

/**
 * AbstractProgressObserver 的基础实现
 *
 * @author heisenberg
 * @date 2017/4/18
 */
public class ProgressObserverImplementation<T> extends AbstractProgressResourceSubscriber<T> {

    private static ObserverResourceHolder defaultHolder;
    private MaterialDialog msgDialog;
    private MaterialDialog.Builder msgDialogBuilder;
    private LoadingDialog loadingDialog;
    private boolean mCancelable;
    private boolean mShow = true;
    private CharSequence pMessage;
    private Context context;

    /**
     * @param holder ApplicationObserverResourceHolder
     */
    public ProgressObserverImplementation(@NonNull ApplicationObserverResourceHolder holder) {
        if (holder != null) {
            setObserverHolder(holder);
            context = holder.getContext();
            if (Looper.myLooper() == Looper.getMainLooper()) {
                // UI主线程
                View view = View.inflate(holder.getContext(), R.layout.dialog_waiting, null);
                loadingDialog = new CustomDialog(holder.getContext(), view, R.style.dialog);
                loadingDialog.setTitle("提示");
                pMessage = "加载中...";
                loadingDialog.setCancelable(mCancelable);
            }
        }
    }

    /**
     * @deprecated 无applicationObserverHolder 不建议
     */
    public ProgressObserverImplementation() {
        if (defaultHolder == null) {
            defaultHolder = new DefaultObserverResourceHolder();
        }
        setObserverHolder(defaultHolder);
    }

    @Override
    public void onError(Throwable t) {
        super.onError(t);
        showError(t);
    }

    private void showError(Throwable t) {
        if (t instanceof IgnoreShow) {
            return;
        }
        t.printStackTrace();
        if (getContext() == null) {
            return;
        }
        setDialogConfirmBtn(getContext().getText(R.string.sure), null);
        String parsedError = parseException(t);
        if (t instanceof ConnectException) {
            String message = "无法连上服务器,请检查您的网络和设置";
            if (checkDialog()) {
                msgDialog = msgDialogBuilder.title("提示").content(message).onPositive((dialog, which) -> {
                    try {
                        getContext().startActivity(new Intent(Settings.ACTION_SETTINGS));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).positiveText(getContext().getString(R.string.system_set)).show();
            }
        } else {
            showDialogByMessage(parsedError);
        }
        onParsedError(parsedError);
    }

    public void onParsedError(String msg) {
        Loger.e(msg);
    }

    protected String parseException(Throwable e) {
        String errMsg;
        if (e instanceof HttpException) {
            String httpErrorMsg = ((HttpException) e).message();
            int httpCode = ((HttpException) e).code();
            Loger.d("onError:" + httpErrorMsg);
            switch (httpCode) {
                case HTTP_BAD_REQUEST:
                    httpErrorMsg = "400-(错误请求）";
                    break;
                case HTTP_UNAUTHORIZED:
                    httpErrorMsg = "401-(未授权）";
                    break;
                case HTTP_FORBIDDEN:
                    httpErrorMsg = "403-(服务器拒绝访问）";
                    break;
                case HTTP_NOT_FOUND:
                    httpErrorMsg = "404-(服务器未找到响应）";
                    break;
                case HTTP_INTERNAL_ERROR:
                    httpErrorMsg = "500-(服务器内部错误）";
                    break;
                case HTTP_BAD_GATEWAY:
                    httpErrorMsg = "502-(错误网关）";
                    break;
                case HTTP_UNAVAILABLE:
                    httpErrorMsg = "503-(服务不可用）";
                    break;
                case HTTP_GATEWAY_TIMEOUT:
                    httpErrorMsg = "504-(网关连接超时）";
                    break;
                default:
                    httpErrorMsg = httpCode + "-(" + httpErrorMsg + "）";
                    break;
            }
            errMsg = "数据加载失败(≧Д≦)ノ，" + httpErrorMsg;
        } else if (e instanceof UnknownHostException) {
            errMsg = "服务器DNS解析失败,请检查您的网络";
        } else if (e instanceof ConnectException) {
            errMsg = "无法连上服务器,请检查您的网络";
        } else if (e instanceof SocketTimeoutException) {
            errMsg = "网络连接超时,请检查您的网络";
        } else if (e instanceof SocketException) {
            errMsg = "网络连接超时,请检查您的网络";
        } else if (e instanceof IServerException) {
            errMsg = e.getMessage();
        } else {
            errMsg = e.getMessage();
        }
        if (TextUtils.isEmpty(errMsg)) {
            errMsg = "未知错误" + e.toString();
        }
        return errMsg;
    }


    /**
     * Sets whether this dialog is cancelable with the
     * {@link KeyEvent#KEYCODE_BACK BACK} key.
     */
    public ProgressObserverImplementation<T> setCancelable(boolean flag) {
        mCancelable = flag;
        return this;
    }

    public ProgressObserverImplementation<T> setMessage(CharSequence message) {
        pMessage = message;
        return this;
    }

    public ProgressObserverImplementation<T> setMessageWithSymbol(CharSequence message) {
        pMessage = message + getContext().getString(R.string.progress_symbol);
        return this;
    }

    public ProgressObserverImplementation<T> setMessageWithSymbol(@StringRes int message) {
        pMessage = getContext().getString(message) + getContext().getString(R.string.progress_symbol);
        return this;
    }

    public ProgressObserverImplementation<T> setMessage(@StringRes int messageID) {
        pMessage = BaseApp.getContext().getString(messageID);
        return this;
    }

    public ProgressObserverImplementation<T> setShow(boolean show) {
        mShow = show;
        return this;
    }

    private boolean checkDialog() {
        if (getContext() == null) {
            return false;
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return false;
        }
        if (msgDialog != null && msgDialog.isShowing()) {
            try {
                msgDialog.dismiss();
            } catch (Exception e) {
                //底层比较渣
                Loger.e("底层比较渣" + e.getClass());
            }
        } else {
            msgDialogBuilder = new MaterialDialog.Builder(getContext());
        }
        return true;

    }

    public void setDialogConfirmBtn(CharSequence btnText, final View.OnClickListener onClickListener) {
        if (checkDialog()) {
            msgDialogBuilder.positiveText(btnText).onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@android.support.annotation.NonNull MaterialDialog dialog, @android.support.annotation.NonNull DialogAction which) {
                    if (onClickListener != null) {
                        onClickListener.onClick(null);
                    }
                    msgDialog.dismiss();
                }
            });
        }
    }

    public void showDialogByMessage(CharSequence message) {
        if (checkDialog()) {
            msgDialog = msgDialogBuilder.title("提示").content(message).show();
        }
    }

    @Override
    protected void showProgress() {
        if (loadingDialog != null && mShow) {
            loadingDialog.setCancelable(mCancelable);
            loadingDialog.setMessage(pMessage);
            loadingDialog.show();
            return;
        }
        if (!mShow) {
            dismissProgress();
        }
    }

    @Override
    protected void dismissProgress() {
        if (loadingDialog != null) {
            try {
                loadingDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Context getContext() {
        return context;
    }
}
