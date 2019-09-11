package org.gong.bmw.model.sea.enemy;

import android.content.Context;
import android.graphics.Bitmap;

import org.gong.bmw.model.Speed;

import static org.gong.bmw.game.GameResource.SUB_26;

/**
 * @author caroline
 * @date 2018/6/28
 */

public class U26 extends EnemyBaseBoot {


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
        return SUB_26;
    }


}
