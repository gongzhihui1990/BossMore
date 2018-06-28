package org.gong.bmw.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import net.gtr.framework.util.Loger;

import org.gong.bmw.R;
import org.gong.bmw.control.BootController;

/**
 * @author caroline
 * @date 2018/6/27
 */

public class MainBoot extends Boot {

    private  transient Bitmap imageCache = null;

    public MainBoot(Context context) {
        super(context);
    }

    @Override
    public void initBy(Context context) {
        super.initBy(context);
        setPositionHorizon(0.5f);
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
