package org.gong.bmw.model;

import org.gong.bmw.control.GameItemInterface;
import org.gong.bmw.control.Movable;

/**
 * @author caroline
 * @date 2018/6/28
 */

public abstract class GameItemView implements GameItemInterface<GameItemState>, Movable {

    /**
     * 返回view的位置
     *
     * @return view的位置
     */
    public abstract GamePoint getPosition();

    @Override
    public void move() {
        GameItemState itState = getGameItemState();
        if (itState != null) {
            GameItemState next = itState.changeTo();
            setGameItemState(next);
        }
    }
}
