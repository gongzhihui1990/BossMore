package org.gong.bmw.model;

import android.content.Context;
import android.graphics.Bitmap;

import org.gong.bmw.control.BootController;

/**
 * Created by caroline on 2018/6/28.
 */

public class U21 extends EnemyBoot {
    public U21(EnemyBootCallBack enemyBootCallBack, Context context) {
        super(enemyBootCallBack, context);
    }

    @Override
    public void init() {
        setSpeed(Speed.L2);
    }


    @Override
    public Bitmap getBitmap() {
        return null;
    }

    @Override
    public BootController getController() {
        return null;
    }

}
