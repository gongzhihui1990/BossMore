package org.gong.bmw.model;

import android.content.Context;

import net.gtr.framework.util.Loger;

/**
 * @author caroline
 * @date 2018/6/28
 */

public abstract class EnemyBoot extends Boot {
    private EnemyBootCallBack enemyBootCallBack;

    public EnemyBoot(EnemyBootCallBack enemyBootCallBack, Context context) {
        super(context);
        this.enemyBootCallBack = enemyBootCallBack;
    }

    @Override
    public void initBoot(Context context) {
        this.context = context;
        setDirect(Direct.Left);
        setPoint(new GamePoint(1,0.9f-(float) Math.random() * 0.8f));
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
