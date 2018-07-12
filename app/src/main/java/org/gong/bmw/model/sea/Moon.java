package org.gong.bmw.model.sea;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.gong.bmw.App;
import org.gong.bmw.R;

/**
 * Created by caroline on 2018/7/11.
 */

public class Moon extends Aster {
    private transient Bitmap imageCache = null;

    public Moon(int mCanvasWith, int mCanvasHigh) {
        super(mCanvasWith, mCanvasHigh);
    }

    @Override
    float getCircumference() {
        return 0;
    }

    public Bitmap getBitmap() {
        if (imageCache == null) {
            imageCache = BitmapFactory.decodeResource(App.Companion.getInstance().getResources(), R.mipmap.icons8_moon);
        }
        return imageCache;
    }

    public float getX1() {
        float x1 = (float) (r * Math.cos(getAngel()));
        return x1;
    }

    public float getY1() {
        float y1 = (float) (r * Math.sin(getAngel()));
        return y1;
    }

    public float getCx() {
        float x1 = getX1();
        return x - x1;
    }

    public float getCy() {
        float y1 = getY1();
        return y - y1;
    }

}
