package org.gong.bmw.model;

import net.gtr.framework.util.Loger;

import org.gong.bmw.control.Changable;

/**
 * @author caroline
 * @date 2018/6/29
 */

public class GameItemState implements Changable {
    private int times = -1;
    private GameItemState nextState;


    public void setTimes(int times) {
        this.times = times;
    }

    public void setNextState(GameItemState nextState) {
        this.nextState = nextState;
    }

    @Override
    public GameItemState changeTo() {
        if (times == -1) {
            return this;
        }
        if (times > 0) {
            times--;
            return this;
        }
        if (nextState != null) {
            return nextState;
        }
        return this;
    }
}
