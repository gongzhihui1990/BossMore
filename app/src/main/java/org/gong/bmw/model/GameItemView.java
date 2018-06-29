package org.gong.bmw.model;

import android.graphics.Bitmap;

/**
 *
 * @author caroline
 * @date 2018/6/28
 */

public interface GameItemView {
    /**
     * 返回view的bitmap
     */
    Bitmap getBitmap();

    /**
     * 返回view的位置
     */
    GamePoint getPoint();
}
