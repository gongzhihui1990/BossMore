package org.gong.bmw.model.sea.enemy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.gong.bmw.R;

/**
 * @author caroline
 * @date 2018/6/28
 */

public class U26 extends EnemyBaseBoot {

    private static Bitmap imageCache = null;

    public U26(Context context) {
        super(context);
    }

    @Override
    public EnemySupply getSupply() {
        return EnemySupply.Builder.create(5)
                .setBoom(1)
                .setOil(2f)
                .build();
    }


    @Override
    public EnemyAbility getAbility() {
        return EnemyAbility.Builder.create()
                .setHP(1)
                .setSpeed(Speed.L3).build();
    }

    @Override
    public Bitmap getBitmap() {
        if (imageCache == null) {
            imageCache = BitmapFactory.decodeResource(context.getResources(), R.mipmap.game_sub_u26);
        }
        return imageCache;
    }


}
