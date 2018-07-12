package org.gong.bmw.model.sea;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.gong.bmw.R;

/**
 * @author caroline
 * @date 2018/6/28
 */

public class U26 extends EnemyBoot {

    private static Bitmap imageCache = null;

    public U26(Context context) {
        super(context);
    }

    @Override
    public void init() {
        setSpeed(Speed.L3);
    }


    @Override
    public Bitmap getBitmap() {
        if (imageCache == null) {
            imageCache = BitmapFactory.decodeResource(context.getResources(), R.mipmap.game_sub_u26);
        }
        return imageCache;
    }


}
