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

public abstract class Boot implements GameBoot, GameItemView {
    /**
     * 边界屏幕宽度千分比
     */
    private static final float BOUNDARY = 0.01f;
    protected Context context;

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
    private GamePoint point = new GamePoint(0, 0);


    Boot(Context context) {
        initBoot(context);
    }

    public void onRemove() {
        shouldRemove = true;
    }

    @Override
    public boolean shouldRemove() {
        return shouldRemove;
    }

    @Override
    public void initBoot(Context context) {
        this.context = context;
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed.speed;
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
        if (point.getX() < BOUNDARY) {
            return Boundary.Left;
        }
        if (point.getX() > 1 - BOUNDARY) {
            return Boundary.Right;
        }
        return Boundary.Mid;
    }

    @Override
    public boolean receiveCode(BootController.Code code) {
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
        return true;
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
        point.moveX(distanceHorizon);
    }

    abstract void onMoved(boolean moved);

    @Override
    public GamePoint getPoint() {
        return point;
    }

    public void setPoint(GamePoint point) {
        this.point = point;
    }

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
