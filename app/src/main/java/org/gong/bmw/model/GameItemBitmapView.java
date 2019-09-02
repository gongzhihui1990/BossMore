package org.gong.bmw.model;

import android.graphics.Bitmap;

/**
 * @author caroline
 * @date 2018/6/28
 */

public abstract class GameItemBitmapView extends GameItemView {
    /**
     * 返回view的bitmap
     *
     * @return view的图片
     */
    public abstract Bitmap getBitmap();

}
