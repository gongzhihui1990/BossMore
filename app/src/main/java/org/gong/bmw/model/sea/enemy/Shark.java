package org.gong.bmw.model.sea.enemy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.gong.bmw.R;

/**
 * @author caroline
 * @date 2018/6/28
 */

public class Shark extends EnemyBaseBoot {

    private static Bitmap imageCache = null;

    public Shark(Context context) {
        super(context);
    }


    @Override
    public EnemyAbility getAbility() {
        return EnemyAbility.Builder.create()
                .setHP(2)
                .setSpeed(Speed.L4).build();
    }
    @Override
    public EnemySupply getSupply() {
        return EnemySupply.Builder.create(20)
                .setFood(5)
                .build();
    }

    @Override
    public Bitmap getBitmap() {
        if (imageCache == null) {
            imageCache = BitmapFactory.decodeResource(context.getResources(), R.mipmap.game_sub_shark);
        }
        return imageCache;
    }


}
