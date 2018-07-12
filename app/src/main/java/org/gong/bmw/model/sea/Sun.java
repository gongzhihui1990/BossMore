package org.gong.bmw.model.sea;

import net.gtr.framework.util.Loger;

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
        float x1 = (float) (r * Math.cos(getAngel()));
        return x - x1;
    }

    public float getCy() {
        float y1 = (float) (r * Math.sin(getAngel()));
        return y - y1;
    }

}
