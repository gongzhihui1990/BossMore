package org.gong.bmw.model.sea.enemy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.gong.bmw.R;

/**
 * @author caroline
 * @date 2018/6/28
 */

public class Dolphin extends EnemyBaseBoot {

    private static Bitmap imageCache = null;

    public Dolphin(Context context) {
        super(context);
    }

    @Override
    public EnemyAbility getAbility() {
        return EnemyAbility.Builder.create()
                .setHP(1)
                .setSpeed(Speed.L3).build();
    }
    @Override
    public EnemySupply getSupply() {
        return EnemySupply.Builder.create(20)
                .setFood(1)
                .build();
    }

    @Override
    public Bitmap getBitmap() {
        if (imageCache == null) {
            imageCache = BitmapFactory.decodeResource(context.getResources(), R.mipmap.game_whale);
        }
        return imageCache;
    }


}
