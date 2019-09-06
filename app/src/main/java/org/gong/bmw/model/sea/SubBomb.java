package org.gong.bmw.model.sea;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.gong.bmw.App;
import org.gong.bmw.R;
import org.gong.bmw.model.GameItemBitmapView;
import org.gong.bmw.model.GameItemState;
import org.gong.bmw.model.GamePoint;

/**
 * @author caroline
 * @date 2018/6/28
 * 潜艇对船只发射的导弹
 */

public class SubBomb extends GameItemBitmapView {
    private static Bitmap imageCache;
    private static Bitmap imageCacheBomb;
    float x, y;
    private float rotation = 0;
    /**
     * 位置 0-1
     */
    private GamePoint gamePoint = new GamePoint(0, 0);
    /**
     * 速度屏幕宽度千分比
     */
    private float speed = SubBomb.Speed.L5.speed;
    private float angelXY = 0f;
    private WaterBombState waterBombState = new WaterBombState(State.Run);

    public SubBomb releaseAt(GamePoint sendPoint, GamePoint targetPoint) {
        this.gamePoint = new GamePoint(sendPoint);
        x = sendPoint.getX() - targetPoint.getX();
        y = sendPoint.getY() - targetPoint.getY();
        rotation = x / y;
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
    public GamePoint getPosition() {
        return gamePoint;
    }

    @Override
    public void move() {
        super.move();
        if (waterBombState.getState() == State.Run) {
            gamePoint.moveX(speed * (x / x + y));
            gamePoint.moveY(speed * (y / x + y));
            if (gamePoint.getY() <= 0.33) {
                //炸天
                bomb();
            }
        }
    }

    /**
     * @return 返回炸弹的伤害值
     */
    public int bomb() {
        waterBombState = new WaterBombState(State.Bomb);
        return 1;
    }

    @Override
    public boolean shouldRemove() {
        return waterBombState.getState() == State.End;
    }


    @Override
    public WaterBombState getGameItemState() {
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


    public enum State {
        Run, Bomb, End
    }

    public class WaterBombState extends GameItemState {
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
