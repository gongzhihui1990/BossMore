package org.gong.bmw.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.gong.bmw.App;
import org.gong.bmw.R;
import org.gong.bmw.control.BootController;

/**
 *
 * @author caroline
 * @date 2018/6/28
 */

public class U26 extends EnemyBoot {

    private transient Bitmap imageCache = null;

    public U26(EnemyBootCallBack enemyBootCallBack, Context context) {
        super(enemyBootCallBack, context);
    }

    @Override
    public void init() {
        setSpeed(Speed.L3);
    }


    @Override
    public Bitmap getBitmap() {
        if (imageCache == null) {
            imageCache = BitmapFactory.decodeResource(context.getResources(), R.mipmap.game_sub);
        }
        return imageCache;
    }

    @Override
    public BootController getController() {
        return this;
    }

    @Override
    public GameItemState getGameItemState() {
        return new GameItemState();
    }
}
