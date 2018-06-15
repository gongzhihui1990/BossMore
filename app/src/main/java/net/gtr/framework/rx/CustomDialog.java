package net.gtr.framework.rx;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import org.gong.bmw.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 自定义圆角的dialog
 *
 * @author caroline
 */
public class CustomDialog extends Dialog implements LoadingDialog {


    String textContent = "";
    private TextView messageView;
    private Disposable disposable;

    public CustomDialog(Context context, View layout, int style) {

        super(context, style);

        setContentView(layout);

        Window window = getWindow();

        WindowManager.LayoutParams params = window.getAttributes();

        params.gravity = Gravity.CENTER;

        window.setAttributes(params);

        messageView = layout.findViewById(R.id.tvTip);

    }

    @Override
    public void show() {
        super.show();
        RxHelper.bindOnUI(RxHelper.countdown(25000, 250 * 4, TimeUnit.MILLISECONDS), new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(Integer integer) {
                StringBuilder str = new StringBuilder();
                for (int len = integer % 4; len <= 3; len++) {
                    if (len == 3) {
                        break;
                    }
                    str.append(".");
                }
                String loadingMsg = textContent + str;
                messageView.setText(loadingMsg);

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * 获取显示密度
     *
     * @param context
     * @return
     */
    private float getDensity(Context context) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        return dm.density;
    }

    @Override
    public void setMessage(CharSequence mMessage) {
        if (messageView != null) {
            textContent = mMessage.toString();
            messageView.setText(mMessage);
        }
    }
}