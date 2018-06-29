package org.gong.bmw.control;

/**
 *
 * @author caroline
 * @date 2018/6/28
 */

public interface GameItemInterface {

    /**
     * 时间pass
     */
    void move();
    /**
     * 是否需要移除
     */
    boolean shouldRemove();
}
