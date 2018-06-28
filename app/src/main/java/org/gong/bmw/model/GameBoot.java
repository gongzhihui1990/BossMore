package org.gong.bmw.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.SurfaceHolder;

import org.gong.bmw.control.BootController;

/**
 * @author caroline
 * @date 2018/6/27
 */

public interface GameBoot extends BootController{
    /**
     * 初始化
     *
     */
    void initBy(Context context);

    /**
     * 返回view的bitmap
     */

     Bitmap getBitmap();

}
