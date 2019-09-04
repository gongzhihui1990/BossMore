package org.gong.bmw.model.sea;

/**
 * Created by caroline on 2018/7/11.
 * 天体
 */

public abstract class Aster {
    protected float x;
    protected float y;
    protected float r;

    public Aster(int mCanvasWith, int mCanvasHigh) {
        x = (float) (mCanvasWith * 0.5);
        y = (float) (mCanvasHigh * 0.7);
        r = (float) (mCanvasHigh * 0.75);
    }

    protected final double getAngel() {
        float angel = 360 - 360 * GameTimer.Companion.getInstance().getPercent() % 360;
        angel += getCircumference();
        return Math.PI * angel / 180;
    }

    abstract float getCircumference();
}
