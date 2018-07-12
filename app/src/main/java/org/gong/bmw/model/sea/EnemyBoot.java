package org.gong.bmw.model.sea;

import android.content.Context;
import android.content.Intent;

import net.gtr.framework.util.Loger;

import org.gong.bmw.control.BootController;
import org.gong.bmw.model.GameItemState;
import org.gong.bmw.model.GamePoint;

/**
 * @author caroline
 * @date 2018/6/28
 */

public abstract class EnemyBoot extends BaseBoot {
    protected EnemyBootState enemyBootState = new EnemyBootState(State.normal);

    public EnemyBoot(Context context) {
        super(context);
    }

    @Override
    public void initBoot(Context context) {
        this.context = context;
        setDirect(Direct.Left);
        setPoint(new GamePoint(1, 1.1f - (float) Math.random() * 0.8f));
        setSpeed(Speed.L1);
        init();
    }

    @Override
    public boolean shouldRemove() {
        return enemyBootState.state == State.end;
    }

    @Override
    void onMoved(boolean moved) {
        Loger.INSTANCE.i("moved:" + moved);
        if (getDirect() == Direct.Stay && !moved && !inFrame()) {
            //消失
            fade();
        }
    }


    public void fade() {
        enemyBootState = new EnemyBootState(State.end);
    }


    @Override
    public GameItemState getGameItemState() {
        return enemyBootState;
    }

    @Override
    public void setGameItemState(GameItemState state) {
        this.enemyBootState = (EnemyBootState) state;
    }

    abstract public void init();

    public void bomb() {
        if (enemyBootState.state == State.normal) {
            enemyBootState = new EnemyBootState(State.shot);
        }
        context.sendBroadcast(new Intent("com.xilai.express.playaudio_start"));
        receiveCode(Code.Sink);
    }

    public enum State {
        normal, shot, end
    }

    public class EnemyBootState extends GameItemState {

        private EnemyBoot.State state;

        public EnemyBootState(EnemyBoot.State state) {
            this.state = state;
            switch (state) {
                case normal:
                    break;
                case shot:
                    setTimes(100);
                    setNextState(new EnemyBoot.EnemyBootState(EnemyBoot.State.end));
                default:
                    break;
            }
        }

        public EnemyBoot.State getState() {
            return state;
        }

        @Override
        public String toString() {
            return state.toString();
        }
    }
    @Override
    public BootController getController() {
        return this;
    }
}
