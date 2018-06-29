package org.gong.bmw.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.gong.bmw.App;
import org.gong.bmw.R;

/**
 * @author caroline
 * @date 2018/6/28
 */

public class WaterBomb extends GameItemView {
    private static Bitmap imageCache;
    private static Bitmap imageCacheBomb;
    /**
     * 位置 0-1
     */
    private GamePoint gamePoint = new GamePoint(0, 0);
    /**
     * 速度屏幕宽度千分比
     */
    private float speed = WaterBomb.Speed.L3.speed;
    private WaterBombState waterBombState = new WaterBombState(State.Run);

    public WaterBomb releaseAt(GamePoint point) {
        this.gamePoint = new GamePoint(point);
        return this;
    }

    @Override
    public Bitmap getBitmap() {
        switch (waterBombState.getState()) {
            case Bomb:
                if (imageCacheBomb == null) {
                    imageCacheBomb = BitmapFactory.decodeResource(App.Companion.getInstance().getResources(), R.mipmap.bombbed);
                }
                return imageCacheBomb;
            default:
                if (imageCache == null) {
                    imageCache = BitmapFactory.decodeResource(App.Companion.getInstance().getResources(), R.mipmap.game_bomb);
                }
                return imageCache;
        }

    }

    @Override
    public GamePoint getPoint() {
        return gamePoint;
    }

    @Override
    public void move() {
        super.move();
        if (waterBombState.getState() == State.Run) {
            gamePoint.moveY(speed);
            if (gamePoint.getY() >= 0.95) {
                //炸到海底
                bomb();
            }
        }
    }

    public void bomb() {
        waterBombState = new WaterBombState(State.Bomb);
    }

    @Override
    public boolean shouldRemove() {
        return waterBombState.getState() == State.End;
    }


    @Override
    public GameItemState getGameItemState() {
        return waterBombState;
    }

    @Override
    public void setGameItemState(GameItemState state) {
        waterBombState = (WaterBombState) state;
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

    private class WaterBombState extends GameItemState {
        State state;

        public WaterBombState(State state) {
            switch (state) {
                case Run:
                    break;
                case Bomb:
                    setTimes(10);
                    setNextState(new WaterBombState(State.End));
                    break;
                case End:
                    break;
                default:
                    break;
            }
            this.state = state;
        }

        public State getState() {
            return state;
        }
    }
}
