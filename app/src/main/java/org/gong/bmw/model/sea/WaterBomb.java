package org.gong.bmw.model.sea;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.gong.bmw.App;
import org.gong.bmw.R;
import org.gong.bmw.model.GameItemBitmapView;
import org.gong.bmw.model.GameItemState;
import org.gong.bmw.model.GamePoint;
import org.gong.bmw.model.Speed;

/**
 * @author caroline
 * @date 2018/6/28
 */

public class WaterBomb extends GameItemBitmapView {
    private static Bitmap imageCache;
    private static Bitmap imageCacheBomb;
    private float rotation = 0;
    /**
     * 位置 0-1
     */
    private GamePoint gamePoint = new GamePoint(0, 0);
    /**
     * 速度屏幕宽度千分比
     */
    private float speed = Speed.L3.speed;
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
    public GamePoint getPosition() {
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

    public float rotation() {
        rotation += 3;
        return rotation;
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
                    setNextStateCallBack(10,() -> new WaterBombState(State.End));
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
