package org.gong.bmw.model.sea;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.gong.bmw.R;

/**
 * @author caroline
 * @date 2018/6/28
 */

public class Fish extends EnemyBoot {

    private static Bitmap imageCache = null;

    public Fish(Context context) {
        super(context);
    }

    @Override
    public void init() {
        setSpeed(Speed.L3);
    }

    @Override
    public int getScore() {
        return 1;
    }

    @Override
    public Bitmap getBitmap() {
        if (imageCache == null) {
            imageCache = BitmapFactory.decodeResource(context.getResources(), R.mipmap.game_whale);
        }
        return imageCache;
    }


}
