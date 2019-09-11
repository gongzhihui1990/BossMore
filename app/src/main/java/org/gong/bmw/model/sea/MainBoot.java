package org.gong.bmw.model.sea;

import android.content.Context;
import android.graphics.Bitmap;

import net.gtr.framework.util.Loger;

import org.gong.bmw.control.BootController;
import org.gong.bmw.game.GameResource;
import org.gong.bmw.model.GameItemState;
import org.gong.bmw.model.GamePoint;
import org.gong.bmw.model.Speed;

/**
 * @author caroline
 * @date 2018/6/27
 */

public final class MainBoot extends BaseBoot {

    private MainBootState bootState = new MainBootState(State.normal);


    public MainBoot(Context context) {
        super(context);
    }

    @Override
    public void initBoot(Context context) {
        super.initBoot(context);
        //设置初始屏幕位置
        setPoint(new GamePoint(0.5f, 0.2f));
        setSpeed(Speed.L5);
    }

    @Override
    public BootController getController() {
        return this;
    }

    @Override
    public void setMoving(boolean moved) {

    }

    @Override
    public boolean joyButton(Code code) {
        switch (code) {
            case ReleaseBomb:
                bootState = new MainBootState(State.sendBomb);
                return true;
            default:
                return super.joyButton(code);
        }
    }

    @Override
    public void joyStick(int angle, int strength) {
        Loger.INSTANCE.d("strength:" + strength);
        if ((angle > 0 && angle < 90) || angle > 270) {
            joyButton(Code.Right);
        } else if (angle > 90 && angle < 270) {
            joyButton(Code.Left);
        } else {
            joyButton(BootController.Code.Stop);
        }
    }

    @Override
    public Bitmap getBitmap() {
        return GameResource.GameBoot;
    }

    @Override
    public MainBootState getGameItemState() {
        return bootState;
    }

    @Override
    public void setGameItemState(GameItemState state) {
        this.bootState = (MainBootState) state;
    }

    public enum State {
        normal, sendBomb, loadingBomb
    }

    public class MainBootState extends GameItemState {

        private State state;

        public MainBootState(State state) {
            this.state = state;
            switch (state) {
                case normal:
                    break;
                case sendBomb:
                    setNextStateCallBack(5, () -> new MainBootState(State.loadingBomb));
                    break;
                case loadingBomb:
                    setNextStateCallBack(10, () -> new MainBootState(State.normal));
                default:
                    break;
            }
        }

        public State getState() {
            return state;
        }

        @Override
        public String toString() {
            return state.toString();
        }
    }
}
