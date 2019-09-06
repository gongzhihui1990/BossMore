package org.gong.bmw.model.sea.enemy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.gong.bmw.R;

/**
 * @author caroline
 * @date 2018/6/28
 */

public class U50 extends EnemyBaseBoot {

    private static Bitmap imageCache = null;


    public U50(Context context) {
        super(context);
    }


    @Override
    public EnemyAbility getAbility() {
        return EnemyAbility.Builder.create()
                .setHP(3)
                .setSpeed(Speed.L3).build();
    }

    @Override
    public EnemySupply getSupply() {
        return EnemySupply.Builder.create(20)
                .setBoom(5)
                .setOil(5f)
                .build();
    }

    @Override
    public Bitmap getBitmap() {
        if (imageCache == null) {
            imageCache = BitmapFactory.decodeResource(context.getResources(), R.mipmap.game_sub_u50);
        }
        return imageCache;
    }


}
