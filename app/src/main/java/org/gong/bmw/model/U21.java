package org.gong.bmw.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.gong.bmw.R;
import org.gong.bmw.control.BootController;

/**
 * Created by caroline on 2018/6/28.
 */

public class U21 extends EnemyBoot {
    private static Bitmap imageCache = null;

    public U21(Context context) {
        super(context);
    }

    @Override
    public void init() {
        setSpeed(Speed.L5);
    }



    @Override
    public Bitmap getBitmap() {
        if (imageCache == null) {
            imageCache = BitmapFactory.decodeResource(context.getResources(), R.mipmap.game_sub_21);
        }
        return imageCache;
    }

}
