package org.gong.bmw.model.sea;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.gong.bmw.R;
import org.gong.bmw.control.BootController;
import org.gong.bmw.model.GameItemState;
import org.gong.bmw.model.GamePoint;

/**
 * @author caroline
 * @date 2018/6/27
 */

public final class MainBoot extends BaseBoot {

    private transient Bitmap imageCache = null;
    private MainBootState bootState = new MainBootState(State.normal);

    public MainBoot(Context context) {
        super(context);
    }

    @Override
    public void initBoot(Context context) {
        super.initBoot(context);
        //设置初始屏幕位置
        setPoint(new GamePoint(0.5f, 0.3f));
        setSpeed(Speed.L5);
    }

    @Override
    public BootController getController() {
        return this;
    }

    @Override
    void onMoved(boolean moved) {

    }

    @Override
    public boolean receiveCode(Code code) {
        switch (code) {
            case ReleaseBomb:
                bootState = new MainBootState(State.sendBomb);
                return true;
            default:
                return super.receiveCode(code);
        }
    }

    @Override
    public Bitmap getBitmap() {
        if (imageCache == null) {
            imageCache = BitmapFactory.decodeResource(context.getResources(), R.mipmap.game_boot);
        }
        return imageCache;
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
                    setTimes(5);
                    setNextState(new MainBootState(State.loadingBomb));
                    break;
                case loadingBomb:
                    setTimes(10);
                    setNextState(new MainBootState(State.normal));
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
