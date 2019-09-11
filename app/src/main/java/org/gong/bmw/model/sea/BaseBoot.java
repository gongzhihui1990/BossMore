package org.gong.bmw.model.sea;

import android.content.Context;
import android.support.annotation.NonNull;

import org.gong.bmw.control.BootController;
import org.gong.bmw.model.GameItemBitmapView;
import org.gong.bmw.model.GamePoint;
import org.gong.bmw.model.Speed;

import java.util.UUID;

/**
 * @author caroline
 * @date 2018/6/27
 */

public abstract class BaseBoot extends GameItemBitmapView implements GameBoot {
    /**
     * 边界屏幕宽度千分比
     */
    private static final float BOUNDARY = 0.01f;
    protected Context context;

    /**
     * 速度为屏幕宽度千分比
     */
    private float speed = Speed.L1.speed;
    /**
     * 前进方向
     */
    private Direct direct = Direct.Stay;
    private UUID uuid;
    private GamePoint point = new GamePoint(0, 0);

    public BaseBoot(Context context) {
        initBoot(context);
    }

    @Override
    public boolean shouldRemove() {
        return false;
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
        return Boundary.Mid == getBoundaryX();
    }

    private Boundary getBoundaryX() {
        if (point.getX() < BOUNDARY) {
            return Boundary.Left;
        }
        if (point.getX() > 1 - BOUNDARY) {
            return Boundary.Right;
        }

        return Boundary.Mid;
    }

    private Boundary getBoundaryY() {
        if (point.getY() < BOUNDARY) {
            return Boundary.Top;
        }
        if (point.getY() > 1 - BOUNDARY) {
            return Boundary.Bottom;
        }

        return Boundary.Mid;
    }

    @Override
    public boolean joyButton(BootController.Code code) {
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
            case Sink:
                direct = Direct.Sink;
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public final void move() {
        super.move();
        switch (direct) {
            case Stay:
                setMoving(false);
                break;
            case Left:
                if (getBoundaryX() != Boundary.Left) {
                    setMoving(true);
                    point.moveX(-speed);
                } else {
                    direct = Direct.Stay;
                }
                break;
            case Right:
                setMoving(true);
                if (getBoundaryX() != Boundary.Right) {
                    setMoving(true);
                    point.moveX(speed);
                } else {
                    direct = Direct.Stay;
                }
                break;
            case Bottom:
                if (getBoundaryY() != Boundary.Bottom) {
                    setMoving(true);
                    point.moveY(speed / 2);
                } else {
                    direct = Direct.Stay;
                }
                break;
            case Sink:
                if (getBoundaryY() != Boundary.Bottom) {
                    setMoving(true);
                    point.move(-speed / 2, speed);
                } else {
                    direct = Direct.Stay;
                }
                break;
            default:
                break;
        }
        setMoving(false);
    }


    /**
     * 设置当前是否在移动
     *
     * @param moved
     */
    public abstract void setMoving(boolean moved);

    @Override
    public GamePoint getPosition() {
        return point;
    }

    public void setPoint(GamePoint point) {
        this.point = point;
    }





    public enum Boundary {
        Left, Mid, Right, Top, Bottom
    }

    public enum Direct {
        Right(1), Stay(0), Left(-1), Bottom(3), Sink(4);

        int direct;

        Direct(int i) {
            this.direct = i;
        }
    }


}
