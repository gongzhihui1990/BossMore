package org.gong.bmw.model.sea.enemy;

import android.content.Context;
import android.graphics.Bitmap;

import org.gong.bmw.model.Speed;

import static org.gong.bmw.game.GameResource.SUB_21;

/**
 * Created by caroline on 2018/6/28.
 */

public class U21 extends EnemyBaseBoot {

    public U21(Context context) {
        super(context);
    }

    @Override
    public EnemyAbility getAbility() {
        return EnemyAbility.Builder.create()
                .setHP(1)
                .setSpeed(Speed.L5).build();
    }


    @Override
    public Bitmap getBitmap() {
        return SUB_21;
    }

    @Override
    public EnemySupply getSupply() {
        return EnemySupply.Builder.create(10)
                .setBoom(3)
                .setOil(3f)
                .build();
    }
}
