package org.gong.bmw.model.sea.enemy;

import android.content.Context;

import org.gong.bmw.control.BootController;
import org.gong.bmw.model.GameItemState;
import org.gong.bmw.model.GamePoint;
import org.gong.bmw.model.sea.BaseBoot;

/**
 * @author caroline
 * @date 2018/6/28
 */

public abstract class EnemyBaseBoot extends BaseBoot {
    protected EnemyBootState enemyBootState = new EnemyBootState(State.normal);
    private int nowHP;
    private AttackCallBack attackCallBack;

    public EnemyBaseBoot(Context context) {
        super(context);
    }

    @Override
    public void initBoot(Context context) {
        this.context = context;
        EnemyAbility enemyAbility = getAbility();
        setDirect(Direct.Left);
        float positionVertical = 0.9f - (float) (Math.random() * 0.5f);
        setPoint(new GamePoint(1, positionVertical));
        setSpeed(enemyAbility.getSpeed());
        nowHP = enemyAbility.getHP();
    }

    /**
     * @return
     */
    public abstract EnemyAbility getAbility();

    @Override
    public final void setSpeed(Speed speed) {
        super.setSpeed(speed);
    }

    @Override
    public boolean shouldRemove() {
        return enemyBootState.state == State.end;
    }

    @Override
    public final void setMoving(boolean moved) {
        if (getDirect() == Direct.Stay && !moved && !inFrame()) {
            //不再移动，也不在窗口内，消失
            fade();
        }
    }

    @Override
    public void joyStick(int angle, int strength) {

    }

    private void fade() {
        enemyBootState = new EnemyBootState(State.end);
    }

    @Override
    public EnemyBootState getGameItemState() {
        return enemyBootState;
    }

    @Override
    public void setGameItemState(GameItemState state) {
        this.enemyBootState = (EnemyBootState) state;
    }

    /**
     * @param value
     * @return 是否毁灭
     */
    public final boolean onDamage(int value) {
        nowHP -= value;
        if (nowHP > 0) {
            return false;
        } else {
            bomb();
            return true;
        }
    }

    /**
     * 作弊按钮
     */
    public final void onCheatDamage() {
        onDamage(nowHP);
    }

    private void bomb() {
        if (enemyBootState.state != State.broken) {
            enemyBootState = new EnemyBootState(State.broken);
        }
        joyButton(Code.Sink);
    }

    public abstract EnemySupply getSupply();

    @Override
    public BootController getController() {
        return this;
    }

    public void setAttackCallBack(AttackCallBack attackCallBack) {
        this.attackCallBack = attackCallBack;
    }

    public void attack() {
        if (attackCallBack != null) {
            attackCallBack.attack();
            attackCallBack = null;
            enemyBootState = new EnemyBootState(State.normal);
        }
    }

    /**
     * 敌人的状态
     */
    public enum State {
        normal, broken, readyAttack, attacking, end,
    }

    public interface AttackCallBack {
        /**
         * 执行攻击
         */
        void attack();
    }

    public class EnemyBootState extends GameItemState {

        private EnemyBaseBoot.State state;

        EnemyBootState(EnemyBaseBoot.State state) {
            this.state = state;
            switch (state) {
                case normal:
                    setNextStateCallBack(100, () -> new EnemyBootState(State.readyAttack));
                    break;
                case readyAttack:
                    setNextStateCallBack(100, () -> new EnemyBootState(State.attacking));
                    break;
                case attacking:
                    setNextStateCallBack(100, () -> new EnemyBootState(State.normal));
                    break;
                case broken:
                    setNextStateCallBack(100, () -> new EnemyBootState(State.end));
                    break;
                case end:
                    setNextStateCallBack(1000, () -> new EnemyBootState(State.end));
                    break;
                default:
                    break;
            }
        }

        public EnemyBaseBoot.State getState() {
            return state;
        }

        @Override
        public String toString() {
            return state.toString();
        }
    }


}
