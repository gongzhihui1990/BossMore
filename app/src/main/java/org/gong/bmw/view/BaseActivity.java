package org.gong.bmw.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import net.gtr.framework.activity.RxAppCompatActivity;

import org.gong.bmw.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author caroline
 * @date 2018/6/15
 */

abstract class BaseActivity extends RxAppCompatActivity implements ViewUI {
    protected Unbinder unbinder;

    @Override
    public int getLayout() {
        return R.layout.layout_disable;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Translucent status bar
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        int layoutID = getLayout();
        setContentView(layoutID);
        unbinder = ButterKnife.bind(this);
        onViewLayout();
    }

    @Override
    public void onViewLayout() {

    }


    @Override
    protected void onDestroy() {
        //解绑view控件
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

}
