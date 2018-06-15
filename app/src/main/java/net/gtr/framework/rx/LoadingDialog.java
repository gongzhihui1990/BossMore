/*
 * Copyright (c) 2017. heisenberg.gong
 */

package net.gtr.framework.rx;

/**
 * TODO 空的实现类
 * Created by heisenberg on 2017/10/20.
 * heisenberg.gong@koolpos.com
 */

public interface LoadingDialog {

    public void setCancelable(boolean mCancelable);

    public void setMessage(CharSequence mMessage);

    public void setTitle(CharSequence mTitle);

    boolean isShowing();

    public void show();

    public void dismiss();

}
