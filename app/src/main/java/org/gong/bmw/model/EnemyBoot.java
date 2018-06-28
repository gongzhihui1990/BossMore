package org.gong.bmw.model;

import android.content.Context;

import net.gtr.framework.util.Loger;

import java.util.UUID;

/**
 * @author caroline
 * @date 2018/6/28
 */

public abstract class EnemyBoot extends Boot {
    EnemyBootCallBack enemyBootCallBack;

    public EnemyBoot(EnemyBootCallBack enemyBootCallBack, Context context) {
        super(context);
        this.enemyBootCallBack = enemyBootCallBack;
    }

    @Override
    public void initBy(Context context) {
        this.context = context;
        setDirect(Direct.Left);
        setPositionHorizon(1);
        setPositionVertical(1-(float) Math.random() * 0.8f);
        setSpeed(Speed.L1);
        init();
    }

    @Override
    void onMoved(boolean moved) {
        Loger.INSTANCE.i("moved:" + moved);
        if (getDirect() == Direct.Stay && !moved && !inFrame()) {
            //消失
            enemyBootCallBack.fade(this);
        }
    }


    abstract public void init();
}
