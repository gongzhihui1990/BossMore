package org.gong.bmw.model;

/**
 * @author caroline
 * @date 2018/6/28
 * <p>
 * 0__________________________1,0
 * |                          |
 * |                          |
 * |                          |
 * |                          |
 * |         screen           |
 * |                          |
 * |                          |
 * |                          |
 * |                          |
 * |__________________________|
 * 0,1                       1,1
 */

public class GamePoint {
    /**
     * 位置 0-1
     */
    private float x;
    /**
     * 位置 0-1
     */
    private float y;


    public GamePoint(float positionHorizon, float positionVertical) {
        this.x = positionHorizon;
        this.y = positionVertical;
    }

    public GamePoint(GamePoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void move(float distanceX, float distanceY) {
        moveX(distanceX);
        moveY(distanceY);
    }

    public void moveX(float distanceX) {
        x = moveBy(x, distanceX);
    }

    public void moveY(float distanceY) {
        y = moveBy(y, distanceY);
    }

    private float moveBy(float p, float distance) {
        p += distance;
        if (p > 1) {
            p = 1;
        }
        if (p < 0) {
            p = 0;
        }
        return p;
    }

}
