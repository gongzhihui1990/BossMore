package org.gong.bmw.model.sea.enemy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.gong.bmw.R;

/**
 * Created by caroline on 2018/6/28.
 */

public class U21_PLUS extends EnemyBaseBoot {
    private static Bitmap imageCache = null;

    public U21_PLUS(Context context) {
        super(context);
    }


    @Override
    public EnemyAbility getAbility() {
        return EnemyAbility.Builder.create()
                .setHP(1)
                .setSpeed(Speed.L6).build();
    }

    @Override
    public Bitmap getBitmap() {
        if (imageCache == null) {
            imageCache = BitmapFactory.decodeResource(context.getResources(), R.mipmap.game_sub_21);
        }
        return imageCache;
    }

    @Override
    public EnemySupply getSupply() {
        return EnemySupply.Builder.create(100)
                .setBoom(2)
                .setOil(5f)
                .build();
    }
}
