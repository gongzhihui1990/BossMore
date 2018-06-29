package org.gong.bmw.control;

import org.gong.bmw.model.GameItemState;

/**
 * @author caroline
 * @date 2018/6/28
 */

public interface GameItemInterface<T> {
    /**
     * 是否需要移除
     */
    boolean shouldRemove();

    /**
     * GameItemState
     * 返回
     */
    T getGameItemState();

    /**
     * 返回
     */
    void setGameItemState(T state);

}
