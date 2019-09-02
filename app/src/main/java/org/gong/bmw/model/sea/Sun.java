package org.gong.bmw.model.sea;

/**
 * Created by caroline on 2018/7/11.
 */

public class Sun extends Aster {


    private float cr;

    public Sun(int mCanvasWith, int mCanvasHigh) {
        super(mCanvasWith, mCanvasHigh);
        cr = (float) (mCanvasWith * 0.025);
    }

    @Override
    float getCircumference() {
        return 180;
    }


    public float getCr() {
        return cr;
    }


    public float getCx() {
        return x - (float) (r * Math.cos(getAngel()));
    }

    public float getCy() {
        return y - (float) (r * Math.sin(getAngel()));
    }

}
