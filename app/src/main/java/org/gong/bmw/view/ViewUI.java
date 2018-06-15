package org.gong.bmw.view;

import android.support.annotation.LayoutRes;

/**
 * UI is
 * Created by caroline on 2018/3/2.
 */

interface ViewUI {

    /**
     * @return 布局文件资源
     */
    @LayoutRes
    int getLayout();

    /**
     * call after view layout create
     * like {@link android.app.Activity#setContentView(int)} .etc
     */
    void onViewLayout();
}
