package org.gong.bmw.model;

import org.gong.bmw.control.Changable;

/**
 * @author caroline
 * @date 2018/6/29
 */

public class GameItemState implements Changable {
    private int times = -1;
    private NextStateCallBack nextStateCallBack;

    /**
     * @param delay
     * @param callBack
     */
    public void setNextStateCallBack(int delay, NextStateCallBack callBack) {
        this.times = delay;
        this.nextStateCallBack = callBack;
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
        if (nextStateCallBack != null) {
            return nextStateCallBack.onNext();
        }
        return this;
    }
}
