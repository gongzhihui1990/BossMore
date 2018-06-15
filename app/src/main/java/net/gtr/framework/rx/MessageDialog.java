/*
 * Copyright (c) 2017. heisenberg.gong
 */

package net.gtr.framework.rx;

import android.content.Context;
import android.view.View;

/**
 * Created by heisenberg on 2017/10/20.
 * heisenberg.gong@koolpos.com
 */

public interface MessageDialog {


    public void setCancelable(boolean mCancelable);

    public void setDialogMessage(CharSequence mMessage);

    public void setDialogTitle(CharSequence mTitle);

    public void setConfirmButtonText(CharSequence mConfirm);

    public void setConfirmButtonOnClickListener(View.OnClickListener onClickListener);

    public void setCancelButtonText(CharSequence mCancel);


    boolean isShowing();

    public void show();

    public void dismiss();

}
