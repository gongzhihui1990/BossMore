package org.gong.bmw.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.gong.bmw.R;
import org.gong.bmw.control.BootController;

/**
 * @author caroline
 * @date 2018/6/27
 */

public final class MainBoot extends Boot {

    private transient Bitmap imageCache = null;

    public MainBoot(Context context) {
        super(context);
    }

    @Override
    public void initBoot(Context context) {
        super.initBoot(context);
        setPoint(new GamePoint(0.5f, 0.2f));
    }

    @Override
    public BootController getController() {
        return this;
    }

    @Override
    void onMoved(boolean moved) {
    }


    @Override
    public Bitmap getBitmap() {
        if (imageCache == null) {
            imageCache = BitmapFactory.decodeResource(context.getResources(), R.mipmap.game_boot);
        }
        return imageCache;
    }


}
