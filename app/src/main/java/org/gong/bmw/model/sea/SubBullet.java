package org.gong.bmw.model.sea;

import android.graphics.Bitmap;

import org.gong.bmw.game.GameResource;
import org.gong.bmw.game.SeaFightGameView;
import org.gong.bmw.model.GameItemBitmapView;
import org.gong.bmw.model.GameItemState;
import org.gong.bmw.model.GamePoint;
import org.gong.bmw.model.Speed;
import org.gong.bmw.model.sea.enemy.EnemyBaseBoot;

/**
 * @author caroline
 * @date 2018/6/28
 * 潜艇对船只发射的导弹
 */

public class SubBullet extends GameItemBitmapView {
    private float xSpeed, ySpeed;
    /**
     * 位置 0-1
     */
    private GamePoint gamePoint = new GamePoint(0, 0);
    /**
     * 速度屏幕宽度千分比
     */
    private float speed = Speed.L3.speed;
    private WaterBombState waterBombState = new WaterBombState(State.Run);

    /**
     * 释放位置
     *
     * @param sender
     * @param target
     * @return
     */
    public SubBullet releaseAt(EnemyBaseBoot sender, MainBoot target) {
        GamePoint sendPoint = new GamePoint(
                sender.getPosition().getX() + ((float) sender.getBitmap().getWidth()) / (SeaFightGameView.getWith() * 2),
                sender.getPosition().getY() + ((float) sender.getBitmap().getHeight()) / (SeaFightGameView.getHigh() * 2));

        GamePoint targetPoint = new GamePoint(
                target.getPosition().getX() + ((float) target.getBitmap().getWidth()) / (SeaFightGameView.getWith() * 2),
                target.getPosition().getY() + ((float) target.getBitmap().getHeight()) / (SeaFightGameView.getHigh() * 2));

        this.gamePoint = new GamePoint(sendPoint);

        float tx = targetPoint.getX() - sendPoint.getX();
        float ty = targetPoint.getY() - sendPoint.getY();
        xSpeed = (float) (tx / Math.sqrt(tx * tx + ty * ty));
        ySpeed = (float) (ty / Math.sqrt(tx * tx + ty * ty));
        return this;
    }

    @Override
    public Bitmap getBitmap() {
        switch (waterBombState.getState()) {
            case Bomb:
                return GameResource.Bombed;
            default:
                return GameResource.BulletRun;
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
            gamePoint.moveX(speed * xSpeed);
            gamePoint.moveY(speed * ySpeed);
            if (gamePoint.getY() <= 0) {
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


    public enum State {
        /**
         * 子弹的状态
         */
        Run, Bomb, End
    }

    public class WaterBombState extends GameItemState {
        State state;

        public WaterBombState(State state) {
            switch (state) {
                case Run:
                    setNextStateCallBack(1000, () -> new WaterBombState(State.Bomb));
                    break;
                case Bomb:
                    setNextStateCallBack(5, () -> new WaterBombState(State.End));
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
