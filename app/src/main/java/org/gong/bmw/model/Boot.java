package org.gong.bmw.model;

import android.content.Context;
import android.support.annotation.NonNull;

import net.gtr.framework.util.Loger;

import org.gong.bmw.control.BootController;

import java.util.UUID;

/**
 * @author caroline
 * @date 2018/6/27
 */

public abstract class Boot implements GameBoot {
    /**
     * 边界屏幕宽度千分比
     */
    private static final float BOUNDARY = 0.01f;
    protected Context context;
    /**
     * 位置 0-1
     */
    private float positionVertical = 0f;
    /**
     * 位置 0-1
     */
    private float positionHorizon = 0f;
    /**
     * 速度屏幕宽度千分比
     */
    private float speed = Speed.L1.speed;
    /**
     * 前进方向
     */
    private Direct direct = Direct.Stay;
    private boolean shouldRemove = false;
    private UUID uuid;

    Boot(Context context) {
        initBy(context);
    }

    public void onRemove() {
        shouldRemove = true;
    }

    public boolean shouldRemove() {
        return shouldRemove;
    }

    @Override
    public void initBy(Context context) {
        this.context = context;
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed.speed;
    }

    /**
     * 0-1
     *
     * @return
     */
    public float getPositionHorizon() {
        return positionHorizon;
    }

    public void setPositionHorizon(float positionHorizon) {
        this.positionHorizon = positionHorizon;
    }

    /**
     * 0-1
     *
     * @return
     */
    public float getPositionVertical() {
        return positionVertical;
    }

    public void setPositionVertical(float positionVertical) {
        this.positionVertical = positionVertical;
    }

    public @NonNull
    Direct getDirect() {
        return direct;
    }

    public void setDirect(Direct direct) {
        this.direct = direct;
    }

    public abstract BootController getController();

    protected boolean inFrame() {
        return Boundary.Mid == getBoundary();

    }

    private Boundary getBoundary() {
        if (positionHorizon < BOUNDARY) {
            return Boundary.Left;
        }
        if (positionHorizon > 1 - BOUNDARY) {
            return Boundary.Right;
        }
        return Boundary.Mid;
    }

    @Override
    public void receiveCode(BootController.Code code) {
        Loger.INSTANCE.e("receiveCode:" + code);
        switch (code) {
            case Right:
                direct = Direct.Right;
                break;
            case Left:
                direct = Direct.Left;
                break;
            case Stop:
                direct = Direct.Stay;
                break;
            default:
                break;
        }
    }

    @Override
    public final void move() {
        //TODO
        switch (direct) {
            case Stay:
                onMoved(false);
                break;
            case Left:
                if (getBoundary() != Boundary.Left) {
                    onMoved(true);
                    moveBy(-speed);
                } else {
                    direct = Direct.Stay;
                }
                break;
            case Right:
                onMoved(true);
                if (getBoundary() != Boundary.Right) {
                    onMoved(true);
                    moveBy(speed);
                } else {
                    direct = Direct.Stay;
                }
                break;
            default:
                break;
        }
        onMoved(false);
    }

    private void moveBy(float distanceHorizon) {
        positionHorizon += distanceHorizon;
        if (positionHorizon > 1) {
            positionHorizon = 1;
        }
        if (positionHorizon < 0) {
            positionHorizon = 0;
        }
    }

    abstract void onMoved(boolean moved);

    enum Speed {
        L1(0.001f), L2(0.0012f), L3(0.002f), L4(0.0028f), L5(0.0032f);

        float speed;

        Speed(float s) {
            speed = s;
        }
    }


    enum Boundary {
        Left, Mid, Right
    }

    public enum Direct {
        Right(1), Stay(0), Left(-1);

        int direct;

        Direct(int i) {
            this.direct = i;
        }
    }


}
