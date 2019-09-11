package org.gong.bmw.model.sea.enemy;

import android.content.Context;
import android.graphics.Bitmap;

import org.gong.bmw.game.GameResource;
import org.gong.bmw.model.Speed;

/**
 * @author caroline
 * @date 2018/6/28
 */

public class Shark extends EnemyBaseBoot {


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
        return GameResource.Shark;
    }


}
