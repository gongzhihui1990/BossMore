package org.gong.bmw.model;

/**
 * Created by caroline on 2018/6/28.
 */

public class GamePoint {
    /**
     * 位置 0-1
     */
    private float positionHorizon;
    /**
     * 位置 0-1
     */
    private float positionVertival;


    public GamePoint(float positionHorizon, float positionVertival) {
        this.positionHorizon = positionHorizon;
        this.positionVertival = positionVertival;
    }

    public float getPositionHorizon() {
        return positionHorizon;
    }

    public float getPositionVertival() {
        return positionVertival;
    }
}
