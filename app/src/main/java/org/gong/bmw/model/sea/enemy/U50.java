package org.gong.bmw.model.sea.enemy;

import android.content.Context;
import android.graphics.Bitmap;

import org.gong.bmw.model.Speed;

import static org.gong.bmw.game.GameResource.SUB_50;

/**
 * @author caroline
 * @date 2018/6/28
 */

public class U50 extends EnemyBaseBoot {



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
        return SUB_50;
    }


}
