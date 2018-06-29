package org.gong.bmw.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.gong.bmw.App;
import org.gong.bmw.R;
import org.gong.bmw.control.GameItemInterface;

/**
 * @author caroline
 * @date 2018/6/28
 */

public class WaterBomb extends GameItemView {
    private Bitmap imageCache;
    /**
     * 位置 0-1
     */
    private GamePoint gamePoint = new GamePoint(0, 0);
    /**
     * 速度屏幕宽度千分比
     */
    private float speed = WaterBomb.Speed.L2.speed;
    private State state = State.Run;

    public WaterBomb releaseAt(GamePoint point) {
        this.gamePoint = new GamePoint(point);
        return this;
    }

    @Override
    public Bitmap getBitmap() {
        if (imageCache == null) {
            imageCache = BitmapFactory.decodeResource(App.Companion.getInstance().getResources(), R.mipmap.game_bomb);
        }
        return imageCache;
    }

    @Override
    public GamePoint getPoint() {
        return gamePoint;
    }

    @Override
    public void move() {
        super.move();
        gamePoint.moveY(speed);
        if (gamePoint.getY() == 1) {
            state = State.Bomb;
        }
    }

    @Override
    public boolean shouldRemove() {
        return state == State.End;
    }


    @Override
    public GameItemState getGameItemState() {
        return new GameItemState();
    }

    @Override
    public void setGameItemState(GameItemState state) {

    }


    enum Speed {
        L1(0.002f), L2(0.003f), L3(0.004f), L4(0.005f), L5(0.006f);

        float speed;

        Speed(float s) {
            speed = s;
        }
    }

    enum State {
        Run, Bomb, End
    }
}
