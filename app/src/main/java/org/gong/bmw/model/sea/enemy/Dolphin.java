package org.gong.bmw.model.sea.enemy;

import android.content.Context;
import android.graphics.Bitmap;

import org.gong.bmw.game.GameResource;
import org.gong.bmw.model.Speed;

/**
 * @author caroline
 * @date 2018/6/28
 */

public class Dolphin extends EnemyBaseBoot {


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
        return GameResource.Dolphin;
    }


}
