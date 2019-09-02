package org.gong.bmw.model;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @author caroline
 * @date 2018/6/28
 */

public abstract class GameItemDrawView extends GameItemView {
      public abstract void draw(Canvas canvas, Paint paint);
}
